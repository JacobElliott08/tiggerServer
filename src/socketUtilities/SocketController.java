package socketUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import serverConnection.ServerThread;

public class SocketController 
{
	List<ServerThread> sockets;
	
	
	public SocketController()
	{
		sockets = Collections.synchronizedList(new ArrayList<ServerThread>());
	}
	
	public List<ServerThread> getConnections()
	{
		return sockets;
	}
	
	
}
