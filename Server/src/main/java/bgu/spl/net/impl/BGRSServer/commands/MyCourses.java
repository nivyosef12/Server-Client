package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;


public class MyCourses implements Command {

    private short opcode = 11;

    public short getOpcode(){
        return opcode;
    }

    @Override
    public String getUsername() { // not relevant for MyCourses command
        return null;
    }

    @Override
    public short getCommandOpcodeToInform() { // not relevant for MyCourses command
        return -1;
    }

    @Override
    public String getPassword() { // not relevant for MyCourses command
        return null;
    }

    @Override
    public String getMessageToPrint() { // not relevant for MyCourses command
        return null;
    }

    @Override
    public int getCourseNum() {  // not relevant for MyCourses command
        return -1;
    }

  /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
