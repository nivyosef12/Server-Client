package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRSServer.commands.ACK;
import bgu.spl.net.impl.BGRSServer.commands.ERR;

import java.util.List;

public class BGRSMessagingProtocol implements MessagingProtocol<Command> {
    private String username = null;
    private boolean terminate = false;
    private final Database database = Database.getInstance();

    @Override
    public Command process(Command command) {
        switch (command.getOpcode()){
            case 1: // AdminReg
                if(username != null || !database.registerUser(command.getUsername(), command.getPassword(), true))
                    return new ERR((short)1);
                return new ACK((short)1, "");

            case 2: // StudentReg
                if(username != null || !database.registerUser(command.getUsername(), command.getPassword(), false))
                    return new ERR((short)2);
                return new ACK((short)2, "");

            case 3: // login
                if(username != null || !database.loginUser(command.getUsername(), command.getPassword()))
                    return new ERR((short) 3);
                username = command.getUsername();
                return new ACK((short) 3, "");

            case 4: // Logout
                if(!database.logoutUser(username))
                    return new ERR((short)4);
                username = null; // user wont be able to request actions
                terminate = true;
                return new ACK((short) 4, "");

            case 5: // CourseReg
                if(!database.courseReg(username, command.getCourseNum()))
                    return new ERR((short) 5);
                return new ACK((short)5, "");

            case 6: // KdamCheck
                String kdamCourses = database.getKdamCourses(username, command.getCourseNum());
                if (kdamCourses == null)
                    return new ERR((short) 6);
                return new ACK((short)6, kdamCourses);

            case 7: // CourseStat
                String courseStat = database.getCourseStat(username, command.getCourseNum());
                if(courseStat == null)
                    return new ERR((short)7);
                return new ACK((short)7, courseStat);

            case 8: // StudentStat
                String studentStat = database.getStudentStat(username, command.getUsername());
                if(studentStat == null)
                    return new ERR((short)8);
                return new ACK((short)8, studentStat);

            case 9: // IsRegistered
                String isRegistered = database.isRegistered(username, command.getCourseNum());
                if(isRegistered == null)
                    return new ERR((short)9);
                return new ACK((short)9, isRegistered);

            case 10: // Unregister
                if(!database.unRegisterStudentFromCourse(username, command.getCourseNum()))
                    return new ERR((short)10);
                return new ACK((short)10, "");
            case 11: // MyCourses
                String studentsCourses = database.myCourses(username);
                if(studentsCourses == null)
                    return new ERR((short)11);
                return new ACK((short)11, studentsCourses);
            case 12:
            case 13:
                return new ERR((short)13);

        }
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return terminate;
    }
}
