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
 * should ideally have 2 tabled? For courses and students -- only student table present now.
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
	/**
     * just for testing purposes, will be fixed to take in proper args and select/return the student
     */
	public void selectStudent () {
		try{
			String query= "SELECT * FROM students where name= ? and ID =? and grade=?";
			PreparedStatement pStat= conn.prepareStatement(query);
			pStat.setString(1, "Bob"); //bob should be in index 1 
			pStat.setString(2, "Dave"); //Dave should be in index 2
			rs= pStat.executeQuery(); //the result statement is when we execute the above query for student DB
			while(rs.next()) {
				System.out.println(rs.getString("name") + " "+ rs.getString("ID"));
			}
		pStat.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
    
	public void addStudent (String name, int ID, String grade) {
		String query = "INSERT INTO Students (name, ID, grade) values (?,?,?)";
		try {
			PreparedStatement pStat= conn.prepareStatement(query);
			pStat.setString (1, name);
			pStat.setInt (2, ID);
			pStat.setString(3, grade);
			int theRow = pStat.executeUpdate();
			//theRow is an integer which contains the row that the student you are looking for is 
			//contained within
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
            startConnection();
            String query = "CREATE TABLE IF NOT EXISTS Student (name varchar(300), id int, grade varchar(1), PRIMARY KEY(id)";
            PreparedStatement pStat = conn.prepareStatement(query);
            //execute the prepared statement query
            pStat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void main ( String args[]) {
		DBController test = new DBController();
		test.startConnection();
	}
	
	
}
