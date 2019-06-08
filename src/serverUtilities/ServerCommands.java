package serverUtilities;
import serverConnection.ServerConnect;

public class ServerCommands
{
	public static void doCommand(String [] args, ServerConnect server)
	{
		switch(args[0])
		{
			case "who":
			{
				server.displayNames();
				break;
			}
			case "shutDown":
			{
				
				break;
			}
			case "kick":
			{
				String name = "";
				if(args.length >= 2)
				{
					name = args[1];
				}
				server.kickUser(name,false);
				break;
			}
			case "msg":
			{
				String name = "";
				String mesg;
				if(args.length >= 2)
				{
					name = args[1];
				}
				mesg = stringCat(args,2);
				server.message(name,mesg);
				break;
			}
			case "bm":
			{
				server.broadcastMessage(stringCat(args,1));
				break;
			}
			default:
			{
				break;
			}
		}
	}
	
	
	private static String stringCat(String [] theString, int start)
	{
		String toReturn = "";
		for(int i = start; i < theString.length; i ++)
		{
			toReturn += theString[i];
		}
		return toReturn;
	}
}	
