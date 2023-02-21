package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;

public class ERR implements Command {

    private short opcode = 13;
    private short commandOpcodeToInform;

    public ERR(short commandOpcodeToInform){
        this.commandOpcodeToInform = commandOpcodeToInform;
    }

    public short getOpcode(){
        return opcode;
    }

    @Override
    public short getCommandOpcodeToInform() {
        return commandOpcodeToInform;
    }
    @Override
    public String getUsername() { // not relevant for ERR command
        return null;
    }

    @Override
    public String getPassword() { // not relevant for ERR command
        return null;
    }

    @Override
    public String getMessageToPrint() { // not relevant for ERR command
        return null;
    }

    @Override
    public int getCourseNum() { // not relevant for ERR command
        return -1;
    }

    /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
