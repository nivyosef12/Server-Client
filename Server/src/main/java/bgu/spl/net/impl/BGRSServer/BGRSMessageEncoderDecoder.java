package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.impl.BGRSServer.commands.*;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.Command;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGRSMessageEncoderDecoder implements MessageEncoderDecoder<Command> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k (10 shl)
    private int len = 0;
    private short opcode = -1;
    private String username = null;
    private String password = null;
    private short courseNumber = -1; // a valid course number is short >= 0

    @Override
    public byte[] encode(Command command) {
        opcode = command.getOpcode();
        byte[] output = shortToBytes(opcode); // encoding opcode
        byte[] byteArray;
        switch (opcode) {
            //cases 1-3 requires both username and password
            case 1:
            case 2:
            case 3:
                byteArray = command.getUsername().getBytes(StandardCharsets.UTF_8); // encoding username
                output = append(output, byteArray); //appending encoded opcode and encoded username
                output = append(output, shortToBytes((short) 0)); // encoding short 0 to bytes and appending it to output
                byteArray = command.getPassword().getBytes(StandardCharsets.UTF_8);  // encoding password
                output = append(output, byteArray);
                output = append(output, shortToBytes((short) 0));
                resetFields();
                return output;

            case 4: // logout require nothing
            case 11: // myCourses message also requires nothing
                resetFields();
                return output; // returning only the encoded opcode

            // cases 5-7, 9-10 requires course number
            // messages in these cases are 2 bytes of opcode and 2 bytes of course number
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
                short courseNum = (short) command.getCourseNum();
                output = append(output, shortToBytes(courseNum)); //appending encoded opcode and encoded course number
                resetFields();
                return output;

            // case 8 requires username
            case 8:
                byteArray = command.getUsername().getBytes(StandardCharsets.UTF_8);
                output = append(output, byteArray); //appending encoded opcode and encoded username
                output = append(output, shortToBytes((short) 0));
                resetFields();
                return output;

            case 12:
                short getCommandOpcodeToInform = command.getCommandOpcodeToInform();
                output = append(output, shortToBytes(getCommandOpcodeToInform)); //appending encoded opcode and encoded message opcode
                if(!command.getMessageToPrint().equals("")) {
                    byteArray = command.getMessageToPrint().getBytes(StandardCharsets.UTF_8);
                    output = append(output, byteArray); //appending output and encoded string to print
                }
                output = append(output, shortToBytes((short) 0));
                resetFields();
                return output;

            // messages in case 13 are 2 bytes of opcode and 2 bytes of message opcode number
            case 13:
                getCommandOpcodeToInform = command.getCommandOpcodeToInform();
                output = append(output, shortToBytes(getCommandOpcodeToInform)); //appending encoded opcode and encoded message opcode
                byte[] z = {output[2], output[3]};
                resetFields();
                return output;
        }
        return null;
    }

    @Override
    public Command decodeNextByte(byte nextByte) {
        // start decoding opcode
        if(opcode == -1){
            bytes[len++] = nextByte;
            if (len == 2) { // opcode decoded
                opcode = bytesToShort(Arrays.copyOfRange(bytes,0,len));
                if (opcode == 4 || opcode == 11){
                    Command commandToBeReturned = null;
                    commandToBeReturned = getCommendFromOpcode();
                    resetFields();
                    return commandToBeReturned;
                }
                len = 0;
            }
            return null; // didnt finish decoding
        }
        else{
            Command commandToBeReturned = null;
            if(isSuccessfullyDecoded(nextByte)){
                commandToBeReturned = getCommendFromOpcode();
                resetFields();
            }
            return commandToBeReturned;
        }
    }

    private boolean isSuccessfullyDecoded(byte nextByte){
        switch (opcode){
            //cases 1-3 requires both username and password
            case 1:
            case 2:
            case 3:
                if(username == null)
                    username = decodeToString(nextByte);
                else if(password == null)
                    password = decodeToString(nextByte);
                return username != null && password != null;

            case 4: // logout require nothing
            case 11: // myCourses message also requires nothing
                return true;

            // cases 5-7, 9-10 requires course number
            // messages in these cases are 2 bytes of opcode and 2 bytes of course number
            // when entering one of those cases, opcode has been decoded already
            // so there are only 2 other bytes to decode, when len==2 --> we can convert those bytes to course num
            // in case 13 courseNumber refers to message opcode number
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 13:
                if(courseNumber == -1){
                    if(len == 0)
                        bytes = new byte[1 << 10]; //start with 1k
                    else if(len == 1){
                        pushByte(nextByte);
                        courseNumber = bytesToShort(Arrays.copyOfRange(bytes, 0, len));
                        return true;
                    }
                    pushByte(nextByte);
                    return false;
                }
            case 8: //case 8 requires username
                if(username == null)
                    username = decodeToString(nextByte);
                return username != null;

            // in case 12 courseNumber refers to message opcode number
            case 12:
                if(courseNumber == -1){
                    if(len == 1)
                        courseNumber = bytesToShort(Arrays.copyOfRange(bytes, 0, 2));
                    pushByte(nextByte);
                }
                else if(username == null) // in case 12 username refers to the string to be printed
                    username = decodeToString(nextByte);
                return courseNumber != -1 && username != null;
        }
        return false;
    }
    private Command getCommendFromOpcode(){
        // when calling getCommendFromOpcode(), we can be sure that "talk about username blablabla"!!!!!!!!!!!!!!!!!!!!!!!!!!!
        switch (opcode){
            case 1:
                return new AdminReg(username, password);
            case 2:
                return new StudentReg(username, password);
            case 3:
                return new Login(username, password);
            case 4:
                return new Logout();
            case 5:
                return new CourseReg(courseNumber);
            case 6:
                return new KdamCheck(courseNumber);
            case 7:
                return new CourseStat(courseNumber);
            case 8:
                return new StudentStat(username);
            case 9:
                return new IsRegistered(courseNumber);
            case 10:
                return new Unregister(courseNumber);
            case 11:
                return new MyCourses();
            case 12:
                return new ACK((short)courseNumber, username);
            case 13:
                return new ERR((short)courseNumber);
        }
        return null;
    }
    private String decodeToString(byte nextByte){
        if(nextByte != '\0'){
            pushByte(nextByte);
            return null;
        }
        else {
            String decodedString = new String(bytes, 0, len, StandardCharsets.UTF_8);
            len = 0;
            return decodedString;
        }
    }
    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private byte[] append(byte[] arr1, byte[] arr2){
        byte[] output = new byte[arr1.length + arr2.length];
        for(int i = 0; i < arr1.length; i++)
            output[i] = arr1[i];
        for(int i = arr1.length; i < output.length; i++)
            output[i] = arr2[i - arr1.length];
        return output;
    }
    private void resetFields(){
        bytes = new byte[1 << 10]; //start with 1k (10 shl)
        len = 0;
        opcode = -1;
        username = null;
        password = null;
        courseNumber = -1;
    }
}
