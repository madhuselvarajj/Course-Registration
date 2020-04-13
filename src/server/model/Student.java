package server.model;

import java.util.ArrayList;
/**
 * Contains the information about a Student
 * @author Madhu Selvaraj, Navjot Singh
 */
public class Student{
	/**
	 * The name of the student
	 */
	private  String name;
	
	/**
	 * Student's id number
	 */
	private int id;
	
	/**
	 * Student's grade
	 */
	private char grade;
	
	/**
	 * The number of courses the Student is enrolled in. Max of 6.
	 */
	private int numOfCourses;
	
	/**
	 * ArrayList of courses that the student is registered in 
	 */
	private ArrayList<Registration>regList;
	
	/**
	 * Default constructor
	 */
	public Student(){
		this(" ", 0, ' ');
	}
	
	/**
	 * Constructor that creates a new Student object with the given name, id, and grade
	 * @param name The name of the student
	 * @param id The id of the student
	 * @param grade The student's grade
	 */
	public Student(String name, int id, char grade){
		this.setName(name);
		this.id = id;
		this.setGrade(grade);
		regList = new ArrayList<Registration>();
		numOfCourses = 0;
	}
	
	/**
	 * Increases the number of courses by 1
	 */
	public void incrementNumOfCourses() {
		this.numOfCourses++;
	}
	
	/**
	 * Decreases the number of courses by 1
	 */
	public void decrementNumOfCourses() {
		this.numOfCourses--;
	}
	
	/**
	 * Prints all of the courses the student is registered in
	 * @return A string containing the course information for each course the student is registered in 
	 */
	public String printRegList() {
		String s ="";
		for(Registration r: regList) 
			s+= r.getOffering();
		
		return s;
	}
	
	/**
	 * Returns the student id
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns student's grade
	 * @return grade
	 */
	public char getGrade() {
		return grade;
	}
	
	/**
	 * Returns list of registered courses
	 * @return regList
	 */
	public ArrayList<Registration>getRegList() {
		return this.regList;
	}
	
	/**
	 * Returns the number of courses the student is registered in
	 * @return numOfCourses 
	 */
	public int getNumOfCourses() {
		return numOfCourses;
	}
	
	/**
	 * Returns the name of the student 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the grade 
	 * @param grade
	 */
	public void setGrade(char grade) {
		this.grade = grade;
	}

	/**
	 * Sets the name 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
