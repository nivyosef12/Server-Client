package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;

public class IsRegistered implements Command {

    private short opcode = 9;
    private int courseNum;

    public IsRegistered(int courseNum){
        this.courseNum = courseNum;
    }

    public short getOpcode(){
        return opcode;
    }

    public int getCourseNum() {
        return courseNum;
    }

    @Override
    public String getUsername() {  // not relevant for IsRegistered command
        return null;
    }

    @Override
    public short getCommandOpcodeToInform() {  // not relevant for IsRegistered command
        return -1;
    }

    @Override
    public String getPassword() {  // not relevant for IsRegistered command
        return null;
    }

    @Override
    public String getMessageToPrint() {  // not relevant for IsRegistered command
        return null;
    }

   /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
