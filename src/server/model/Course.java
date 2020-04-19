package server.model;

import java.util.ArrayList;

/**
 * Contains the information about a Course
 * @author Madhu Selvaraj, Navjot Singh
 */
public class Course {
	/**
	 * Name of the course
	 */
	private String courseName;
	
	/**
	 * Course Number
	 */
	private int courseNum;
	
	/**
	 * Number of students enrolled in the course
	 */
	private int numEnrolled;
	
	/**
	 * Status of the course (if the course is running or not). True if numEnrolled >=8. 
	 */
	
	private boolean status;
	
	/**
	 * ArrayList of Courses that are prerequisites 
	 */
	private ArrayList <Course> preReq;
	
	/**
	 * ArrayList of the different the offerings available 
	 */
	private ArrayList<CourseOffering>offeringList;
	
	/**
	 * Constructor that creates a new Course with the given course name and course number
	 * Sets default status to false, and the number enrolled to 0
	 * @param courseName the name of the course 
	 * @param courseNum the course number
	 */
	public Course(String courseName, int courseNum) {
		this.setCourseName(courseName);
		this.setCourseNum(courseNum);
		setStatus(false);
		preReq = new ArrayList<Course>();
		setOfferingList(new ArrayList<CourseOffering>());
		numEnrolled = 0;
	} 
	
	public Course (String name, int num, int enrolled, int Status) {
		this.setCourseName(name);
		this.setCourseNum(num);
		numEnrolled = enrolled;
		if (Status == 1) {
			status = true;
		}
		else {
			status = false;
		}
		
	}
	

	
	/**
	 * Determines the status of the course based on if the number enrolled is at least 8
	 */
	public void determineCourseStatus() {
		if(numEnrolled >= 8) 
			status = true;
		else
			status = false;
	}

	/**
	 * Increments the number of students enrolled by 1
	 */
	public void incrementNumEnrolled() {
		this.numEnrolled++;
	}
	
	/**
	 * Decreases the number of students enrolled by 1
	 */
	public void decrementNumEnrolled() {
		this.numEnrolled--;
	}
	
	/**
	 * Returns the course name
	 * @return courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * Sets the course name
	 * @param courseName the name of the course
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * Gets the course number 
	 * @return courseNum
	 */
	public int getCourseNum() {
		return courseNum;
	}

	/**
	 * Sets the course number 
	 * @param courseNum the course number
	 */
	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}

	/**
	 * Gets the list of course offerings
	 * @return offeringList 
	 */
	public ArrayList<CourseOffering> getOfferingList() {
		return offeringList;
	}

	/**
	 * Sets offeringList 
	 * @param offeringList The array list of course offerings to assign to offeringList
	 */
	public void setOfferingList(ArrayList<CourseOffering> offeringList) {
		this.offeringList = offeringList;
	}
	
	/**
	 * Gets the number of students enrolled in the course
	 * @return numEnrolled
	 */
	public int getNumEnrolled() {
		return numEnrolled;
	}

	/**
	 * Returns the status of the Course
	 * @return status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status of the course 
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	/**
	 * Returns a String containing all of the information about a course. 
	 * Example:
	 * ENSF 409
	 * No prerequisites 
	 * Number of students enrolled: 2
	 * Status: Not running. Waiting for 6 more students to enroll in this course.
	 */
	public String toString() {
		String s = courseName + " " + courseNum + "\n";
		if(preReq.size()==0)
			s+= "No prerequisites  \n";
		else {
			s+="Pre-reqs: \n";
			for(Course c: preReq) 
				s += c;
		}
		s+= "Number of students enrolled: " + this.numEnrolled +"\n"; 
		if(status == true)
			s+= "Status: Running";
		else
			s+="Status: Not running. Waiting for " + (8-this.numEnrolled) + " more students to enroll in this course.";
		return s;
	}
}
