package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import server.model.Student;

/**
 * Communicates with Server
 *
 */
public class ServerCommunication {
	Socket aSocket;
	ServerSocket serverSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	public Student theStudent = new Student(); //later, this should be the database I think?
	
	public ServerCommunication(){
		try {
			serverSocket = new ServerSocket(1010);
			aSocket = serverSocket.accept();
			System.out.println("Client connected");
			out = new ObjectOutputStream(aSocket.getOutputStream());
			in = new ObjectInputStream(aSocket.getInputStream());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * First reads the code, then depending on the code it will call another method or exit
	 */
	public void communicateWithClient() {
		String line = "";
		while(true) {
			try {
				line = (String)in.readObject(); //reads the code
				if(line.equals("EXIT")) { //if code == "EXIT" then break loop
					out.writeObject("Bye");
					break;
				}else if(line.equals("ADD")) { //if code == "ADD" then read the object
					Object temp = in.readObject();
					addObject(temp);
				}
				else {
					out.writeObject("Error in reading code");
					break;
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addObject(Object temp) {
		try {
			Student newStudent = (Student)temp;
			theStudent.allStudents.add(newStudent); //later will add to the database
			String response = newStudent.name + " was added. Number of students is now: " + theStudent.allStudents.size();
			out.writeObject(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main (String [] args) {
		ServerCommunication sv = new ServerCommunication();
		sv.communicateWithClient();
	}
}


