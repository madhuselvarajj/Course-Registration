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
        //listener for the exit button. This sends code: 6 to the server socket
        mainFrame.addExitListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String response = (String)communication.sendCode(6); //sends just a code because there is no object to send
                System.out.println(response); //prints the response of the server
                mainFrame.dispose(); //closes the main frame and terminates the client
            }
            
        });
        
        //anonymous listener class for the "OK" button
        mainFrame.OK.addActionListener(new ActionListener ()  {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                //send the course name and then course number to the socket in that order
                //since the codes will be sent out first, for each of these performed it will
                //automatically display the response on the GUI depending on what button
                //was pressed prior to pressing the OK (ex delete vs add course)
                communication.out.writeObject(mainFrame.courseName.getText());
                communication.out.writeObject(mainFrame.courseNum.getText());
                mainFrame.displayText (communication.getServerResponse());
                
            }
            
        });
        
        //anonymous listener class for the "cancel" button
        mainFrame.cancel.addActionListener(new ActionListener ( ) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMainFrame();
            }
            
        });
        
        //anonymous listener class for the "search catalogue button"
        //this will send the Course object and code
        //code : 1
        mainFrame.searchCat.addActionListener (new ActionListener ( ) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode (1);
                mainFrame.askForCourseInfo();
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
                mainFrame.askForCourseInfo();
                
            }
            
        });
        
        //code : 3
        //anonymous listener class for the "remove course" button
        //this will send the Course object and code to the socket
        mainFrame.removeCourse.addActionListener (new ActionListener ( ) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode (3);
                mainFrame.askForCourseInfo();
                
            }
            
        });
        
        //code : 4
        //anonymous listener class for the "view all in catalogue" button
        //this will only send a code to the socket.
        mainFrame.viewAllCat.addActionListener (new ActionListener () {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode(4);
                mainFrame.displayText(communication.getServerResponse());
                
            }
            
        });
        
        //code : 5
        //anonymous listener class for the "view all courses taken by student" button
        //this will only send a code to the socket.
        mainFrame.viewAllStud.addActionListener (new ActionListener () {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                communication.sendCode(5);
                mainFrame.displayText(communication.getServerResponse());
                
            }
            
        });
        
        
    }
}
