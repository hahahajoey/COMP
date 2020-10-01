package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;


public class PlayerClient implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	int id;
	ScorePad Player_Score;
	ScorePad[] Opponent_Score = new ScorePad[2];
	
	public PlayerClient(String name)
	{
		this.name = name;
		Player_Score = new ScorePad(name);
	}
	
	public static void main(String args[])
	{
		Scanner user_input = new Scanner(System.in);
		
	}
	
	private class Client
	{
		Socket socket;
		private ObjectInputStream In;
		private ObjectOutputStream Out;
		
		public Client()
		{
			try 
			{
				this.socket = new Socket("localhost",8000);
				this.In = new ObjectInputStream(this.socket.getInputStream());
				this.Out = new ObjectOutputStream(this.socket.getOutputStream());
				
				id = this.In.readInt();
				
				System.out.println("Loginin as : " + name);
				send_ScorePad();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		void send_ScorePad()
		{
			try
			{
				Out.writeObject(Player_Score.getScorePad());
				Out.flush();
			}
			catch (IOException e)
			{
				System.out.println("ScordPad sent fail :");
				e.printStackTrace();
			}
		}
	}

}
