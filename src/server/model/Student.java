package server.model;

import java.io.Serializable;
import java.util.ArrayList;

//just an example in order to test sending objects to a socket
public class Student implements Serializable{
	public  String name;
	public int id;
	public char grade;
	private int numOfCourses; //should be the length of regList
	public ArrayList<Student> allStudents = new ArrayList<Student>();
	private ArrayList<Registration>regList;
	public Student(){
		this(" ", 0, 'F');
	}
	public Student(String name, int id, char grade){
		this.name = name;
		this.id = id;
		this.grade = grade;
		regList = new ArrayList<Registration>();
		setNumOfCourses(0);
	}
	
	public int getId() {
		return this.id;
	}
	public ArrayList<Registration>getRegList() {
		return this.regList;
	}
	public int getNumOfCourses() {
		return numOfCourses;
	}
	public void setNumOfCourses(int numOfCourses) {
		this.numOfCourses = numOfCourses;
	}
	
	public String printRegList() {
		String s ="";
		for(Registration r: regList) {
			s+= r.getOffering();
		}
		return s;
	}
}
