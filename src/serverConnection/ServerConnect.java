package serverConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.io.PrintWriter;

import socketUtilities.SocketController;
import socketUtilities.SocketWrapper;

public class ServerConnect 
{
	SocketController socketConnections;
	ServerSocket serverSocket = null;
    public static void main(String args[])
    {
    	new ServerConnect();
    }
    
    
    public ServerConnect()
    {
       
        System.out.println("Server Online");

        try
        {
            serverSocket = new ServerSocket(8000); 
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Server error");

        }

        socketConnections = new SocketController();
        
        SystemCommandHelper in = new SystemCommandHelper(socketConnections,this);
        in.start();
        
        while(true)
        {
            
            try
            {
            	ServerThread socketControl = new ServerThread(serverSocket.accept(), this);
            	socketControl.start();
            	//SocketWrapper socket = new SocketWrapper(serverSocket.accept(), this);
                
            	//socketConnections.getConnections().add(socket);
                System.out.println("New connection established.");

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public boolean kickUser(String userName, boolean nice)
    {
    	synchronized(socketConnections.getConnections())
    	{
            for(ServerThread s : socketConnections.getConnections())
            {
                if(s.getUserName().equalsIgnoreCase(userName))
                {
                	if(!nice)
                		s.getSocketWrapper().sendMessage("You've been kicked from the server.");
                    socketConnections.getConnections().remove(s);
                    return s.getSocketWrapper().destroy();
                }
            }	
    	}
        return false;

    }
    
    public boolean kickUser(int id)
    {
    	synchronized(socketConnections.getConnections())
    	{
	    	for(ServerThread s : socketConnections.getConnections())
	    	{
	    		if(s.getSocketWrapper().getSocket().getPort() == id)
	    		{
	    			socketConnections.getConnections().remove(s);
	    			return s.getSocketWrapper().destroy();
	    		}
	    	}
    	}
    	
    	return false;
    }
    
    public void sendNames(PrintWriter os)
    {
    	synchronized(socketConnections.getConnections())
    	{
	        for(ServerThread s : socketConnections.getConnections())
	        {
	            os.write("Name : "+s.getUserName()+"\n");
	        }
    	}
    }

    public void displayNames()
    {
    	synchronized(socketConnections.getConnections())
    	{
	    	for(ServerThread s: socketConnections.getConnections())
	    	{
	    		System.out.println(s.getSocketWrapper().getSocket().getPort());
	    	}
    	}
    }

    public void message(String name, String mesg) 
    {
    	synchronized(socketConnections.getConnections())
    	{
	        for(ServerThread s : socketConnections.getConnections())
	        {
	            if(s.getUserName().equalsIgnoreCase(name))
	            {
	            	s.getSocketWrapper().sendMessage(mesg);
	            }
	        }
    	}
    }

	public void message(int id, String mesg) 
	{	
    	for(ServerThread s : socketConnections.getConnections())
    	{
    		if(s.getSocketWrapper().getSocket().getPort() == id)
    		{
    			s.getSocketWrapper().sendMessage(mesg);
    		}
    	}
	}

    public void broadcastMessage(String mesg)
    {
        for(ServerThread s : socketConnections.getConnections())
        {
            s.getSocketWrapper().sendMessage(mesg);
        }
    }
	
	
	public void shutDownServer()
	{
		try 
		{
			serverSocket.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public boolean userExists(String userName) 
	{
		synchronized(socketConnections.getConnections())
		{
			for(ServerThread s : socketConnections.getConnections())
			{
				if(s.getUserName().equalsIgnoreCase(userName))
				{
					return true;
				}
			}
		}
		return false;
	}
    
  

}







