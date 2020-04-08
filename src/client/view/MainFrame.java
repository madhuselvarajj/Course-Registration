package client.view;

import java.awt.event.ActionListener;

import javax.swing.*;

public class MainFrame extends JFrame{
    public JButton exit = new JButton("exit");
    public JButton addStudent = new JButton("add student");
    public MainFrame() {
    	
    	setSize(300,300);
    	//frame only terminates properly when exit button is pressed 
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	JPanel p = new JPanel();
    	p.add(exit);
    	p.add(addStudent);
    	add(p);
    }
    
    public void addExitListener(ActionListener l) {
    	exit.addActionListener(l);
    }
    
    public void addStudent(ActionListener l) {
    	addStudent.addActionListener(l);
    }
 }
