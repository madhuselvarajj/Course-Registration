package server.model;

import java.io.Serializable;
import java.util.ArrayList;

//just an example in order to test sending objects to a socket
public class Student implements Serializable{
	public  String name;
	public int id;
	public char grade;
	public ArrayList<Student> allStudents = new ArrayList<Student>();
	
	public Student(){
		this(" ", 0, 'F');
	}
	public Student(String name, int id, char grade){
		this.name = name;
		this.id = id;
		this.grade = grade;
	}
}
