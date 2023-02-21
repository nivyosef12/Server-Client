package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class User {

    private final String userName;
    private final String password;
    private boolean isLoggedIn;
    private boolean isAdmin;
    private final List<Integer> coursesTakenByThisUser;

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
        isLoggedIn = false;
        isAdmin = false; // will be updated according to the login request
        coursesTakenByThisUser = new LinkedList<>();
    }

    public List<Integer> getCoursesTakenByThisUser() {
        return coursesTakenByThisUser;
    }

    public boolean isRegisteredToCourse(int courseNum){
        return coursesTakenByThisUser.contains(courseNum);
    }

    public void regToCourse(Integer courseNum){
        coursesTakenByThisUser.add(courseNum);

    }
    public void unRegToCourse(Integer courseNum){
        coursesTakenByThisUser.remove(courseNum);
    }

    public String getUserName() {
        return userName;
    }

    public boolean isCorrectPassword(String password){
        return this.password.equals(password);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
