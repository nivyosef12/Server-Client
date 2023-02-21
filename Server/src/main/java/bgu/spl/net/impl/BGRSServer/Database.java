

package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Course;
import bgu.spl.net.impl.BGRSServer.commands.ERR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

	private final ConcurrentHashMap<String, User> users;
	private final ConcurrentHashMap<Integer, Course> courses;
	private final List<Integer> coursesByOrder;

	//to prevent user from creating new Database
	private Database() {
		users = new ConcurrentHashMap<>();
		courses = new ConcurrentHashMap<>();
		coursesByOrder = new ArrayList<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	private static class SingelHolder {
		private static Database instance = new Database();
	}
	public static Database getInstance() {
		return SingelHolder.instance; //singleton;
	}
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader(coursesFilePath));
			String line = reader.readLine();

			while (line != null){
				String[] courseInfo = line.split("\\|");
				Course course = new Course(courseInfo);
				coursesByOrder.add(Integer.parseInt(courseInfo[0]));
				courses.putIfAbsent(course.getCourseNum(), course);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return false;
	}

	boolean registerUser(String username, String password, boolean isAdmin){
		synchronized (users) {
			if (users.containsKey(username))
				return false;
			User userToReg = new User(username, password);
			users.put(userToReg.getUserName(), userToReg);
			userToReg.setAdmin(isAdmin);
			return true;
		}
	}

	boolean loginUser(String username, String password){
		User userToLogin = getUser(username); // returns null if no such user exists
		if(userToLogin == null || userToLogin.isLoggedIn() || !userToLogin.isCorrectPassword(password))
			return false;
		userToLogin.setLoggedIn(true);
		return true;
	}

	boolean logoutUser(String username){
		User userToLogout = getUser(username); // returns null if no such user exists
		if(userToLogout == null || !userToLogout.isLoggedIn())
			return false;
		userToLogout.setLoggedIn(false);
		return true;
	}

	boolean courseReg(String username, Integer numOfCourse){
		User userToReg = getUser(username); // returns null if no such user exists
		Course courseToRegTo = getCourse(numOfCourse); // returns null if no such course exists

		if(userToReg == null || !userToReg.isLoggedIn() || userToReg.isAdmin() || userToReg.isRegisteredToCourse(numOfCourse)
				|| courseToRegTo == null || !courseToRegTo.hasAvailableSeats()) {
			return false;
		}

		// check if user has all necessary kdam courses
		List<Integer> kdamCourses = courseToRegTo.getKdamCoursesList();
		List<Integer> coursesTakenByStudent = userToReg.getCoursesTakenByThisUser();
		for (Integer kdamCours : kdamCourses) { // student is missing a kdam course
			if (!coursesTakenByStudent.contains(kdamCours))
				return false;
		}

		// register
		if(courseToRegTo.registerStudent(userToReg.getUserName())) {
			userToReg.regToCourse(courseToRegTo.getCourseNum());
			return true;
		}
		return false;
	}

	boolean unRegisterStudentFromCourse(String username, Integer courseNum){
		User userToUnReg = getUser(username); // returns null if no such user exists
		Course courseToUnRegTo = getCourse(courseNum); // returns null if no such course exists

		if(userToUnReg == null || userToUnReg.isAdmin() || !userToUnReg.isLoggedIn() || courseToUnRegTo == null) {
			return false;
		}

		if(courseToUnRegTo.unRegisterStudent(userToUnReg.getUserName())) {
			userToUnReg.unRegToCourse(courseToUnRegTo.getCourseNum());
			return true;
		}
		return false;
	}

	String getCourseStat(String username, Integer courseNum){
		User userToAskCourseStat = getUser(username); // returns null if no such user exists
		Course course = getCourse(courseNum); // returns null if no such course exists

		if(userToAskCourseStat == null || !userToAskCourseStat.isLoggedIn() || !userToAskCourseStat.isAdmin()
				|| course == null)
			return null;

		StringBuilder output = new StringBuilder();

//		output.append("Course: (").append(course.getCourseNum()).append(") ").append(course.getCourseName()).append("|");
//		output.append("Seats Available: ").append(course.getNumOfSeatsAvailable()).append("/").append(course.getNumOfMaxStudent()).append("|");
//		output.append("Students Registered: [");

		output.append(String.format("Course: (%d) %s |", course.getCourseNum(), course.getCourseName()));
		output.append(String.format("Seats Available: %d/%d|", course.getNumOfSeatsAvailable(), course.getNumOfMaxStudent()));
		output.append("Students Registered: [");

		List<String> studentsTakingThisCourse = course.getStudentsTakingThisCourse();
		for(String student : studentsTakingThisCourse)
			output.append(student).append(", ");

		if(output.charAt(output.length() - 1) != '[')
			output.delete(output.length() - 2, output.length());

		output.append("]");
		return output.toString();
	}
	String getKdamCourses(String username, Integer courseNum){
		if(getUser(username) == null || getUser(username).isAdmin() || courses.get(courseNum) == null)
			return null;
		StringBuilder output = new StringBuilder();
		output.append("[");
		List<Integer> kdamCoursesList = courses.get(courseNum).getKdamCoursesList();
		for(int i = 0; i < coursesByOrder.size(); i++){
			if(kdamCoursesList.contains(coursesByOrder.get(i))) { // in the same order as in the courses file
				output.append(coursesByOrder.get(i).toString());
				output.append(",");
			}
		}

		if(output.charAt(output.length() - 1) != '[')
			output.delete(output.length() - 1, output.length());
		output.append("]");

		return output.toString();
	}

	String getStudentStat(String username, String studentUsername){
		User userToAskStudentStat = getUser(username); // returns null if no such user exists
		User student = getUser(studentUsername);  // returns null if no such user exists

		if(userToAskStudentStat == null || !userToAskStudentStat.isLoggedIn() || !userToAskStudentStat.isAdmin()
				|| student == null || student.isAdmin())
			return null;

		StringBuilder output = new StringBuilder();
		output.append("Student: ").append(student.getUserName()).append("|");
		output.append("Courses: [");
		List<Integer> coursesThisStudentIsTaking = student.getCoursesTakenByThisUser();
		for(int i = 0; i < coursesByOrder.size(); i++){
			if(coursesThisStudentIsTaking.contains(coursesByOrder.get(i))){ // in the same order as in the courses file
				output.append(coursesByOrder.get(i).toString());
				output.append(", ");
			}
		}

		if(output.charAt(output.length() - 1) != '[')
			output.delete(output.length() - 2, output.length());
		output.append("]");

		return output.toString();
	}

	String isRegistered(String username, Integer courseNum){
		User user = getUser(username); // returns null if no such user exists
		Course course = getCourse(courseNum); // returns null if no such course exists

		if(course == null || user == null || !user.isLoggedIn() || user.isAdmin())
			return null;

		else if(!course.isStudentTakingThisCourse(username))
			return "NOT REGISTERED";

		return "REGISTERED";
	}
	String myCourses(String username){
		User userToCheckCourses = getUser(username); // returns null if no such user exists

		if(userToCheckCourses == null || !userToCheckCourses.isLoggedIn() || userToCheckCourses.isAdmin()) {
			return null;
		}

		StringBuilder output = new StringBuilder("[");
		List<Integer> coursesThisStudentIsTaking = userToCheckCourses.getCoursesTakenByThisUser();
		for(Integer courseNum : coursesThisStudentIsTaking)
			output.append(courseNum).append(",");

		if(output.charAt(output.length() - 1) != '[')
			output.deleteCharAt(output.length() - 1);
		output.append("]");

		return output.toString();
	}

	private User getUser(String username){
		if(username == null)
			return null;
		return users.get(username); // returns null if no such user exists
	}

	private Course getCourse(Integer courseNum){
		return courses.get(courseNum); // returns null if no such course exists
	}
}
