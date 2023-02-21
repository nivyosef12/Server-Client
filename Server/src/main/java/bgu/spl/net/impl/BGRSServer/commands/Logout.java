package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;


public class Logout implements Command {

    private short opcode = 4;

    public short getOpcode(){
        return opcode;
    }

    @Override
    public String getUsername() { // not relevant for Logout command
        return null;
    }

    @Override
    public short getCommandOpcodeToInform() { // not relevant for Logout command
        return -1;
    }

    @Override
    public String getPassword() { // not relevant for Logout command
        return null;
    }

    @Override
    public String getMessageToPrint() { // not relevant for Logout command
        return null;
    }

    @Override
    public int getCourseNum() { // not relevant for Logout command
        return -1;
    }

  /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
