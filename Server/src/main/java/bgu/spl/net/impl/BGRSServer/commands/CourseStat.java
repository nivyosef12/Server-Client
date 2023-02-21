package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;


public class CourseStat implements Command {

    private int courseNum;
    private short opcode = 7;

    public CourseStat(int courseNum){
        this.courseNum = courseNum;
    }
    public short getOpcode(){
        return opcode;
    }

    public int getCourseNum() {
        return courseNum;
    }

    @Override
    public String getUsername() { // not relevant for CourseStat command
        return null;
    }

    @Override
    public String getPassword() { // not relevant for CourseStat command
        return null;
    }
    @Override
    public short getCommandOpcodeToInform() { // not relevant for CourseStat command
        return -1;
    }

    @Override
    public String getMessageToPrint() { // not relevant for CourseStat command
        return null;
    }


   /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
