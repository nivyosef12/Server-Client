//
// Created by spl211 on 09/01/2021.
//

#ifndef CLIENT_KEYBOARDREADER_H
#define CLIENT_KEYBOARDREADER_H
#include "ConnectionHandler.h"

class KeyBoardReader {
public:

    KeyBoardReader(ConnectionHandler& _handler);
    void run();

private:

    ConnectionHandler& handler;
};


#endif //CLIENT_KEYBOARDREADER_H
