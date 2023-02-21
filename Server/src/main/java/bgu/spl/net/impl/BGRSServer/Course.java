package bgu.spl.net.impl.BGRSServer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Course {

    private final int courseNum;
    private final String courseName;
    private final List<Integer> kdamCoursesList;
    private final List<String> studentsTakingThisCourse;
    private final int numOfMaxStudent;
    private int numOfSeatsAvailable;
    private final Object LockNumOfSeatsAvailable_studentsTakingThisCourse = new Object();

    public Course(String[] courseInfo){
        courseNum = Integer.parseInt(courseInfo[0]);
        courseName = courseInfo[1];
        kdamCoursesList = new LinkedList<>();
        StringBuilder currNumAsString = new StringBuilder();
        for(int i = 0; i < courseInfo[2].length(); i++){
            char currChar = courseInfo[2].charAt(i);
            if(currChar != '[' && currChar != ',' && currChar != '|' && currChar != ']')
                currNumAsString.append(currChar);
            else if(currChar == ',' || currChar == ']'){
                if(!currNumAsString.toString().isEmpty()) {
                    kdamCoursesList.add(Integer.parseInt(String.valueOf(currNumAsString)));
                    currNumAsString.delete(0, currNumAsString.length());
                }
            }
        }

        studentsTakingThisCourse = new LinkedList<>();
        numOfMaxStudent = Integer.parseInt(courseInfo[3]);
        numOfSeatsAvailable = numOfMaxStudent;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<Integer> getKdamCoursesList() {
        for(int i =0;i<kdamCoursesList.size();i++)
            System.out.print(kdamCoursesList.get(i) + " , ");
        return kdamCoursesList;
    }

    public List<String> getStudentsTakingThisCourse() {
        Comparator<String> stringComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int j = Math.min(s1.length(), s2.length());
                for(int i = 0; i < j; i++){
                    if(s1.charAt(i) != s2.charAt(i))
                        return s1.charAt(i) - s2.charAt(i);
                }
                return s1.length() - s2.length();
            }
        };
        synchronized (LockNumOfSeatsAvailable_studentsTakingThisCourse) {
            studentsTakingThisCourse.sort(stringComparator);
            return studentsTakingThisCourse;
        }
    }

    public int getNumOfMaxStudent() {
        return numOfMaxStudent;
    }

    public int getNumOfSeatsAvailable() {
        return numOfSeatsAvailable;
    }

    public boolean isStudentTakingThisCourse(String username){
        synchronized (LockNumOfSeatsAvailable_studentsTakingThisCourse) {
            return studentsTakingThisCourse.contains(username);
        }
    }

    public synchronized boolean hasAvailableSeats(){
        return numOfSeatsAvailable > 0;
    }

    public boolean registerStudent(String username){
        synchronized (LockNumOfSeatsAvailable_studentsTakingThisCourse) {
            if (hasAvailableSeats()) {
                studentsTakingThisCourse.add(username);
                numOfSeatsAvailable -= 1;
                return true;
            }
            return false;
        }
    }

    public boolean unRegisterStudent(String username){
        synchronized (LockNumOfSeatsAvailable_studentsTakingThisCourse) {
            boolean isRemoved = studentsTakingThisCourse.remove(username);
            if (isRemoved)
                numOfSeatsAvailable += 1;
            return isRemoved;
        }
    }
}
