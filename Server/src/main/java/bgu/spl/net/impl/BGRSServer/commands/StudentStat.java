package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;

public class StudentStat implements Command {

    private short opcode = 8;
    private String username;

    public StudentStat(String username){
        this.username = username;
    }
    public short getOpcode(){
        return opcode;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public short getCommandOpcodeToInform() {  // not relevant for StudentStat command
        return -1;
    }

    @Override
    public String getPassword() {  // not relevant for StudentStat command
        return null;
    }

    @Override
    public String getMessageToPrint() {  // not relevant for StudentStat command
        return null;
    }

    @Override
    public int getCourseNum() {  // not relevant for StudentStat command
        return -1;
    }

  /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
