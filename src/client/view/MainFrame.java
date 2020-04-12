package client.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public JButton OK = new JButton ("OK");
    public JButton cancel = new JButton ("cancel");
    
    public MainFrame() {
        super ("main menu:");
        displayMainFrame();
    }
    
    //need a class Course that is serializable for the following options:
    //searchCat, add course, find course, view offerings, remove course, possible view registration?
    public void askForCourseInfo() {
        JPanel coursePrompt = new JPanel();
        coursePrompt.add(courseName);
        coursePrompt.add(courseNum);
        coursePrompt.add(OK);
        coursePrompt.add(cancel);
        add(coursePrompt);
        coursePrompt.setVisible(true);
        setVisible(true);
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
        JPanel p = new JPanel();
        p.add(mainLabel);
        p.add(viewAllStud);
        p.add((viewAllCat));
        p.add(removeCourse);
        p.add(addCourse);
        p.add(searchCat);
        p.add(exit);
        
        
        //add the panel to the frame
        add(p);
        p.setVisible(true);
        setVisible(true);
    }
    
    public void displayText (String str) {
        JPanel textPanel = new JPanel();
        JTextArea textArea = new JTextArea(400,400);
        textPanel.add(textArea, BorderLayout.CENTER);
        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textPanel.add(scroll);
        textArea.setText(str);
        textPanel.setVisible(true);
        setVisible(true);
    }
    
    
}
