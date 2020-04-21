package server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The server of the program
 * @author Madhu Selvaraj, Navjot Singh
 *
 */
public class Server {
	
	/**
	 * Used to accept connections 
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Thread pool
	 */
	private ExecutorService pool;
	
	/**
	 * Constructs a server with socket 1011, and with a thread pool
	 */
	public Server() {
		try {
			serverSocket = new ServerSocket(1011);
			pool = Executors.newCachedThreadPool();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Communicates with the class that communicates with the client
	 * @param db The database that will be sent to ServerCommunicatin
	 */
	public void communicateWithServer(DBController db) {
		System.out.println("Server is running");
		try {
			for(;;) {
				ServerCommunication sc = new ServerCommunication(serverSocket.accept(),db);
				System.out.println("Connected to a new client");
				pool.execute(sc); //ServerCommunication implements runnable
			}
		} catch (IOException e) {
			pool.shutdown();
			e.printStackTrace();
		}
	}
}
