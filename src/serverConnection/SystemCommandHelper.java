package serverConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import serverUtilities.ServerCommands;
import socketUtilities.SocketController;

public class SystemCommandHelper extends Thread{

    BufferedReader br;
    SocketController controller;
    ServerConnect instance;
     
    public SystemCommandHelper(SocketController sc, ServerConnect instance)
    {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.instance = instance;
        this.controller = sc;
    }
    
    public void run()
    {   
        while(true)
        {
            checkForSysInput(br);
        }
    }

    private void checkForSysInput(BufferedReader bInput) 
    {
        String input = null;
        String [] commands;
        try
        {
            input = bInput.readLine();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        commands = input.split(" ");
        ServerCommands.doCommand(commands, instance);
    }  


}