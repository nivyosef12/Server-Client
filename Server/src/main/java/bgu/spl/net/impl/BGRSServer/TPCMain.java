package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        int port= Integer.parseInt(args[0]);
        Database database = Database.getInstance();
        database.initialize("/home/spl211/Desktop/206100968#_206103863/Server/Courses.txt");
            Server.threadPerClient(
                    port, //port
                    BGRSMessagingProtocol::new, //protocol factory
                    BGRSMessageEncoderDecoder::new //message encoder decoder factory
            ).serve();
    }
}
