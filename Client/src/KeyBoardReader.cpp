//
// Created by spl211 on 09/01/2021.
//

#include "KeyBoardReader.h"

using namespace std;
using boost::asio::ip::tcp;

KeyBoardReader::KeyBoardReader(ConnectionHandler &_handler) : handler(_handler) {}

void KeyBoardReader::run() {
    while(handler.shouldTerminate() == 0){
        const short bufsize = 1024;
        char buf[bufsize];
        try {
            cin.getline(buf, bufsize);
        } catch (exception) {}
        string line(buf);
        string command[line.length()];
        int i = 0;
        for (char c:line) {
            if (c != ' ')
                command[i] = command[i] + c;
            else
                i = i + 1;
        }
        string msgToSend;
        if(command[0] == "ADMINREG" && i >= 2){
            msgToSend = command[1] + '\0' + command[2] + '\0';
            handler.sendFrame(msgToSend, (short)1);
        }
        else if(command[0] == "STUDENTREG" && i >= 2){
            msgToSend = command[1] + '\0' + command[2] + '\0';
            handler.sendFrame(msgToSend, (short)2);
        }
        else if(command[0] == "LOGIN" && i >= 2){
            msgToSend = command[1] + '\0' + command[2] + '\0';
            handler.sendFrame(msgToSend, (short)3);
        }
        else if(command[0] == "LOGOUT" && i <= 1){
            msgToSend = "";
            handler.sendFrame(msgToSend, (short)4);
            while (true){ // busy wait for ACK/ERR msg
                if (handler.shouldTerminate() == 1) { // ERR received
                    handler.setTerminate(0);
                    break;
                }
                else if (handler.shouldTerminate() == -1) // ACK received
                    break;
            }
        }
        else if(command[0] == "COURSEREG" && i >= 1 ){
            msgToSend = command[1];
            handler.sendFrame(msgToSend, (short)5);
        }
        else if(command[0] == "KDAMCHECK" && i >= 1 ){
            msgToSend = command[1];
            handler.sendFrame(msgToSend, (short)6);
        }
        else if(command[0] == "COURSESTAT" && i >= 1 ){
            msgToSend = command[1];
            handler.sendFrame(msgToSend, (short)7);
        }
        else if(command[0] == "STUDENTSTAT" && i >= 1 ){
            msgToSend = command[1] + '\0';
            handler.sendFrame(msgToSend, (short)8);
        }
        else if(command[0] == "ISREGISTERED" && i >= 1 ){
            msgToSend = command[1] ;
            handler.sendFrame(msgToSend, (short)9);
        }
        else if(command[0] == "UNREGISTER" && i >= 1 ){
            msgToSend = command[1];
            handler.sendFrame(msgToSend, (short)10);
        }
        else if(command[0] == "MYCOURSES" && i <= 1){
            msgToSend = "";
            handler.sendFrame(msgToSend, (short)11);
        }
        else
            cout << "invalid request" << endl;
    }
}