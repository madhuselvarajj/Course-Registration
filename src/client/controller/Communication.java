package client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import client.view.MainFrame;

/**
 * Communicates with Server. This class will handle all socket input
 *and output and communication between the client and server through
 *the use of a socket. For the purpose of this program we will be using
 *socket 1011. 
 *@author Navjot Singh, Madhu Selvaraj
 */
public class Communication {
	
    /**
     *This is the main socket that the client will be using to communicate
     *with the server.
     */
	 Socket aSocket;
	 
	/**
     *This is the output stream which will be used to write objects
     *to the server using the socket
     */
	 ObjectOutputStream out;  
	 
	/**
     *This is the input stream which will be used to read objects from the
     *server that were written into the socket.
     */
	 ObjectInputStream in; 
	
	
    /**
     *Constructs a Communication object with the socket
     *connected to the name and port number as specified, and will also
     *initialize the relevant input and output streams.
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
	 * @param code the code that tells the server what to do
	 */
	public void sendCode(int code) {
		try {
			out.writeObject(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     *This will return the response from the server when prompted
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
     * Writes the given object to the socket
     * @param input the object to write to the socket
     */
	public void writeToServer(Object input) {
	    	try {
				out.writeObject(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Writes the given objects to the socket
	 * @param input1 the first object to write to the socket 
	 * @param input2 the second object to write to the socket 
	 */
    public void writeToServer(Object input1, Object input2) {
    	try {
			out.writeObject(input1);
			out.writeObject(input2); 
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Writes the given objects to the socket
     * @param input1 the first object to write to the socket 
	 * @param input2 the second object to write to the socket 
     * @param input3 the third object to write to the socket 
     * @param input4the fourth object to write to the socket 
     */
    public void writeToServer(Object input1, Object input2, Object input3, Object input4) {
    	try {
			out.writeObject(input1);
			out.writeObject(input2); 
			out.writeObject(input3); 
			out.writeObject(input4); 
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Starts the client side of the program
     * @param args
     */
	public static void main (String [] args) {
		MainFrame m = new MainFrame();
		Communication c = new Communication("localhost", 1011); 
		MainFrameController v = new MainFrameController(m,c);
		
	}
	
}
