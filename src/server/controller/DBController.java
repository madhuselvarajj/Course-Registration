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
* courseOffering will contain the sections for the course - therefore it has a composite keu
* composed of courseName and section
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
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseReg?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", credentialStore.USER, credentialStore.PASS);
			System.out.println("connection accepted");
			theStatement = conn.createStatement();
			//rs is what we will use to communicate queries.
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

    
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
     *creates a student table if it does not already exists, no changes made if the table exists.
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
    
    public void createOfferingTable () {
    	try {
    		//startConnection();
    		String query = "CREATE TABLE OFFERINGS (name varchar (300), number integer not NULL, section integer not NULL, PRIMARY KEY(number, section))";
    		PreparedStatement pStat = conn.prepareStatement(query);
    		//execute the prepared statement query
    		pStat.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void createAdminTable () {
    	try {
    		String query = "CREATE TABLE ADMIN (studentID integer not NULL, courseID integer not NULL, section integer not NULL, PRIMARY KEY (studentID, courseID))";
    		PreparedStatement pStat = conn.prepareStatement (query);
    		pStat.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
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
    	//first let's increment the number of students enrolled in that course.
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
    	
    	//now we need to take the student using studentid and increment the number of courses that they are
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
    
    
    
    
    
    public void addOffering (String name, int num, int section) {
    	String query = "INSERT INTO COURSE (name,number,section) values(?,?,?)";
    	try {
			PreparedStatement pStat = conn.prepareStatement(query);
			pStat.setString (1, name);
			pStat.setInt(2, num);
			pStat.setInt(3, section);
			pStat.executeUpdate();
			pStat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    
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
    
    public String viewAllEnrolled (int studID) {
    	String query = "SELECT * FROM ADMIN WHERE studentID = ?";
		String output = "";
    	try {
    		PreparedStatement pStat = conn.prepareStatement(query);
    		pStat.setInt(1, studID);
    		rs = pStat.executeQuery();
    		while (rs.next()) {
    			Course find = findCourse(rs.getInt("courseid"));
    			output += "enrolled in " + find.getCourseName() + " " + find.getCourseNum() + "\n";
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    		return output;
    }
    
    public String viewAllCourses () {
    	String query = "SELECT * FROM COURSE";
    	String output = "";
    	try {
    		PreparedStatement pStat = conn.prepareStatement(query);
    		rs = pStat.executeQuery();
    		while (rs.next()) {
    			output += "Course: " + rs.getString("name") + " " + rs.getInt("number") + "Students enrolled: " + rs.getInt("numberEnrolled") + "\n";
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return output;
    }
    
    
    
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
		//test.createStudentTable();
		//test.createAdminTable();

		//test.populateStudents();
		test.enrollInCourse(2000, 233, 1);
		System.out.println(test.viewAllEnrolled(2000));
		//UNCOMMENT THE FOLLOWING LINES THE FIRST TIME YOU RUN THE PROG
		//test.createCourseTable();
		//test.createOfferingTable();
		//test.populateCourses();
		
	}
	
	
}
