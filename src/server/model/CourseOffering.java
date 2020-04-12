package server.model;

import java.util.ArrayList;

public class CourseOffering {
	private int secNum;
	private int secCapacity;
	private Course theCourse;
	private ArrayList<Registration>regList;
	
	public CourseOffering(int secNum, int secCapacity, Course c) {
		this.setSecNum(secNum);
		this.setSecCapacity(secCapacity);
		this.setTheCourse(c);
		setRegList(new ArrayList<Registration>());
	}

	public int getSecNum() {
		return secNum;
	}

	public void setSecNum(int secNum) {
		this.secNum = secNum;
	}

	public int getSecCapacity() {
		return secCapacity;
	}

	public void setSecCapacity(int secCapacity) {
		this.secCapacity = secCapacity;
	}

	public ArrayList<Registration> getRegList() {
		return regList;
	}

	public void setRegList(ArrayList<Registration> regList) {
		this.regList = regList;
	}

	public Course getTheCourse() {
		return theCourse;
	}

	public void setTheCourse(Course theCourse) {
		this.theCourse = theCourse;
	}
	
	public String toString() {
		String s = theCourse + ": In section " + this.secNum;
		return s;
	}
}
