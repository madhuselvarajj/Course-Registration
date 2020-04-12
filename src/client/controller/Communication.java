package client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import client.view.MainFrame;

/**
 * Communicates with Server 
 *
 */
public class Communication {
	 Socket aSocket; //the socket
	
	 ObjectOutputStream out; //writes objects to the server 
	
	 ObjectInputStream in; //reads objects from the server
	
	
	public Communication(String name, int portNum){
		try {
			aSocket = new Socket(name, portNum);
			out = new ObjectOutputStream(aSocket.getOutputStream());
			in = new ObjectInputStream(aSocket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Sends just the code to the server
	 * @param code
	 * @return the response of the server
	 */
	public void sendCode(int code) {
		try {
			out.writeObject(code);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
    
    public String getServerResponse () {
        String response = "";
        try {
            response = (String)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }
	/**
	 * Sends an object with a code to the server	
	 * @param code tells the server what to do with the object
	 * @param obj
	 * @return
	 */
	public Object sendObjectWithCode(int code, Object obj) {
		Object temp = null;
		try {
			out.writeObject(code); //send the code first
			out.writeObject(obj); 
			temp = in.readObject(); //read the response of the server
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Class not found");
		}
		return temp;
	}
    
	public static void main (String [] args) {
		MainFrame m = new MainFrame();
		Communication c = new Communication("localhost", 1010); 
		MainFrameController v = new MainFrameController(m,c);
		
	}
	
}
