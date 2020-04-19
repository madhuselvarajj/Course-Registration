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
		String query = "INSERT INTO STUDENT (name,id , grade) values(?,?,?)";
		try {
			PreparedStatement pStat= conn.prepareStatement(query);
			pStat.setString(1, name);
			pStat.setInt(2, ID);
			pStat.setInt(3, grade);
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
            String query = "CREATE TABLE STUDENT (name varchar(255), id integer not NULL, grade integer not NULL, PRIMARY KEY(id))";
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
    		String query = "CREATE TABLE COURSE (name varchar(255), number integer not NULL, PRIMARY KEY(number))";
    		PreparedStatement pStat = conn.prepareStatement(query);
    		pStat.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    
    
    public void addOffering (String name, int num, int section) {
    	String query = "INSERT INTO COURSES (name,number,section) values(?,?,?)";
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
    }
    
    
    public Student findStudent (int id) {
		Student theStud = null;
    	try {
	    	String query = "SELECT * FROM STUDENTS where id=?";
	    	PreparedStatement pStat = conn.prepareStatement(query);
	    	pStat.setInt(1, id);
	    	rs = pStat.executeQuery();
	    	theStud = new Student (rs.getString("name"), rs.getInt("id"), (char)(rs.getInt("grade")));

		} catch (SQLException e) {
			e.printStackTrace();
		}  
    	return theStud;
    }
    
    
    public Course findCourse (int courseNum) {
    	Course theCourse = null;
    	try {
    		String query = "SELECT * FROM COURSES where number=?";
    		PreparedStatement pStat = conn.prepareStatement(query);
    		pStat.setInt(1, courseNum);
    		rs = pStat.executeQuery(query);
    		theCourse = new Course (rs.getString("name"), rs.getInt("number"));
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return theCourse;
    }
    
    
    
    
    
    
    
    
    
    
    
    
	public static void main ( String args[]) {
		DBController test = new DBController();
		test.startConnection();
		//UNCOMMENT THE FOLLOWING LINES THE FIRST TIME YOU RUN THE PROG
		//test.createStudentTable();
		//test.createCourseTable();
		//test.createAdminTable();
		test.createOfferingTable();
	}
	
	
}
