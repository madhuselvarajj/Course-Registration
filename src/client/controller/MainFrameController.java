package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
        //listener for the exit button. This sends code: 6 to the server socket
        mainFrame.exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                communication.sendCode(6); //sends just a code because there is no object to send
                mainFrame.displayMessage(communication.getServerResponse());
                mainFrame.dispose(); //closes the main frame and terminates the client
            }
            
        });
        
        //anonymous listener class for the "Search" button
        //this is used when a student searches for a class
        mainFrame.Search.addActionListener(new ActionListener ()  {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                //send the course name and then course number to the socket in that order
                //since the codes will be sent out first, for each of these performed it will
                //automatically display the response on the GUI depending on what button
                //was pressed prior to pressing the OK (ex delete vs add course)
                try {
					communication.out.writeObject(mainFrame.courseName.getText());
					communication.out.writeObject(mainFrame.courseNum.getText());
					String response = (String)communication.in.readObject();
					System.out.println(response);
//		            mainFrame.displayText(response);
					mainFrame.displayMessage(response);
				} catch (IOException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
//                mainFrame.displayText (communication.getServerResponse());
                
            }
            
        });
        //anonymous listener class for the "OK" button
        //this is used when a student would like to add or remove a course
        mainFrame.OK.addActionListener (new ActionListener () {
            
            @Override
            public void actionPerformed (ActionEvent e) {
            
                try {
					communication.out.writeObject(mainFrame.studentID.getText());
	                communication.out.writeObject(mainFrame.courseName.getText());
	                communication.out.writeObject(mainFrame.courseNum.getText());
	                communication.out.writeObject(mainFrame.section.getText());
	                String response = (String)communication.in.readObject();
	                mainFrame.displayMessage(response);
				} catch (IOException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//                mainFrame.displayText (communication.getServerResponse());
               
            }
        });
        
        /** anonymous listener class for the "view" button. This is used when a student
         *would like to view all of the courses that they are currently enrolled in.
         */
        mainFrame.view.addActionListener (new ActionListener () {
            
            @Override
            public void actionPerformed (ActionEvent e) {
                try {
					communication.out.writeObject(mainFrame.studentID.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                mainFrame.displayMessage(communication.getServerResponse());
            }
            
        });
        
        
        
//        anonymous listener class for the "cancel" button
        mainFrame.cancel.addActionListener(new ActionListener ( ) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.displayMainFrame();
            }
            
        });
        
        //anonymous listener class for the "search catalogue button"
        //this will send the Course object and code
        //code : 1
        mainFrame.searchCat.addActionListener (new ActionListener ( ) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
//                communication.sendCode ("1");
            	try {
					communication.out.writeObject(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                mainFrame.searchForCourse();
                //we send the code and then OK's actionListener in askForCourseInfo() will
                //automatically send the nessecary info to the server socket and display the
                //response on the GUI.
                
            }
            
        });
        
        //code : 2
        //anonymous listener class for the "add course" button
        //this will send the Course object and code to the socket.
        mainFrame.addCourse.addActionListener (new ActionListener ( ) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode (2);
                mainFrame.addOrRemoveCourse();
                
            }
            
        });
        
        //code : 3
        //anonymous listener class for the "remove course" button
        //this will send the Course object and code to the socket
        mainFrame.removeCourse.addActionListener (new ActionListener ( ) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode (3);
                mainFrame.addOrRemoveCourse();
                
            }
            
        });
        
        //code : 4
        //anonymous listener class for the "view all in catalogue" button
        //this will only send a code to the socket.
        mainFrame.viewAllCat.addActionListener (new ActionListener () {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode(4);
                mainFrame.displayMessage(communication.getServerResponse());
                
            }
            
        });
        
        //code : 5
        //anonymous listener class for the "view all courses taken by student" button
        //this will send code and student id to the socket
        mainFrame.viewAllStud.addActionListener (new ActionListener () {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode(5);
                String input = mainFrame.viewAllCourses();
                try {
					communication.out.writeObject(input);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                mainFrame.displayMessage(communication.getServerResponse());
                
            }
            
        });
        
    }
}
