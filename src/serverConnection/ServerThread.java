package serverConnection;

import java.io.*;
import java.net.Socket;

import socketUtilities.SocketWrapper;

public class ServerThread extends Thread
{  

    String line = null;
    String userName = null;
    BufferedReader is = null;
    PrintWriter os = null;

    SocketWrapper socket = null;
    ServerConnect server;

    public ServerThread(Socket s, ServerConnect sc)
    {
        this.socket = new SocketWrapper(s,sc);
        server = sc;
    }

    public void run() 
    {
        try
        {
            is = new BufferedReader(new InputStreamReader(socket.getSocket().getInputStream()));
            os = new PrintWriter(socket.getSocket().getOutputStream());
        }
        catch(IOException e)
        {
            System.out.println("IO error in server thread");
        }
        
        try 
        {
            line = is.readLine();
            System.out.println("here");
            while(line.compareTo("QUIT")!= 0)
            {
                
                System.out.println("Input from the Client : "+line);
                if(!handleInput(line))
                {
                   //System.out.println("Response to Client  :  "+line);
                    server.broadcastMessage(userName+" - "+line);
                }

                os.flush();
                line = is.readLine();
                
            }   
        } 
        catch (IOException e)
        {

            line = this.getName(); 
            System.out.println("IO Error/ Client "+line+" terminated abruptly");
        }
        catch(NullPointerException e)
        {
            line = this.getName();
            System.out.println("Client "+line+" Closed");
        }

        finally
        {    
            try
            {
                System.out.println("Connection Closing..");
                if (is != null)
                {
               
                    is.close(); 
                    System.out.println("Socket Input Stream Closed");
                }

                if(os != null)
                {
                	os.write("Quit");
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (socket.getSocket() != null)
                {
                    socket.getSocket().close();
                    System.out.println("Socket Closed");
                }

            }
            catch(IOException ie)
            {
                System.out.println("Socket Close Error");
            }
        }


    }

    public String getUserName()
    {
        return userName;
    }
    
    public PrintWriter getPrintStream()
    {
    	return os;
    }

    private boolean handleInput(String line)
    {
        if(line.equalsIgnoreCase("USERNAME"))
        {
            if(userName == null)
            {
                try
                {
                    userName = is.readLine();
                    if(!server.userExists(userName))
                    {
                    	//Now we can add them into the list...
                    	server.socketConnections.getConnections().add(this);
                    	System.out.println("Setting the username to : "+userName);	
                    }
                    else
                    {
                    	os.write("invalid username\n");
                    	os.flush();
                    	userName = null;
                    }
                    
                }catch(Exception e){e.printStackTrace();}
                return true;
            }
        }
        else if(line.equalsIgnoreCase("COMMAND"))
        {
            try
            {
                String command = is.readLine();
                if(command != null)
                {
                    String [] commands = command.split(" ");  
                    doCommand(commands);
                }
            }catch(Exception e){e.printStackTrace();}
            return true;
        }
        return false;
    }

    private void doCommand(String [] commands)
    {
        if(commands[0].equalsIgnoreCase("who"))
        {
            server.sendNames(os);
            os.flush();
        }

        if(commands[0].equalsIgnoreCase("kick"))
        {
            String name = joinString(commands,1);
            server.kickUser(name, false);
            
        }

        if(commands[0].equalsIgnoreCase("dm"))
        {
            String name = commands[1];
            String message = joinString(commands,2);
            server.message(name,"Direct("+userName+") "+message);
        }
        
        if(commands[0].equalsIgnoreCase("quit"))
        {
        	server.kickUser(this.getUserName(), true);
        }

        if(commands[0].equalsIgnoreCase("help"))
        {
            os.println("!who - Displays a list of all the people who are on the server. ");
            os.println("!kick name - Removes 'name' from the server.");
            os.println("!dm name message - Directly messages the user by the name of 'name'.");
            os.flush();
        }
    }




    
    public String joinString(String [] arr, int start)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = start; i < arr.length; i ++)
        {
            builder.append(arr[i]);
            if(i != arr.length - 1)
            {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
    
    
    public SocketWrapper getSocketWrapper()
    {
    	return socket;
    }
}