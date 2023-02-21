package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;

public interface Command {
    public short getOpcode();
    public short getCommandOpcodeToInform();
    public String getUsername();
    public String getPassword();
    public String getMessageToPrint();
    public int getCourseNum();
}
