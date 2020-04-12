package server.controller;

import java.util.ArrayList;

import server.model.*;

public class DBController {
	//later will have use JDBC, right now just store everything in array lists
	private ArrayList<Course> courseList;
	private ArrayList<CourseOffering> courseOfferingList;
	private ArrayList<Student>studentList;
	public DBController(CourseCatalogue theCatalogue) {
		courseList = new ArrayList<Course>();
		studentList = new ArrayList<Student>();
		setDataBases();
		theCatalogue.setCourseList(courseList);
	}
	
	private void setDataBases() {
		//default courses and students 
		
		courseList.add(new Course("ENGG", 233));
		courseList.add(new Course("ENSF", 409));
		courseList.add(new Course("PHYS", 259));

		studentList.add(new Student("Bob", 1001,'B'));
		studentList.add(new Student("Carl", 1002,'B'));
		studentList.add(new Student("Dave", 1003,'B'));
		
		//adds one offering to each course...might add more later
		for(Course c: courseList) {
			c.getOfferingList().add(new CourseOffering(1,30,c));
		}
	}
	public String displayAllCourses() {
		String s = "";
		for(Course c: courseList)
			s +=c.getCourseName() + " " +c.getCourseNum() + "\n";
		
		return s;
	}
	public ArrayList<Course> getCourseList(){
		return this.courseList;
	}
	public ArrayList<Student> getStudentList(){
		return this.studentList;
	}
	
}
