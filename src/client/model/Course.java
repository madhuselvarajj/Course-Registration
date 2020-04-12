package Client.model;

import java.io.Serializable;

public class Course implements Serializable {
	private String courseName;
	private int courseNum;
	
	public Course (String name, int number) {
		courseName = name;
		courseNum = number;
	}

}
