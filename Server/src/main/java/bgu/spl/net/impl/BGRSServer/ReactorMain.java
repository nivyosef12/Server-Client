package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        if(args.length < 2)
            throw new IllegalArgumentException("Illegal number of argument");

        Database database = Database.getInstance();;
        database.initialize("/home/spl211/Desktop/206100968#_206103863/Server/Courses.txt");

        int port= Integer.parseInt(args[0]);
        Server.reactor(
                Integer.parseInt(args[1]), // num of threads
                port, //port
                BGRSMessagingProtocol::new, //protocol factory
                BGRSMessageEncoderDecoder::new //message encoder decoder factory
            ).serve();
        System.out.println("finish");
    }
}



