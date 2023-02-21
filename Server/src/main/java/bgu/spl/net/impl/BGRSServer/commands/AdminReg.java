package bgu.spl.net.impl.BGRSServer.commands;
import bgu.spl.net.impl.BGRSServer.Command;

public class AdminReg implements Command {

    private String username;
    private String password;
    private final short opcode = 1;

    public AdminReg(String username, String password){
        if(username == null || password == null)
            throw new NullPointerException();
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
    public short getCommandOpcodeToInform() { // not relevant for AdminReg command
        return -1;
    }

    @Override
    public String getMessageToPrint() { // not relevant for AdminReg command
        return null;
    }

    @Override
    public int getCourseNum() { // not relevant for AdminReg command
        return -1;
    }

    /*@Override
    public Command execute(BGRSMessagingProtocol protocol) {
        User user = new User(username, password);
        user.setAdmin(true);
        if(protocol.addUser(user))
            return null; // return ACK command!!!!!!
        return null; //return ERR command!!!!
    }*/
}
