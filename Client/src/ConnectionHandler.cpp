//
// Created by spl211 on 09/01/2021.
//

#include "ConnectionHandler.h"
#include <utility>
#include <sstream>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port): host_(std::move(host)), port_(port), io_service_(), socket_(io_service_),
terminate(0){}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
            tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed?????????? (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getFrame(std::string& frame) {
    char ch;
    bool decodedOpcodes = false;
    bool encounteredZeroByte = false;
    bool stop = false;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            if (!decodedOpcodes) {
                char opcodeArray[2];
                char opcodeArray2[2];
                if (!getBytes(opcodeArray, 2)) {
                    cout << "returned false, get frame, if_1" << endl;
                    return false;
                }
                if (!getBytes(opcodeArray2, 2)) {
                    cout << "returned false, get frame, if_1" << endl;
                    return false;
                }
                short opcodeNum = bytesToShort(opcodeArray);
                short opcodeNum2 = bytesToShort(opcodeArray2);
                if (opcodeNum == 13) {
                    stop = true;
                }
                frame.append(std::to_string(opcodeNum)).append(1, '\n');
                frame.append(std::to_string(opcodeNum2)).append(1, '\n');
                decodedOpcodes = true;
                if (opcodeNum2 == 4){
                    if (opcodeNum == 12){
                        terminate = -1; // approval of LOGOUT request
                    }
                    else{
                        terminate = 1; // disapproval of LOGOUT request
                    }
                }
            }
            else {
                if (!getBytes(&ch, 1)) {
                    return false;
                }
                if (ch != '\0') {
                    frame.append(1, ch);
                    encounteredZeroByte = false;
                }
                else{
                    if (encounteredZeroByte){
                        frame.append(1, '\n');
                        stop = true;
                    }
                    encounteredZeroByte = true;
                }
                 }
        }while (!stop);
    } catch (std::exception& e) {
        std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendFrame(const std::string& frame, short opcode) {
    char bytes[2];
    shortToBytes(opcode, bytes);
    bool numOpcode = sendBytes(bytes, 2);
    if(!numOpcode) {
        return false;
    }
    if (opcode <= 10 && opcode >= 5 && opcode != 8){
        std::stringstream stringStream(frame);
        short courseNum = 0;
        stringStream >> courseNum;
        shortToBytes(courseNum,bytes);
        return sendBytes(bytes, 2);
    }
    else
        return sendBytes(frame.c_str(),frame.length());
}

short ConnectionHandler::bytesToShort(char *bytesArray) {
    short result = (short)((bytesArray[0] & 0xff) << 8);
    result += (short)(bytesArray[1] & 0xff);
    return result;
}

void ConnectionHandler::shortToBytes(short num, char *bytesArray) {
    bytesArray[0] = ((num >> 8) & 0xff);
    bytesArray[1] = (num & 0xff);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}
int ConnectionHandler::shouldTerminate() {
    return terminate;
}
void ConnectionHandler::setTerminate(int i) {
    terminate = i;
}