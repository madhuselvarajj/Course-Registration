package server.model;
import java.util.ArrayList;

/**
 * Represents a course offering/section for a specific course object
 * @author Madhu Selvaraj, Navjot Singh
 *
 */
public class CourseOffering {
	/**
	 * The section number
	 */
	private int secNum;
	
	/**
	 * Max number of students that can be enrolled 
	 */
	private int secCapacity;
	
	/**
	 * The course that this offering is associated with 
	 */
	private Course theCourse;
	
	/**
	 * Array list of registrations 
	 */
	private ArrayList<Registration>regList;
	
	/**
	 * Constructor that creates a CourseOffering with the given values 
	 * @param secNum the section number 	
	 * @param secCapacity the section capacity
	 * @param c the course 
	 */
	public CourseOffering(int secNum, int secCapacity, Course c) {
		this.setSecNum(secNum);
		this.setSecCapacity(secCapacity);
		this.setTheCourse(c);
		setRegList(new ArrayList<Registration>());
	}

	/**
	 * Returns the section number
	 * @return secNum
	 */
	public int getSecNum() {
		return secNum;
	}

	/**
	 * Sets the section number
	 * @param secNum the section number
	 */
	public void setSecNum(int secNum) {
		this.secNum = secNum;
	}

	/**
	 * Returns the section capacity
	 * @return secCapacity
	 */
	public int getSecCapacity() {
		return secCapacity;
	}

	/**
	 * Sets the section capacity
	 * @param secCapacity the section capacity
	 */
	public void setSecCapacity(int secCapacity) {
		this.secCapacity = secCapacity;
	}

	/**
	 * Gets the registration array list
	 * @return regList
	 */
	public ArrayList<Registration> getRegList() {
		return regList;
	}

	/**
	 * Sets the registration array list
	 * @param regList 
	 */
	public void setRegList(ArrayList<Registration> regList) {
		this.regList = regList;
	}

	/**
	 * Gets the course that this CourseOffering class is associated with 
	 * @return theCourse 
	 */
	public Course getTheCourse() {
		return theCourse;
	}

	/**
	 * Sets the course 
	 * @param theCourse
	 */
	public void setTheCourse(Course theCourse) {
		this.theCourse = theCourse;
	}
	
	/**
	 * Returns a string containing the information about the course, and about this course offering 
	 */
	public String toString() {
		String s = theCourse + "\nSection " + this.secNum + "\n";
		return s;
	}
}
