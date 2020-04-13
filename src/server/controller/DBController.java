package server.controller;

import java.util.ArrayList;

import server.model.*;
/**
 * Represents the database of the program. Stores all of the courses that are available to register in. Also stores the list of students. 
 * @author Madhu Selvaraj, Navjot Singh
 *
 */
public class DBController {
	/**
	 * Array list of all the courses
	 */
	private ArrayList<Course> courseList;
	
	/**
	 * Array list of all the students
	 */
	private ArrayList<Student>studentList;
	
	/**
	 * Constructor that creates the database
	 * @param theCatalogue
	 */
	public DBController(CourseCatalogue theCatalogue) {
		courseList = new ArrayList<Course>();
		studentList = new ArrayList<Student>();
		setDataBases();
		theCatalogue.setCourseList(courseList);
	}
	
	/**
	 * Adds courses and students to courseList and studentList
	 */
	private void setDataBases() {
		
		//default courses and students 
		courseList.add(new Course("ENGG", 233));
		courseList.add(new Course("ENSF", 409));
		courseList.add(new Course("PHYS", 259));

		studentList.add(new Student("Bob", 1001,'B'));
		studentList.add(new Student("Carl", 1002,'B'));
		studentList.add(new Student("Dave", 1003,'B'));
		
		//adds one offering to each course
		for(Course c: courseList) {
			c.getOfferingList().add(new CourseOffering(1,30,c));
		}
	}

	/**
	 * Returns the list of courses
	 * @return courseList
	 */
	public ArrayList<Course> getCourseList(){
		return this.courseList;
	}
	
	/**
	 * Returns the list of students
	 * @return studentList
	 */
	public ArrayList<Student> getStudentList(){
		return this.studentList;
	}
	
}
