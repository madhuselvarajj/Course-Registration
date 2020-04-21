package client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
/**
 *class mainFrame is the class that will handle the graphic user interface which the
 *client will be interacting with. This will be achieved through the use of a JFrame.
 *this class extends JFrame which will contain the main frame which all user interactions
 *will be based off of.
 * @author Navjot Singh, Madhu Selvaraj
 */
public class MainFrame extends JFrame{
    /**
     *searchCat is used to search for a course in the course catalogue
     */
    public JButton searchCat = new JButton("Search the Catalogue for a Course");
    /**
     *addCourse is used when a student wants to add a course to their current course listing
     */
    public JButton addCourse = new JButton("Enroll in a Course");
    /**
     *removeCourse is used when a student wants to remove a course from their current course listing
     */
    public JButton removeCourse = new JButton ("Unenroll in a Course");
    /**
     *viewAllCat is used to view all courses available to enroll in
     */
    public JButton viewAllCat = new JButton ("View all Courses in the Catalogue");
    /**
     *viewAllStud is used to view all courses that one particular student is taking
     */
    public JButton viewAllStud = new JButton ("View all Enrolled Courses");
    /**
     *exit is used when a student would like to exit the main menu and terminate the program
     */
    public JButton exit = new JButton("Exit");
    /**
     *courseName is a text field prompting for only the name (ex. ENGG)
     */
    public JTextField courseName = new JTextField ("", 30);
    /**
     *courseNum is a text field prompting for the specific class number (ex. 233)
     */
    public JTextField courseNum = new JTextField ("",30);
    /**
     *section is a text field prompting for the specific section pertaining to a course
     */
    public JTextField section = new JTextField ("",30);
    /**
     *In order to make changes a student must enter their unique student ID (ex. to add a course)
     */
    public JTextField studentID = new JTextField ("",30);
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
    /**
     *when a student chooses an option from the main menu but would like to return to the
     *main menu, they will hit cancel.
     */
    public JButton cancel = new JButton ("Cancel");
    /**
     *this is the only JPanel used in the JFrame (continually flushed in functions).
     */
    public JPanel thePanel = new JPanel();
    /**
     *the default constructor will simply display the main frame and prompt for the student
     *to choose from a menu item.
     */
    public MainFrame() {
        super ("Main Menu");
        displayMainFrame();
    }
    
    /**
     *this function is used when a student would like to search for a course from the course *catalogue -prompts for course name and number only.
     */
    public void searchForCourse() {
        remove(thePanel);
        thePanel = new JPanel();
        
        JLabel title = new JLabel("Please Enter Course Information", SwingConstants.CENTER);
        JPanel buttons = new JPanel();
        buttons.add(Search);
        buttons.add(cancel);
        
        JPanel userInfo = new JPanel();
        JLabel courseNameLabel = new JLabel("Course Name: ");
        JLabel courseNumLabel = new JLabel("Course Number: ");
        
        userInfo.add(courseNameLabel);
        userInfo.add(courseName);
        userInfo.add(courseNumLabel);
        userInfo.add(courseNum);
        
        thePanel.setLayout(new BorderLayout());
        thePanel.add("North",title);
        thePanel.add(userInfo,BorderLayout.CENTER);
        thePanel.add("South",buttons);
        add(thePanel);
        thePanel.setVisible(true);
        setVisible(true);
    }
    /**
     *This function is used to add or remove a course, and promptys for course name, number
     *section and the student's ID to change the student's course listing.
     */
    public void addOrRemoveCourse() {
        remove(thePanel);
        thePanel = new JPanel();
        
        JLabel title = new JLabel("Please Enter Course Information", SwingConstants.CENTER);
        
        JPanel buttons = new JPanel();
        buttons.add(OK);
        buttons.add(cancel);
        
        JPanel userInfo = new JPanel();
        JLabel courseNameLabel = new JLabel("Course Name: ");
        JLabel courseNumLabel = new JLabel("Course Number: ");
        JLabel sectionLabel = new JLabel("Section Number: ");
        JLabel idLabel = new JLabel("Student ID: ");
        userInfo.add(courseNameLabel);
        userInfo.add(courseName);
        userInfo.add(courseNumLabel);
        userInfo.add(courseNum);
        userInfo.add(sectionLabel);
        userInfo.add(section);
        userInfo.add(idLabel);
        userInfo.add(studentID);
        
        thePanel.setLayout(new BorderLayout());
        thePanel.add("North",title);
        thePanel.add(userInfo,BorderLayout.CENTER);
        thePanel.add("South",buttons);
        add(thePanel);
        thePanel.setVisible(true);
        setVisible(true);
    }
    /**
     *this function only prompts for student ID for a student to see all courses they are
     *currently enrolled in.
     */
    public String viewAllCourses () {
        String input = JOptionPane.showInputDialog("Enter student id");
        System.out.println(input);
        return input;
    }
    /**
     *this function displays the main frame and allows a student to choose from the
     *main menu items (such as add/remove course, search course catalogue, etc.)
     */
    public void displayMainFrame ( ) {
        setSize(500,500);
        //frame only terminates properly when exit button is pressed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        courseName.setText("");
        courseNum.setText("");
        section.setText("");
        studentID.setText("");
        JLabel mainLabel = new JLabel ("Please select an option:");
        mainLabel.setBounds(new Rectangle (15, 5, 450, 75));
        
        //set the bounds for each button so they stack on top of each other
        exit.setPreferredSize (new Dimension (450,35));
        viewAllStud.setPreferredSize (new Dimension (450,35));
        viewAllCat.setPreferredSize (new Dimension (450,35));
        removeCourse.setPreferredSize (new Dimension (450,35));
        addCourse.setPreferredSize (new Dimension (450,35));
        searchCat.setPreferredSize (new Dimension (450,35));
        
        //in case the panel is already placed, need to get rid of it
        getContentPane().removeAll();
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
        validate();
        thePanel.setVisible(true);
        setVisible(true);
    }
    
    /**
     *displayMessage is used to show the String passed as a GUI component (a message on a
     *JOptionPane).
     *@param response: the string the user wishes to display on the GUI.
     */
    public void displayMessage(String response) {
        JOptionPane.showMessageDialog(this, response);
    }
    
    
    
    
}
