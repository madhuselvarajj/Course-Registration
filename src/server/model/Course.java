package server.model;

import java.util.ArrayList;

public class Course {
	private String courseName;
	private int courseNum;
	private int numEnrolled;
	private boolean status; //True if at lest 8 students are enrolled, false if not  
	private ArrayList <Course> preReq;
	private ArrayList<CourseOffering>offeringList;
	
	public Course(String courseName, int courseNum) {
		this.setCourseName(courseName);
		this.setCourseNum(courseNum);
		numEnrolled = 0;
		status = false;
		preReq = new ArrayList<Course>();
		setOfferingList(new ArrayList<CourseOffering>());
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}

	public ArrayList<CourseOffering> getOfferingList() {
		return offeringList;
	}

	public void setOfferingList(ArrayList<CourseOffering> offeringList) {
		this.offeringList = offeringList;
	}
	
	public String toString() {
		String s = courseName + " " + courseNum;
		return s;
	}
	
}
