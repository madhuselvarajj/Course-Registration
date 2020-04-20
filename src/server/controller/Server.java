package server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//needs documentation
public class Server {
	
	private ServerSocket serverSocket;
	
	private ExecutorService pool;
	
	public Server() {
		try {
			serverSocket = new ServerSocket(1011);
			pool = Executors.newCachedThreadPool();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
