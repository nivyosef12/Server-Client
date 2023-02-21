package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;


public class StudentReg implements Command {

    private String username;
    private String password;
    private short opcode = 2;

    public StudentReg(String username, String password){
        this.username = username;
        this.password = password;
    }
    public short getOpcode(){
        return opcode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getMessageToPrint() {  // not relevant for StudentReg command
        return null;
    }

    @Override
    public short getCommandOpcodeToInform() {  // not relevant for StudentReg command
        return -1;
    }

    @Override
    public int getCourseNum() {  // not relevant for StudentReg command
        return -1;
    }

   /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
