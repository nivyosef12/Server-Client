LDFLAGS:=-lboost_system -lpthread
all: BGRSclient

BGRSclient: bin/main.o bin/ConnectionHandler.o bin/KeyBoardReader.o
	@echo 'Linking'
	g++ -o  bin/BGRSclient bin/main.o bin/ConnectionHandler.o bin/KeyBoardReader.o -lboost_system -lpthread

bin/main.o: src/main.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/main.o src/main.cpp

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/KeyBoardReader.o: src/KeyBoardReader.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/KeyBoardReader.o src/KeyBoardReader.cpp



clean:
	@echo 'Cleaning'
	rm -f bin/*
