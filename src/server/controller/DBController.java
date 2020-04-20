package server.controller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.model.*;
/**
 * Represents the database of the program. Stores all of the courses that are available to register in. Also stores the list of students. 
 * @author Madhu Selvaraj, Navjot Singh
 * 
 * should ideally have 3 tables? 
*course will contain all of the courses offered  - therefore it has a composite key
*composed of the courseNumber.
*
* 
*student will contain all of the students and their information
*
*admin table will have a list of studentID and courseID's which are linked (meaning that student is taking
*that course)- note that course-student will therefore have 2 primary keys (studentID and courseID) called a
*composite key. 
 *
 */
public class DBController {
	private Connection conn;
	private Statement theStatement;
	private ResultSet rs;
	/**
	 * the purpose of this is to connect to our course registration database created in mySQL. 
	 */
	public void startConnection () {
		String theQuery = new String ("");
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			//Driver theDriver = new oracle.jdbc.OracleDriver();
			//DriverManager.registerDriver(theDriver);
			Class.forName(driver);
			//using localHost here
			String url = "jdbc:oracle:thin:@root:3306/courseReg";
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseReg?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", 
					credentialStore.USER, credentialStore.PASS);
			
			//ignore this
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursereg?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", 
//					"root", "root");
			
			
			System.out.println("connection accepted");
			theStatement = conn.createStatement();
			//rs is what we will use to communicate queries.
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * this function will add a student to the STUDENT table in the database
     * @param name the name of the student to add
     * @param ID the students id (primary key)
     * @param grade the grade of the student (will be stored as an ASCII value in the database)
     */
	public void addStudent (String name, int ID, char grade) {
		String query = "INSERT INTO STUDENT (name,id , grade, numCourses) values(?,?,?,?)";
		try {
			PreparedStatement pStat= conn.prepareStatement(query);
			pStat.setString(1, name);
			pStat.setInt(2, ID);
			pStat.setInt(3, grade);
			pStat.setInt(4, 0);
			pStat.executeUpdate();
			pStat.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function will add a course to the COURSE table in the database
	 * @param name the name of the course to add
	 * @param number the course number 
	 */
	public void addCourse (String name, int number) {
		String query = "INSERT INTO COURSE (name, number, numberEnrolled, status) values (?,?,?,?)";
		try {
			PreparedStatement pStat = conn.prepareStatement(query);
			pStat.setString(1, name);
			pStat.setInt(2, number);
			pStat.setInt(3, 0);
			pStat.setInt(4, 0);
			pStat.executeUpdate();
			pStat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    /**
     *creates a student table in the coursereg database
     */
    public void createStudentTable () {
        try {
            //startConnection();
            String query = "CREATE TABLE STUDENT (name varchar(255), id integer not NULL, grade integer not NULL, numCourses integer not NULL, PRIMARY KEY(id))";
            Statement Stat = conn.createStatement();
            //execute the prepared statement query
            Stat.executeUpdate(query);
            Stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * the admin table will contain a list linking students to the courses which they are taking.
     * each new enrollment in a course will create a new admin entry
     */
    public void createAdminTable () {
    	try {
    		String query = "CREATE TABLE ADMIN (studentID integer not NULL, courseID integer not NULL, section integer not NULL, PRIMARY KEY (studentID, courseID))";
    		PreparedStatement pStat = conn.prepareStatement (query);
    		pStat.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
     * this will create the table COURSE in the coursereg database which will contain a list of all 
     * available courses to be enrolled in
     */
    public void createCourseTable () {
    	try {
    		String query = "CREATE TABLE COURSE (name varchar(255), number integer not NULL, numberEnrolled integer not NULL, status integer, PRIMARY KEY(number))";
    		PreparedStatement pStat = conn.prepareStatement(query);
    		pStat.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    /**
     * this will be called every time a new student enrolls in a course. It will increase the number of students enrolled in that course as well as the number of courses
     * that particular student is enrolled within. 
     * @param courseNum
     * @param studentID
     */
    public void incrementNumberEnrolled (int courseNum, int studentID) {
    	//first let's increment the number of students enrolled in that course.
    	Course theCourse = findCourse (courseNum);
    	int enrollment = theCourse.getNumEnrolled() + 1;
    	String query = 	"UPDATE COURSE SET numberEnrolled = " + enrollment + " WHERE number = " + courseNum;
    	try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//change the status of the course if the enrollment is now 8.
    	if (enrollment == 8) {
    		String statusUpdate = 	"UPDATE COURSE SET status = 1 WHERE number = " + courseNum;
        	try {
    			Statement stmt = conn.createStatement();
    			stmt.executeUpdate(statusUpdate);
    			stmt.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	//now we need to take the student using studentid and increment the number of courses that they are
    	//currently enrolled in
    	System.out.println("update 2");
    	Student theStud = this.findStudent(studentID);
    	int updated = theStud.getNumOfCourses() + 1;
    	String sql = "UPDATE STUDENT SET numCourses = " + updated + " WHERE id = " + studentID;
    	try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * this will be called every time a student decides to drop a course. It will decrease both the number of students enrolled
     * in that course from the course database as well as the number of courses that student is shown to 
     * be enrolled in. 
     * @param courseNum the course number which the student is unenrolling from
     * @param studentID the students id
     */
    public void decrementNumberEnrolled (int courseNum, int studentID) {
    	//first let's decrement the number of students enrolled in that course.
    	Course theCourse = findCourse (courseNum);
    	int enrollment = theCourse.getNumEnrolled() - 1;
    	String query = 	"UPDATE COURSE SET numberEnrolled = " + enrollment + " WHERE number = " + courseNum;
    	try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//change the status of the course if the enrollment is now below 8 due to the drop-out.
    	if (enrollment == 7) {
    		String statusUpdate = 	"UPDATE COURSE SET status = 0 WHERE number = " + courseNum;
        	try {
    			Statement stmt = conn.createStatement();
    			stmt.executeUpdate(statusUpdate);
    			stmt.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	//now we need to take the student using studentid and decrement the number of courses that they are
    	//currently enrolled in
    	
    	Student theStud = this.findStudent(studentID);
    	int updated = theStud.getNumOfCourses() - 1;
    	String sql = "UPDATE STUDENT SET numCourses = " + updated + " WHERE id = " + studentID;
    	try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    


    /**
     * this function will be used to remove a student-course relationship from the admin table
     */
    public void unenrollInCourse (int studID, int courseID) {
    	String query = "DELETE FROM ADMIN WHERE studentID = ? AND courseID = ?";
    	try {
			PreparedStatement pStat = conn.prepareStatement(query);
			pStat.setInt(1, studID);
			pStat.setInt(2, courseID);
			pStat.executeUpdate();
			pStat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	decrementNumberEnrolled(courseID, studID);
    }
    /**
     * this will return a string containing all of the courses which a student is currently enrolled in
     * @param studID the id of the student requesting the information
     * @return the string object containing all courses the student is enrolled in
     */
    public String viewAllEnrolled (int studID) {
    	String query = "SELECT * FROM ADMIN WHERE studentID = ?";
		String output = "";
    	try {
    		PreparedStatement pStat = conn.prepareStatement(query);
    		pStat.setInt(1, studID);
    		rs = pStat.executeQuery();
    		while (rs.next()) {
    			Course find = findCourse(rs.getInt("courseid"));
    			output += "Enrolled in: " + find.getCourseName() + " " + find.getCourseNum() + "\n";
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    		return output;
    }
    /**
     * this will display all the courses in a string which are available currently in the COURSE databse
     * @return a string containing all courses
     */
    public String viewAllCourses () {
    	String query = "SELECT * FROM COURSE";
    	String output = "";
    	try {
    		PreparedStatement pStat = conn.prepareStatement(query);
    		rs = pStat.executeQuery();
    		String theStatus ="";
    		while (rs.next()) {
    			if(rs.getInt("status") == 0)
    				theStatus = "Course is not running, waiting for 8 students to enrol.";
    			else
    				theStatus = "Course is running.";
    			
    			output += "Course: " + rs.getString("name") + " " + rs.getInt("number") + "\nNumber of students enrolled: " + rs.getInt("numberEnrolled") + "\nStatus of the course: "+theStatus +"\n\n";
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return output;
    }
    
    
    /**
     * this will be used to enroll a student in a course by creating a new student-course relationship
     * in the admin table
     * @param studID the student id to enroll in the course
     * @param courseID the course id the student would like to enroll in
     * @param section the section of that course the student will be in
     */
    public void enrollInCourse (int studID, int courseID, int section) {
    	String query = "INSERT INTO ADMIN (studentID, courseID, section) values (?,?,?)";
    	try {
    		PreparedStatement pStat = conn.prepareStatement(query);
    		pStat.setInt(1,  studID);
    		pStat.setInt(2, courseID);
    		pStat.setInt(3, section);
    		pStat.executeUpdate();
    		pStat.close();
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	System.out.println("got to the increment statement");
    	
    	incrementNumberEnrolled(courseID, studID);
    }
    
    /**
     * this will return a student object from the database who has the id speicified
     * @param id the student id to find from the database
     * @return the student object
     */
    public Student findStudent (int id) {
		Student theStud = null;
    	try {
	    	String query = "SELECT * FROM STUDENT where id=?";
	    	PreparedStatement pStat = conn.prepareStatement(query);
	    	pStat.setInt(1, id);
	    	rs = pStat.executeQuery();
	    	rs.first();
	    	theStud = new Student (rs.getString("name"), rs.getInt("id"), (char)(rs.getInt("grade")), rs.getInt("numCourses"));
		} catch (SQLException e) {
			e.printStackTrace();
		}  
    	return theStud;
    }
    
    /**
     * this will return a course object relating to the course with the coursenum as specified in the 
     * parameters
     * @param courseNum the number of the course which will be searched for
     * @return the course object containing that course number
     */
    public Course findCourse (int courseNum) {
    	Course theCourse = null;
    	try {
    		String query = "SELECT * FROM COURSE where number=?";
    		PreparedStatement pStat = conn.prepareStatement(query);
    		pStat.setInt(1, courseNum);
    		rs = pStat.executeQuery();
    		rs.first();
    		theCourse = new Course (rs.getString("name"), rs.getInt("number"), rs.getInt("numberEnrolled"), rs.getInt("status"));
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return theCourse;
    }
    /**
     * this will add 10 entries to the STUDENT table
     */
    public void populateStudents () {
    	addStudent ("Bob", 1000, 'B');
    	addStudent ("Dave", 2000, 'C');
    	addStudent ("Kate", 3000, 'A');
    	addStudent ("Tina", 4000, 'B');
    	addStudent ("Adam", 5000, 'A');
    	addStudent ("Chris", 6000, 'D');
    	addStudent ("Cody", 7000, 'B');
    	addStudent ("Sam", 8000, 'A');
    	addStudent ("Dan", 9000, 'C');
    	addStudent ("Cam", 10000, 'D');
    }
    /**
     * this will add 6 entries to the COURSE table
     */
    public void populateCourses() {
    	addCourse ("ENGG", 233);
    	addCourse ("PHYS", 259);
    	addCourse ("PSYC", 200);
    	addCourse ("ENSF", 409);
    	addCourse ("ENEL", 353);
    	addCourse ("ENCM", 369);
    	
    }
    
    
	public static void main ( String args[]) {
		DBController test = new DBController();
		test.startConnection();
		
		Server server = new Server();
		server.communicateWithServer(test);
		
		
		
		//test.createStudentTable();
		//test.createAdminTable();

		//test.populateStudents();
//		test.enrollInCourse(2000, 233, 1);
//		System.out.println(test.viewAllEnrolled(2000));
		//UNCOMMENT THE FOLLOWING LINES THE FIRST TIME YOU RUN THE PROG
		//test.createCourseTable();
		//test.createOfferingTable();
		//test.populateCourses();
		
	}
	
	
}
