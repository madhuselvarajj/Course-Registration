package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import server.model.Student; //doesn't work with client.model.Student and I think it is supposed to 
import client.view.MainFrame;

public class MainFrameController {
	public MainFrame mainFrame;
	public Communication communication;
	
	public MainFrameController(MainFrame m, Communication com) {
		this.mainFrame = m;
		this.communication = com;
		mainFrame.setVisible(true);
		addListeners();
	}
	
	private void addListeners() {
		//listener for the exit button
		mainFrame.addExitListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String response = (String)communication.sendCode("EXIT"); //sends just a code because there is no object to send 
				System.out.println(response); //prints the response of the server
				mainFrame.dispose(); //closes the main frame and terminates the client
			}
			
		});
		
		//listener for the add student button
		mainFrame.addStudent(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//needs to send both a new student object, and a code
				//the code tells the server what to with the object
				String response = (String)communication.sendObjectWithCode("ADD",new Student("bob", 8, 'F')); 
				System.out.println(response);
			}
			
		});
		
	}
}
