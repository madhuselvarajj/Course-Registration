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
			theStudent.incrementNumOfCourses();
			
			theOffering.getRegList().add(this);
			theOffering.getTheCourse().incrementNumEnrolled();
			theOffering.getTheCourse().determineCourseStatus();
			return true;
		}else { //return false if student has already been registered in the max number of courses
			return false;
		}
	}
	
	public CourseOffering getOffering() {
		return theOffering;
	}
	public Student getStudent() {
		return theStudent;
	}
	public void removeRegistration() {
		theStudent.getRegList().remove(this);
		theOffering.getRegList().remove(this);
		theStudent.decrementNumOfCourses();
		
		theOffering.getTheCourse().decrementNumEnrolled();
		theOffering.getTheCourse().determineCourseStatus();
		theStudent = null;
		theOffering = null;
	}
}
