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
 * Represents and controls the database of the program. 
 * @author Madhu Selvaraj, Navjot Singh
 * 
 */
public class DBController {
	
	/**
	 * Connection to the database
	 */
	private Connection conn;
	
	/**
	 * Executes a SQL statement
	 */
	private Statement theStatement;
	
	/**
	 * Connects to the course registration database created in mySQL. 
	 */
	public void startConnection () {
		String theQuery = new String ("");
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);
			
			//using localHost here
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseReg?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", 
//					credentialStore.USER, credentialStore.PASS);
			
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursereg?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", 
					"root", "root");
			
			System.out.println("Connection to database accepted");
			theStatement = conn.createStatement();
			
			//rs is what we will use to communicate queries.
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * This function will add a student to the STUDENT table in the database
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
	 * This function will add a course to the COURSE table in the database
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
     *Creates a student table in the coursereg database
     */
    public void createStudentTable () {
        try {
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
     * The admin table will contain a list linking students to the courses which they are taking.
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
     * This will create the table COURSE in the coursereg database which will contain a list of all 
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
     * This method will be called every time a new student enrolls in a course. It will increase the number of students enrolled in that course as well as the number of courses
     * that particular student is enrolled within. 
     * @param courseNum the course number which the student is enrolling in
     * @param studentID the student's id
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
     * This will be called every time a student decides to drop a course. It will decrease both the number of students enrolled
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
     * This function will be used to remove a student-course relationship from the admin table
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
     * This will return a string containing all of the courses which a student is currently enrolled in
     * @param studID the id of the student requesting the information
     * @return the string object containing all courses the student is enrolled in
     */
    public String viewAllEnrolled (int studID) {
    	String query = "SELECT * FROM ADMIN";
    	String output = "";
    	try {
    		PreparedStatement pStat = conn.prepareStatement(query);
    		ResultSet resSet = pStat.executeQuery();
    		while (resSet.next()) {
    			if (resSet.getInt("studentID") == studID) {
    				Course theCourse = findCourse (resSet.getInt("courseID"));
    				output += theCourse.getCourseName() + " " + theCourse.getCourseNum() + "\n";
    			}
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return output;
    }
    /**
     * This will display all the courses in a string which are available currently in the COURSE database
     * @return a string containing all courses
     */
    public String viewAllCourses () {
    	String query = "SELECT * FROM COURSE";
    	String output = "";
    	try {
    		PreparedStatement pStat = conn.prepareStatement(query);
    		ResultSet rs = pStat.executeQuery();
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
     * This will be used to enroll a student in a course by creating a new student-course relationship
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
     * This will return a student object from the database who has the id specified
     * @param id the student id to find from the database
     * @return the student object
     */
    public Student findStudent (int id) {
		Student theStud = null;
    	try {
	    	String query = "SELECT * FROM STUDENT WHERE id=?";
	    	PreparedStatement pStat = conn.prepareStatement(query);
	    	pStat.setInt(1, id);
	    	ResultSet rs = pStat.executeQuery();
	    	rs.first();
	    	theStud = new Student (rs.getString("name"), rs.getInt("id"), (char)(rs.getInt("grade")), rs.getInt("numCourses"));
		} catch (SQLException e) {
			e.printStackTrace();
		}  
    	return theStud;
    }
    
    /**
     * This will return a course object relating to the course with the course num as specified in the 
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
    		ResultSet rs = pStat.executeQuery();
    		rs.first();
    		theCourse = new Course (rs.getString("name"), rs.getInt("number"), rs.getInt("numberEnrolled"), rs.getInt("status"));
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return theCourse;
    }
    /**
     * This will add 10 entries to the STUDENT table
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
     * This will add 6 entries to the COURSE table
     */
    public void populateCourses() {
    	addCourse ("ENGG", 233);
    	addCourse ("PHYS", 259);
    	addCourse ("PSYC", 200);
    	addCourse ("ENSF", 409);
    	addCourse ("ENEL", 353);
    	addCourse ("ENCM", 369);
    	
    }
    
    /**
     * Starts the server of the program
     * @param args
     */
	public static void main ( String args[]) {
		DBController test = new DBController();
		test.startConnection();

		
		Server server = new Server();
		server.communicateWithServer(test);
		
		//UNCOMMENT THE FOLLOWING LINES THE FIRST TIME YOU RUN THE PROG
		//test.createStudentTable();
		//test.createAdminTable();
		//test.createCourseTable();
		//test.populateStudents();
		//test.populateCourses();
		
	}
	
	
}
