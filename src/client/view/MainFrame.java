package client.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class MainFrame extends JFrame{
    public JButton searchCat = new JButton("search catalogue courses");
    public JButton addCourse = new JButton("Add course to student courses");
    public JButton removeCourse = new JButton ("Remove course from student courses");
    public JButton viewAllCat = new JButton ("View all courses in catalogue");
    public JButton viewAllStud = new JButton ("view all courses taken by student");
    public JButton exit = new JButton("exit");
    public JTextField courseName = new JTextField ("enter course name");
    public JTextField courseNum = new JTextField ("enter course number");
    public JTextField section = new JTextField ("enter section number");
    public JTextField studentID = new JTextField ("enter the student ID");
    /**
     * Search is the JButton which will be used when a student is searching
     * for a particular course in the course catalogue
     */
    public JButton Search = new JButton ("Search");
    /**
     * OK is the JButton which will be used when a student is trying to add
     * or remove a course (will also require the student to enter their ID).
     */
    public JButton OK = new JButton ("OK");
    /**
     * view is the JButton which will be used when a student is trying to view
     * all of the courses they are currently taking.
     */
    public JButton view = new JButton ("View");
    public JButton cancel = new JButton ("cancel");
    public JPanel thePanel = new JPanel();
    
    public MainFrame() {
        super ("main menu:");
        displayMainFrame();
    }
    
    //need a class Course that is serializable for the following options:
    //searchCat, add course, find course, view offerings, remove course, possible view registration?
    public void searchForCourse() {
        remove(thePanel);
        thePanel = new JPanel();
        thePanel.add(courseName);
        thePanel.add(courseNum);
        thePanel.add(Search);
        thePanel.add(cancel);
        add(thePanel);
        thePanel.setVisible(true);
        setVisible(true);
    }
    
    public void addOrRemoveCourse() {
        remove(thePanel);
        thePanel = new JPanel();
        thePanel.add(courseName);
        thePanel.add(courseNum);
        thePanel.add(section);
        thePanel.add(studentID);
        thePanel.add(OK);
        thePanel.add(cancel);
        add(thePanel);
        thePanel.setVisible(true);
        setVisible(true);
    }
    
    public String viewAllCourses () {
//        remove(thePanel);
//        thePanel = new JPanel();
//        thePanel.add(studentID);
//        thePanel.add(view);
//        thePanel.add(cancel);
//        add(thePanel);
//        thePanel.setVisible(true);
//        setVisible(true);
    	String input = JOptionPane.showInputDialog("Enter student id");
    	return input;
    }
    
    public void displayMainFrame ( ) {
        setSize(500,500);
        //frame only terminates properly when exit button is pressed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        JLabel mainLabel = new JLabel ("Please select an option:");
        mainLabel.setBounds(new Rectangle (15, 5, 450, 75));
        
        //set the bounds for each button so they stack on top of each other
        exit.setPreferredSize (new Dimension (450,35));
        viewAllStud.setPreferredSize (new Dimension (450,35));
        viewAllCat.setPreferredSize (new Dimension (450,35));
        removeCourse.setPreferredSize (new Dimension (450,35));
        addCourse.setPreferredSize (new Dimension (450,35));
        searchCat.setPreferredSize (new Dimension (450,35));
        
        
        //add the buttons for the main menu items to the main panel
        thePanel = new JPanel();
        thePanel.add(mainLabel);
        thePanel.add(viewAllStud);
        thePanel.add((viewAllCat));
        thePanel.add(removeCourse);
        thePanel.add(addCourse);
        thePanel.add(searchCat);
        thePanel.add(exit);
        
        removeCourse.addActionListener (new ActionListener ()  {
            @Override
            public void actionPerformed (ActionEvent e) {
                addOrRemoveCourse();
            }
            
        });
        
        
        //add the panel to the frame
        add(thePanel);
        thePanel.setVisible(true);
        setVisible(true);
    }
    
    public void displayText (String str) {
        remove(thePanel);
        thePanel = new JPanel();
        JTextArea textArea = new JTextArea(400,400);
        thePanel.add(textArea, BorderLayout.CENTER);
        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        thePanel.add(scroll);
        textArea.setText(str);
        thePanel.setVisible(true);
        setVisible(true);
    }

	public void displayMessage(String response) {
		JOptionPane.showMessageDialog(this, response);
	}
    
    
    
}
