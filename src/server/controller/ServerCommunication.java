package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import server.model.Course;
import server.model.CourseCatalogue;
import server.model.CourseOffering;
import server.model.Registration;
import server.model.Student;

/**
 * Communicates with Server
 *
 */
public class ServerCommunication {
	private Socket aSocket;
	private ServerSocket serverSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	public Student theStudent = new Student(); //later, this should be the database I think?
	private DBController dataBase;
	private CourseCatalogue theCatalogue;
	public ServerCommunication(){
		try {
			serverSocket = new ServerSocket(1010);
			aSocket = serverSocket.accept();
			System.out.println("Client connected");
			out = new ObjectOutputStream(aSocket.getOutputStream());
			in = new ObjectInputStream(aSocket.getInputStream());
			
			theCatalogue = new CourseCatalogue();
			dataBase = new DBController(theCatalogue);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * First reads the code, then depending on the code it will call another method or exit
	 */
	public void communicateWithClient() {
		Integer code = 0;
		while(true) {
			try {
				code = (Integer)in.readObject(); //reads the code
				if(code.equals(1)) { //search for course
					String courseName = (String)in.readObject();
					String courseNum = (String)in.readObject();
					searchForCourse(courseName,courseNum);
				}else if(code.equals(2)) { //add course
					String studentId = (String)in.readObject();
					String courseName = (String)in.readObject();
					String courseNum = (String)in.readObject();
					String sectionNum = (String)in.readObject();
					addCourse(studentId,courseName,courseNum,sectionNum);
				}else if(code.equals(3)) { //remove course
					String studentId = (String)in.readObject();
					String courseName = (String)in.readObject();
					String courseNum = (String)in.readObject();
					String sectionNum = (String)in.readObject();
					removeCourse(studentId,courseName,courseNum,sectionNum);
				}else if(code.equals(4)) { //display catalogue
					displayAllCourses();
				}else if(code.equals(5)) { //display student's courses
					String studentId = (String)in.readObject();
					viewAllCoursesTakenByStudent(studentId);
				}else if(code.equals(6)) { //exit
					out.writeObject("Bye");
					break;
				}else {
                    out.writeObject("Error. Program terminating.");
                    break;
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
	
	private void viewAllCoursesTakenByStudent(String studentId) {
		try {	
			Integer id = Integer.parseInt(studentId);
			
			//search through database for that student
			Student studentObj = findStudent(id);
			if(studentObj == null) {
				out.writeObject("Id not valid");
				return;
			}
			if(studentObj.getRegList().size()==0)
				out.writeObject(studentObj.getName() + " is not registered in anything.");
			else
				out.writeObject(studentObj.getName() + " is registered in: \n"+studentObj.printRegList());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void displayAllCourses() {
		try {
			String output = theCatalogue.displayAllCourses();
			out.writeObject(output);
		} catch (IOException e) {
			System.err.println("error in displaying all courses");
			e.printStackTrace();
		}
	}

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
			
			CourseOffering theOffering = findOffering(sectionNum, theCourse);
			if(theOffering == null) {
				out.writeObject("Offering does not exist");
				return;
			}
			
			Registration regObj = null;
			for(Registration r: theOffering.getRegList()) {
				if(r.getStudent() == studentObj && r.getOffering() == theOffering) {
					regObj = r;
					break;
				}
			}
			if(regObj == null) {
				out.writeObject("Registration does not exist.");
				return;
			}
			regObj.removeRegistration();
			out.writeObject("Course was successfully removed");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void searchForCourse(String name, String num) {
		System.out.println("hi");
		try {
			Integer courseNum = Integer.parseInt(num);
			name = name.toUpperCase();
			String output = theCatalogue.searchForCourse(courseNum);
			
			System.out.println(output);
			if(output == null) {
				out.writeObject("Course was not found.");
			}else {
				out.writeObject(output);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void addCourse(String studentId, String courseName, String num, String secNum) {
		try {
			Integer id =Integer.parseInt(studentId);
			Integer courseNum = Integer.parseInt(num);
			Integer sectionNum = Integer.parseInt(secNum);
			courseName = courseName.toUpperCase();
			
			//verify the student is in the database
			Student studentObj = findStudent(id);
			if(studentObj == null) {
				out.writeObject("Id not valid");
				return;
			}
			
			//find location ofStudentObj in database
			int index = findStudentLocation(studentObj);
			
			//find the course to register the student in
			Course theCourse = findCourse(courseNum, courseName);
			if(theCourse == null) {
				out.writeObject("Course does not exist");
				return;
			}
			
			//find the offering to register the student in
			CourseOffering theOffering = findOffering(sectionNum, theCourse);
			if(theOffering == null) {
				out.writeObject("Offering does not exist");
				return;
			}
			Registration regObj = new Registration(dataBase.getStudentList().get(index), theOffering);
			//add this object to both the student and the offering's regList
			if(regObj.addRegistration()) {
				out.writeObject("Registration complete. " + studentObj.getName() + " is enrolled in " + theCourse.getCourseName() 
				+ " "+theCourse.getCourseNum()+" section " +sectionNum);
			}else {
				out.writeObject("Regstration failed. Student enrolled in max of 6 courses");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
	}
	
	private int findStudentLocation(Student studentObj) {
		int i = 0;
		for(Student s: dataBase.getStudentList()) {
			if(studentObj.getId() == s.getId()) {
				break;
			}
			i++;
		}
		return i;
	}

	private CourseOffering findOffering(Integer sectionNum, Course theCourse) {
		CourseOffering theOffering = null;
		for(CourseOffering o: theCourse.getOfferingList()) {
			if(o.getSecNum() == sectionNum) {
				theOffering = o;
				break;
			}
		}
		return theOffering;
	}

	private Course findCourse(Integer courseNum, String courseName) {
		Course theCourse = null;
		for(Course c: dataBase.getCourseList()) {
			if(c.getCourseName().equals(courseName) && c.getCourseNum()==courseNum) {
				theCourse = c;
				break;
			}
		}
		return theCourse;
	}

	private Student findStudent(int id) {
		Student studentObj = null;
		for(Student s: dataBase.getStudentList()) {
			if(s.getId() == id) {
				studentObj = s;
				break;
			}
		}
		
		return studentObj;
		
	}
	

	public static void main (String [] args) {
		ServerCommunication sv = new ServerCommunication();
		sv.communicateWithClient();
	}
}


