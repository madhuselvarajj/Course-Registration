package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import server.model.Course;
import server.model.CourseCatalogue;
import server.model.CourseOffering;
import server.model.Registration;
import server.model.Student;

/**
 * The server class that communicates with the client
 *  @author Madhu Selvaraj, Navjot Singh
 */
public class ServerCommunication implements Runnable{
	
	/**
	 * The socket that will serve as the connection between the client and server
	 */
	private Socket aSocket;
	
	/**
	 * serverSocket accepts connections and builds sockets 
	 */
	private ServerSocket serverSocket;
	
	/**
	 * The output stream, used to send information to the client
	 */
	private ObjectOutputStream out;
	
	/**
	 * The input stream, used to read information from the client
	 */
	private ObjectInputStream in;
	
	/**
	 * Connection to the database
	 */
	private DBController dataBase;
	
	/**
	 * Instance of CourseCatalogue that will be sent to the database and initialized 
	 */
	private CourseCatalogue theCatalogue;
    
    /**
     *this is the default constructor which will create a new serverSocket with port num 1010.
     *It will instantiate the input and output streams as well as the relevant sockets and
     *let the user know when a connection has been achieved.
     */
	public ServerCommunication(Socket aSocket, DBController database){
		try {
//			serverSocket = new ServerSocket(1011);
			this.aSocket = aSocket;
			System.out.println("Client connected");
			out = new ObjectOutputStream(aSocket.getOutputStream());
			in = new ObjectInputStream(aSocket.getInputStream());
			this.dataBase = database;
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the method that communicates with the client
	 * First reads the code, then depending on the code it will call another method or exit
	 */
	public void communicateWithClient() {
		Integer code = 0;
		while(true) {
			try {
				Object isCancelled = null;
				code = (Integer)in.readObject(); //reads the code
				
				if(code == 7) { //need this in case cancel is pressed after the OK button was already pressed 
					out.writeObject("Cancelled");
				}
				else {
					if(code!=4 && code!=6) { //if code == 4 or 6, then user did not have the option to press cancel
						//read the next entry in the socket...might be 7 if user cancelled, or might just be user input
						isCancelled = in.readObject(); 
					}
					
					if(code==4){ //code 4: display entire catalog
						displayAllCourses();
					}
					
					else if(code == 6) { //code 6: terminate program because user wants to exit
						out.writeObject("Bye");
						break;
					}
					
					else { //if code is 1,2,3,5 ...that means isCancelled contains the next value in the socket
						
						//if isCancelled is an integer and if isCancelled == 7, that means the user canceled
						if (isCancelled instanceof Integer && (Integer)isCancelled == 7) { 
								out.writeObject("Cancelled");
						}else {
							//if isCanceled is not an integer, or if isCancelled is != 7...that means that user entered in their input and did not cancel
							//so store the object isCancelled as the next input, and continue normally 
							String nextInput = (String)isCancelled;
							
							if(code.equals(1)) { //code 1: search for a course
								String courseName = nextInput;
								String courseNum = (String)in.readObject();
								searchForCourse(courseName,courseNum);
							}
							else if(code.equals(2)) { //code 2: add a course to student's courses 
								String studentId = nextInput;
								String courseName = (String)in.readObject();
								String courseNum = (String)in.readObject();
								String sectionNum = (String)in.readObject();
								addCourse(studentId,courseName,courseNum,sectionNum);
							}
							else if(code.equals(3)) { //code 3: remove a course from student's courses
								String studentId = nextInput;
								String courseName = (String)in.readObject();
								String courseNum = (String)in.readObject();
								String sectionNum = (String)in.readObject();
								removeCourse(studentId,courseName,courseNum,sectionNum);
							}
							else if(code.equals(5)) { //code 5: display student's courses
								String studentId = nextInput;
								viewAllCoursesTakenByStudent(studentId);
							}
							else {
			                    out.writeObject("Error. Program terminating.");
			                    break;
			                }
						}
					}
				}
				
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
     *Code 1
     *this will write to the socket if the course with the specified name and number is available
     *in the course offering list.
     *@param name : the name of the course
     *@param num: the number of the course to be searched for
     */
	private void searchForCourse(String name, String num) {
		try {
			Integer courseNum = Integer.parseInt(num);
			name = name.toUpperCase();
			Course theCourse = dataBase.findCourse (courseNum);
			if(theCourse == null) {
				out.writeObject("Course was not found.");
			}else {
//				out.writeObject(output);
				String output = theCourse.getCourseName() + " "+ theCourse.getCourseNum() + "\nNumber enrolled: " +theCourse.getNumEnrolled()
				+"\nStatus: " +theCourse.getStatus();
//				System.out.println(output);
				out.writeObject(output);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 *Code 2
     *this will add a course to a student's listing with the specified parameters
     *@param studentId : the student's ID for the student looking to do the action
     *@param courseName : the name of the course to remove
     *@param num : the number of the course to remove
     *@param secNum : the section of the course to remove.
     */
	private void addCourse(String studentId, String courseName, String num, String secNum) {
		try {
			Integer id =Integer.parseInt(studentId);
			Integer courseNum = Integer.parseInt(num);
			Integer sectionNum = Integer.parseInt(secNum);
			courseName = courseName.toUpperCase();
			
			dataBase.enrollInCourse (id, courseNum, sectionNum);
			out.writeObject("course added"); //fix to show which course was added...
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     *Code 3
     *this function will remove a course from a student's course listing
     *@param studentId : the ID of the student hoping to remove a course
     *@param courseName : the name of the course the student would like to remove
     *@param cNum : the number of the course the student would like to remove
     *@param secNum : the section of the course the student is looking to remove.
     */
	private void removeCourse(String studentId, String courseName, String cNum, String secNum) {
		try {
			Integer id = Integer.parseInt(studentId);
			Integer courseNum = Integer.parseInt(cNum);
			Integer sectionNum = Integer.parseInt(secNum);		
			courseName = courseName.toUpperCase();
			
			//verify student is in the database
			Student studentObj = findStudent(id);
			if(studentObj == null) {
				out.writeObject("Id not valid");
				return;
			}
			
			Course theCourse = findCourse(courseNum, courseName);
			if(theCourse == null) {
				out.writeObject("Course does not exist");
				return;
			}
			
			dataBase.unenrollInCourse(id, courseNum);
			out.writeObject("course removed"); //fix this to show which course was removed...
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 *Code 4
     *this function will send a String with all of the courses available in the course catalogue
     *to the client's socket.
     */
	private void displayAllCourses() {
		try {
			String output = dataBase.viewAllCourses();
			out.writeObject(output);
		} catch (IOException e) {
			System.err.println("error in displaying all courses");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     *Code 5
     *this function will send a String with all of a student's courses to the socket
     *@param studentID the ID of the student who is looking to see all of their course listings.
     */
	private void viewAllCoursesTakenByStudent(String studentId) {
		try {	
			Integer id = Integer.parseInt(studentId); 
			String output = dataBase.viewAllEnrolled(id);
			out.writeObject (output);
			//search through database for that student
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	

	
    /**
     *findCourse will return the Course object with the specified courseNum and name
     *@param courseNum : the course number to search
     *@param courseName : the course name to search
     *@return a course object with the name and number, else null if non existent.
     */
     
	private Course findCourse(Integer courseNum, String courseName) {
		Course theCourse = null;
		theCourse = dataBase.findCourse(courseNum);
		return theCourse;
	}
	
    /**
     *returns a Student object with the specified ID, else will return null if not found.
     *@return a Student object with the specified ID (from the database)
     *@param id : the ID of the student to search for. 
     */
	private Student findStudent(int id) {
		Student studentObj = null;
		studentObj = dataBase.findStudent(id);
		return studentObj;
		
	}
	
	
	@Override
	public void run() {
		communicateWithClient();
		
	}

//	public static void main (String [] args) {
//		ServerCommunication sv = new ServerCommunication();
//		sv.communicateWithClient();
//	}
}