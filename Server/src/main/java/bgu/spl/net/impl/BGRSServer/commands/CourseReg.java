package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;


public class CourseReg implements Command {

    private int courseNum;
    private short opcode = 5;

    public CourseReg(int courseNum){
        this.courseNum = courseNum;
    }

    public short getOpcode(){
        return opcode;
    }

    public int getCourseNum() {
        return courseNum;
    }

    @Override
    public short getCommandOpcodeToInform() {  // not relevant for CourseReg command
        return -1;
    }

    @Override
    public String getUsername() { // not relevant for CourseReg command
        return null;
    }

    @Override
    public String getPassword() { // not relevant for CourseReg command
        return null;
    }

    @Override
    public String getMessageToPrint() { // not relevant for CourseReg command
        return null;
    }


   /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
