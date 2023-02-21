package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;

public class ACK implements Command {
    private final short opcode = 12;
    private short commandOpcodeToInform;
    private String stringToPrint;

    public ACK(short commandOpcodeToInform, String stringToPrint){
        this.commandOpcodeToInform = commandOpcodeToInform;
        this.stringToPrint = stringToPrint;
    }
    public short getOpcode(){
        return opcode;
    }

    @Override
    public short getCommandOpcodeToInform() {
        return commandOpcodeToInform;
    }
    @Override
    public String getMessageToPrint() {
        return stringToPrint;
    }

    @Override
    public String getUsername() { // not relevant for ACK command
        return null;
    }

    @Override
    public String getPassword() { // not relevant for ACK command
        return null;
    }

    @Override
    public int getCourseNum() { // not relevant for ACK command
        return -1;
    }

   /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
