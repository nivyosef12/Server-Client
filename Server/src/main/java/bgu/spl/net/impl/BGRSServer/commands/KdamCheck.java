package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;


public class KdamCheck implements Command {

    private int courseNum;
    private short opcode = 6;

    public KdamCheck(int courseNum){
        this.courseNum = courseNum;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public short getOpcode(){
        return opcode;
    }

    @Override
    public String getUsername() {  // not relevant for KdamCheck command
        return null;
    }

    @Override
    public short getCommandOpcodeToInform() {   // not relevant for KdamCheck command
        return -1;
    }

    @Override
    public String getPassword() {  // not relevant for KdamCheck command
        return null;
    }

    @Override
    public String getMessageToPrint() {  // not relevant for KdamCheck command
        return null;
    }

   /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
