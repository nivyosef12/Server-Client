#include <iostream>
#include "KeyBoardReader.h"
#include "ConnectionHandler.h"
#include <string>
#include <thread>
using namespace std;
int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    
    std::cerr << "Connected" << std::endl;
    bool terminate = false;
    KeyBoardReader keyBoardReader(connectionHandler);
    thread keyBoardThread(&KeyBoardReader::run, keyBoardReader);
    
    while (!terminate){
        string command = "";
        connectionHandler.getFrame(command);
        string words[command.size()];
        int i = 0;
        for (char c:command) {
            if (c != '\n') {
                if (c == '|')
                    c = '\n';
                words[i] = words[i] + c;
            } else {
                i = i + 1;
            }
        }
        string msgToPrint;
        if(words[0] == "12"){
            msgToPrint = "ACK " + words[1];
            if(words[2].length() > 0)
                msgToPrint = words[2];
            if(words[1] == "4") {
                terminate = true;
            }
            else if (words[1] == "9")
                msgToPrint = words[2];
            cout << msgToPrint << endl;
        }
        else if(words[0] == "13"){
            msgToPrint = "ERROR " + words[1];
            cout << msgToPrint << endl;
        }
    }
    keyBoardThread.join();
    connectionHandler.close();
    return 0;
}
