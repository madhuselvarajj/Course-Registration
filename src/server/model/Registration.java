package server.model;
/**
 * Registration class that allows a student to register for a course offering, or to remove a course offering
 * @author Madhu Selvaraj, Navjot Singh
 *
 */
public class Registration {
	/**
	 * Instance of Student
	 */
	private Student theStudent;
	
	/**
	 * Instance of CourseOffering
	 */
	private CourseOffering theOffering;
	
	/**
	 * Default constructor
	 */
	public Registration() {
		this(null,null);
	}
	
	/**
	 * Constructor that creates a new registration object with the given objects 
	 * @param theStudent 
	 * @param theOffering
	 */
	public Registration (Student theStudent, CourseOffering theOffering) {
		this.theStudent = theStudent;
		this.theOffering = theOffering;
	}
	
	/**
	 * Allows a student to register for a course by assigning this registration object to both the student's regList and CourseOffering's regList
	 * @return True if registration was successful, false if student has already enrolled in the max amount of 6 classes
	 */
	public boolean addRegistration() {
		if(theStudent.getNumOfCourses()<=6) {
			theStudent.getRegList().add(this);
			theStudent.incrementNumOfCourses(); 
			
			theOffering.getRegList().add(this);
			theOffering.getTheCourse().incrementNumEnrolled();
			theOffering.getTheCourse().determineCourseStatus(); //course status may change 
			return true;
		}else { //return false if student has already been registered in the max number of courses
			return false;
		}
	}
	
	/**
	 * Allows a student to remove a course they were registered in. 
	 * Removes this instance of Registration from the regList in theStudent in theOffering
	 * Removes connection to Student and to CourseOffering
	 */
	public void removeRegistration() {
		theStudent.getRegList().remove(this);
		theOffering.getRegList().remove(this);
		theStudent.decrementNumOfCourses();
		
		theOffering.getTheCourse().decrementNumEnrolled();
		theOffering.getTheCourse().determineCourseStatus(); //course status may change 
		theStudent = null;
		theOffering = null;
	}
	
	/**
	 * Returns the instance of CourseOffering
	 * @return theOffering
	 */
	public CourseOffering getOffering() {
		return theOffering;
	}
	
	/**
	 * Returns the instance of Student
	 * @return theStudent
	 */
	public Student getStudent() {
		return theStudent;
	}
	
}
