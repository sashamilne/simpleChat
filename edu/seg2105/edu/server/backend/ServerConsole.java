package edu.seg2105.edu.server.backend;

import java.io.IOException;
import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;

public class ServerConsole implements ChatIF {
	
	private Scanner fromConsole;
	EchoServer server;
	
	@Override
	public void display(String message) {
		System.out.println("SERVERMSG> " + message);
		

	}
	
	ServerConsole(EchoServer server)
	{
		fromConsole = new Scanner(System.in);
		this.server = server;
	}
	
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        handleMessage(message);
	        
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	private void handleMessage(String message)
	{
		display(message);
		if(message.startsWith("#"))
		{
			handleCommand(message);
			return;
		}
		
		
		server.sendToAllClients("SERVERMSG> " + message);
	}
	
	private void handleCommand(String message)
	{
	//  parse string into words
		String[] words = message.split(" ");
		String command = words[0].substring(1);
		
		switch(command)
		{
		case "quit":
			try {
				server.close();
			}
			catch(IOException e)
			{
				display("Error while closing server");
			}
			System.exit(0);
			break;
		case "stop":
			server.stopListening();
			display("Server has stopped listening");
			break;
				
		case "close":
			server.sendToAllClients("The server has been shut down");
			try {
			server.close();
			}
			catch(IOException e)
			{
				display("Error while closing server");
			}
			break;
		case "getport":
			display(Integer.toString(server.getPort()));
		
		case "setport":
			if(!server.serverActive())
			{
				try {
				server.setPort(Integer.parseInt(words[1]));
				}
				catch (NumberFormatException e)
				{
					display("Invalid port");
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					display("No port provided");
				}
			}
		case "start":
			if(!server.isListening())
			{
				try {
				server.listen();
				} catch(IOException e) {
					display("Error while starting server");
				}
				display("Server has started listening");
			}
			else {
				display("Error: Server is already listening");
			}
			break;
		default:
			display("Unrecognized command");
		}
	}
	
}
