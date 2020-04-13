package server.model;

import java.util.ArrayList;

/**
 * Represents a catalogue containing all of the courses available for registration
 * @author Madhu Selvaraj, Navjot Singh
 *
 */
public class CourseCatalogue {
	
	/**
	 * Array list of all of the courses in the catalogue
	 */
	private ArrayList<Course>courseList;  
	
	
	/**
	 * Constructor that creates a new CourseCatalogue object 
	 */
	public CourseCatalogue() {
		courseList = new ArrayList<Course>();
	}

	/**
	 * Returns courseList
	 * @return courseList
	 */
	public ArrayList<Course> getCourseList() {
		return courseList;
	}

	/**
	 * Sets courseList
	 * @param courseList the array list of courses to assign to courseList
	 */
	public void setCourseList(ArrayList<Course> courseList) {
		this.courseList = courseList;
	}
	
	/**
	 * Displays all of the courses in courseList
	 * @return a string containing all of the courses in courseList
	 */
	public String displayAllCourses() {
		String s = "";
		for(Course c: courseList)
			s +=c +"\n";
		
		return s;
	}
	
	/**
	 * Looks for a course in courseList with a specific course number
	 * @param id the course number to look for
	 * @return a string containing the information about that course 
	 */
	public String searchForCourse(int id) {
		String s ="";
		Course temp = null;
		for(Course c: courseList) {
			if(c.getCourseNum() == id) {
				temp = c;
				break;
			}
		}
		if( temp == null)
			return null;
		
		s += temp.getCourseName() + " " +temp.getCourseNum() + "\n";
		s+= "\nThese are all of the offerings \n";
		
		for(CourseOffering o: temp.getOfferingList())
			s+="Section " + o.getSecNum() + ": has capacity of " + o.getSecCapacity() + "\n";
		return s;
	}
}
