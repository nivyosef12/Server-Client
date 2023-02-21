//
// Created by spl211 on 09/01/2021.
//

#ifndef CLIENT_CONNECTIONHANDLER_H
#define CLIENT_CONNECTIONHANDLER_H

#include <string>
#include <iostream>
#include <boost/asio.hpp>
//#include <boost/system/error_code.hpp>

using boost::asio::ip::tcp;
using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

class ConnectionHandler {

private:
    const std::string host_;
    const short port_;
    boost::asio::io_service io_service_;   // Provides core I/O functionality
    tcp::socket socket_;
    int terminate;

public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();

    // Connect to the remote machine
    bool connect();

    // Read a fixed number of bytes from the server - blocking.
    // Returns false in case the connection is closed before bytesToRead bytes can be read.
    bool getBytes(char bytes[], unsigned int bytesToRead);

    // Send a fixed number of bytes from the client - blocking.
    // Returns false in case the connection is closed before all the data is sent.
    bool sendBytes(const char bytes[], int bytesToWrite);

    // Get Ascii data from the server until the delimiter character
    // Returns false in case connection closed before null can be read.
    bool getFrame(std::string& frame);

    // Send a message to the remote host.
    // Returns false in case connection is closed before all the data is sent.
    bool sendFrame(const std::string& frame, short opcode);

    short bytesToShort(char* bytesArray);

    void shortToBytes(short num, char* bytesArray);

    int shouldTerminate();

    void setTerminate(int i);
    // Close down the connection properly.
    void close();

}; //class ConnectionHandler


#endif //CLIENT_CONNECTIONHANDLER_H
