// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  private String loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginId, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
    openConnection();
    
    try
    {
      sendToServer("#login " + loginId);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send login message to server.  Terminating client.");
      quit();
    }
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    
	// check if message is a command
	if(message.startsWith("#"))
	{
		handleCommand(message);
		return;
	}
		
	try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String message)
  {
	//  parse string into words
			String[] words = message.split(" ");
			String command = words[0].substring(1);
			
			switch (command)
			{
				case "quit":
					quit();
					break;
				case "logoff":
					try {
						closeConnection();
					}
					catch(IOException e) {
						clientUI.display("Failed to close connection. Exception : " + e.getMessage());
					}
					break;
				case "sethost":
					if(isConnected())
					{
						clientUI.display("Cannot change hostname while client is connected");
					}
					else
					{
						try {
							setHost(words[1]);
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							clientUI.display("No hostname provided");
						}
					}
					break;
				case "setport":
					if(isConnected())
					{
						clientUI.display("Cannot change port while client is connected");
					}
					else
					{
						try {
							setPort(Integer.parseInt(words[1]));
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							clientUI.display("No port provided.");
						}
						catch (NumberFormatException e)
						{
							clientUI.display("Invalid port provided.");
						}
					}
					break;
				case "getport":
					clientUI.display(Integer.toString(getPort()));
					break;
				case "gethost":
					clientUI.display(getHost());
					break;
				case "login":
					try {
						openConnection();
					}
					catch (IOException e)
					{
						clientUI.display("Failed to open connection. Exception : " + e.getMessage());
					}
					break;
				default:
					clientUI.display("Unrecognized command.");
			
			}
	  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  @Override
  protected void connectionClosed()
  {
  
	  clientUI.display("Connection with " + getHost() + " has been shut closed. Terminating");
	  System.exit(0);
	  
  }
  
  @Override
  protected void connectionException(Exception exception)
  {
	  clientUI.display("Exception in connection with " + getHost() + ". Exception : " 
			  + exception +"\nTerminating.");
	  System.exit(0);
  }
  
  
}
//End of ChatClient class
