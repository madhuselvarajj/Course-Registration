package server.model;

import java.util.ArrayList;

public class CourseCatalogue {
	private ArrayList<Course>courseList; //array List of all courses 
	
	public CourseCatalogue() {
		courseList = new ArrayList<Course>();
	}

	public ArrayList<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(ArrayList<Course> courseList) {
		this.courseList = courseList;
	}
	
	public String displayAllCourses() {
		String s = "";
		for(Course c: courseList)
			s +=c +"\n";
		
		return s;
	}
	
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
		s+= "These are all of the offerings \n";
		
		for(CourseOffering o: temp.getOfferingList())
			s+="Section " + o.getSecNum() + ": has capacity of " + o.getSecCapacity() + "\n";
		return s;
	}
}
