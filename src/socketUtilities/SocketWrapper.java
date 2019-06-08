package socketUtilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import serverConnection.*;

public class SocketWrapper
{
	Socket socket;
	
	public SocketWrapper(Socket socket, ServerConnect sc)
	{
		this.socket = socket;
	}
	
	public Socket getSocket()
	{
		return socket;
	}
	
	public boolean destroy()
	{
		try 
		{
			getSocket().getOutputStream().write("Kick".getBytes());
			getSocket().close();
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	
	}
	
	
	public boolean sendMessage(String message)
	{
		try {
			getSocket().getOutputStream().write( (message+"\n").getBytes() );
			getSocket().getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
 
}

