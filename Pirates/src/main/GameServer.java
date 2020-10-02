package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Server[] Servers = new Server[3];
	ScorePad[] ScorePads = new ScorePad[3];
	int turnNum;
	
	int num_Player;
	
	ServerSocket serversocket;
	
	public static void main(String args[]) throws Exception
	{
		GameServer gameserver = new GameServer();
		gameserver.Connect();
		gameserver.Loop();
	}
	
	public GameServer()
	{
		num_Player = 0;
		try
		{
			System.out.println("Server created");
			this.serversocket = new ServerSocket(3333);
		}
		catch (IOException e)
		{
			System.out.println("Server starts failed :");
			e.printStackTrace();
		}
	}
	
	public void Connect() throws ClassNotFoundException
	{
		try
		{
			System.out.println("Connecting......");
			while (num_Player < 3)
			{
				Socket socket = serversocket.accept();
				num_Player++;
				
				Server server = new Server(socket,num_Player);
				
				// send the player id
				server.Out.writeInt(server.id);
				server.Out.flush();
				
				ScorePad get_ScorePad = (ScorePad) server.In.readObject();
				System.out.println("Player " + server.id + " " + get_ScorePad.name);
				//add to list
				ScorePads[num_Player-1] = get_ScorePad;
				Servers[num_Player-1] = server;
			}
			
			for(int i = 0; i < 3; i++)
			{
				Thread temp = new Thread(Servers[i]);
				temp.start();
			}
		}
		catch (IOException ex)
		{				
			ex.printStackTrace();
	    }
		
	}
	
	//keep the game running
	public void Loop()
	{
		Servers[0].sendScorePad(ScorePads);
		Servers[1].sendScorePad(ScorePads);
		Servers[2].sendScorePad(ScorePads);
		
		turnNum = 0;
		int countWin = 1;
		
		while(countWin > 0)
		{
			turnNum++;
			
			//Send rounds
			System.out.println();
			System.out.println("Turns : " + turnNum);
			Servers[0].sendTurns(turnNum);
			Servers[0].sendScorePad(ScorePads);
			ScorePads = Servers[0].receiveScore();
			System.out.println("Player 1 score is : " + ScorePads[0].Score);
			
			Servers[1].sendTurns(turnNum);
			Servers[1].sendScorePad(ScorePads);
			ScorePads = Servers[1].receiveScore();
			System.out.println("Player 2 score is : " + ScorePads[0].Score);
			
			Servers[2].sendTurns(turnNum);
			Servers[2].sendScorePad(ScorePads);
			ScorePads = Servers[2].receiveScore();
			System.out.println("Player 3 score is : " + ScorePads[0].Score);
			
			if(ScorePads[0].Score >=6000 || ScorePads[1].Score >=6000 || ScorePads[2].Score >=6000)
			{countWin--;}
			else if(countWin < 1)
			{countWin++;}
		}
		if(ScorePads[0].Score >=6000)
		{
			System.out.println("Player 1 win");
			ScorePads[0].win = true;
		}
		if(ScorePads[1].Score >=6000)
		{
			System.out.println("Player 2 win");
			ScorePads[1].win = true;
		}
		if(ScorePads[2].Score >=6000)
		{
			System.out.println("Player 3 win");
			ScorePads[2].win = true;
		}
		Servers[0].sendTurns(-1);
		Servers[1].sendTurns(-1);
		Servers[2].sendTurns(-1);
		
	}
	
	
	//Single Server class use to connect each player server
	public class Server implements Runnable
	{
		private Socket Socket;
		private ObjectInputStream In;
		private ObjectOutputStream Out;
		private int id;
		
		public Server(Socket socket, int id)
		{
			this.Socket = socket;
			this.id = id;
			try
			{
				In = new ObjectInputStream(this.Socket.getInputStream());
				Out = new ObjectOutputStream(this.Socket.getOutputStream());
				
			}
			catch(IOException e)
			{
				System.out.println("Server connection error : fail ot connect ");
				e.printStackTrace();
			}
		}
		
		//keep the server running to connect player client
		public void run()
		{
			try
			{
				while(true);
			}
			catch(Exception e)
			{
				System.out.println("Server" + id +" Crashed: ");
				e.printStackTrace();
			}
		}
		
		public void sendTurns(int t)
		{
			try
			{
				Out.writeInt(t);
				Out.flush();
			}
			catch (Exception e)
			{
				System.out.println("Turns not sended");
				e.printStackTrace();
			}
		}
		
		public void sendScorePad(ScorePad[] SP)
		{
			try
			{
				for(ScorePad S : SP)
				{
					Out.writeObject(S);
					Out.flush();
				}
			}
			catch (IOException e)
			{
				System.out.println("Score pad not sent");
				e.printStackTrace();
			}
		}
		
		public ScorePad[] receiveScore()
		{
			try
			{
				ScorePad[] temp = (ScorePad[]) In.readObject();
				return temp;
			}
			catch(Exception e)
			{
				System.out.println("Score Pad is missing from client :");
				e.printStackTrace();
			}
			return ScorePads;
		}
	}
}


