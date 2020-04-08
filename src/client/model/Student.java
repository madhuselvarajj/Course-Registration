package client.model;

import java.io.Serializable;
import java.util.ArrayList;


//client needs a student class in order to be able to send an object of student through the network (the socket)

public class Student implements Serializable{
	public String name;
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
