package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;


public class Login implements Command {

    private String username;
    private String password;
    private short opcode = 3;

    public Login(String username, String password){
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
    public short getCommandOpcodeToInform() {  // not relevant for Login command
        return -1;
    }

    @Override
    public String getMessageToPrint() {  // not relevant for Login command
        return null;
    }

    @Override
    public int getCourseNum() { // not relevant for Login command
        return -1;
    }



    /* @Override
    public Command execute(BGRSMessagingProtocol protocol) {
        return null;
    }*/
}
