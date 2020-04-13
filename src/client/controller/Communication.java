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
	
	
    /**
     *this constructs a Communication object with the socket
     *connected to the name and port number as specified, and will also
     *intitialize the relevant input and output streams.
     *@param name : the socket name for the serverSocket which
     *is connected.
     *@param portNum: the port number which the serverSocket is also *connected to
     
     */
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    /**
     *this will return the response from the server when prompted/
     *@return the String object which is written into the socket by
     *the server the client is connected to. 
     */
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
