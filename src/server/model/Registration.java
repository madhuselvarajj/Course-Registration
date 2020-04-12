package server.model;

public class Registration {
	private Student theStudent;
	private CourseOffering theOffering;
	
	public Registration() {
		this(null,null);
	}
	public Registration (Student theStudent, CourseOffering theOffering) {
		this.theStudent = theStudent;
		this.theOffering = theOffering;
	}
	
	public boolean addRegistration() {
		if(theStudent.getNumOfCourses()<=6) {
			theStudent.getRegList().add(this);
			theStudent.setNumOfCourses(theStudent.getRegList().size());
			theOffering.getRegList().add(this);
			return true;
		}else { //return false if student has already been registered in the max number of courses
			return false;
		}
	}
	
	public CourseOffering getOffering() {
		return theOffering;
	}
}
