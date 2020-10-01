package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;


public class PlayerClient implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	int id;
	ScorePad[] Player_Score = new ScorePad[3];
	static Client Player_Client_Connect;
	static Scanner input = new Scanner(System.in);;
	
	public PlayerClient(String name)
	{
		this.name = name;
	}
	
	public static void main(String args[])
	{
		System.out.print("Please enter the name :");
		String name = input.next();
		PlayerClient player = new PlayerClient(name);
		player.connectClient();
		player.startGame();
		player.returnWin();
		input.close();
	}
	
	public void connectClient()
	{
		Player_Client_Connect = new Client();
	}
	
	public void startGame()
	{
		Player_Score = Player_Client_Connect.receiveScorePad();
		while(true)
		{
			int roundNum = Player_Client_Connect.receiveRoundNum();
			if (roundNum < 0)
			{break;}
			System.out.println("Round Number " + roundNum);
			Player_Score = Player_Client_Connect.receiveScorePad();
			Player_Score = Round();
			Player_Client_Connect.send_ScorePads();
		}
	}
	
	//game play
	public ScorePad[] Round()
	{
		printPlayerScores(id);
		// 0:skull 1:Parrot 2:Coin 3:Monkey 4:Sword 5:Diamond
		int[] dices = new int[8];
		Random r = new Random();
		// 0:Treasure Chest 1:Captain 2:Sorceress 3:Sea Battle 4:Coin 5:Diamond 6:Monkey Business 7:Skulls
		int FC = r.nextInt(8);
		dices = rollDies();
		int[] types = new int[6];
		int FC_counter = printFC(FC);
		int[] Chest = new int[8];
		for(int i=0; i<Chest.length; i++)
		{Chest[i] = -1;}
		types = CountType(dices, FC, FC_counter);
		
		//loop for game
		while(true)
		{
			System.out.println(Count(dices,0));
			if(dead(types[0]))
			{
				System.out.println("You are dead");
				for(int i=0; i<Chest.length; i++)
				{Chest[i] = 0;}
				break;
			}
			
			if (FC == 0)
			{
				
			}
			
			printDices(dices);
			System.out.print("\nDo you want to reroll?(Y/N) : ");
			if(input.next().equals("Y"))
			{
				dices = reroll(dices, FC);
				types = CountType(dices, FC, FC_counter);
			}
			else
			{break;}
		}
		
		return Player_Score;
	}
	
	//Treasure chest
	public int[] MoveChest(int[] Dices, int[] Chest)
	{
		System.out.println("You have these dice in chest");
		printDices(Chest);
		System.out.println("And you have these dice");
		printDices(Dices);
		System.out.println("Please entry the dice number with the one you want to reroll :(1,2,3....)");
		String[] re = (input.next()).replaceAll("\\s", "").split(",");
		
		
		return Chest;
	}
	
	
	
	//reroll with the user input
	public int[] reroll(int[] Dices,int FC)
	{
		System.out.println("Please entry the dice number with the one you want to reroll :(1,2,3....)");
		String[] re = (input.next()).replaceAll("\\s", "").split(",");
		
		int maxroll = 6;
		Random r =new Random();
		for(String s: re)
		{
			if(maxroll == 0)
			{break;}
			int redice = Integer.parseInt(s) - 1;
			if(Dices[redice] == -1)
			{continue;}
			if((Dices[redice] == 0) && (FC != 2))
			{continue;}
			else if((Dices[redice] == 0) && (FC == 2))
			{
				Dices[redice] = r.nextInt(6);
				maxroll--;
				FC = 0;
				System.out.println("Sorceress used");
			}
			else
			{
				Dices[redice] = r.nextInt(6);
				maxroll--;
			}
		}
		return Dices;
	}
	
	//death boolean
	public boolean dead(int numSkull)
	{return numSkull ==3;}
	
	//count each type of dice
	public int[] CountType(int[] dices,int FC, int FC_counter)
	{
		int[] types = new int[6];
		
		for(int i = 0; i < 6; i++)
		{
			types[i] = Count(dices, i);
		}
		
		if(FC == 4)
		{types[2] += 1;}
		else if(FC == 5)
		{types[5] += 1;}
		else if(FC == 6)
		{
			types[3] += types[1];
			types[1] = 0;
		}
		else if(FC == 7)
		{types[1] += FC_counter;}
		
		return types;
	}
	
	//count single type of dice
	public int Count(int[] Dices,int item)
	{
		int counter = 0;
		for(int i = 0;i < 8;i++)
		{
			if(Dices[i] == item)
			{counter++;}
		}
		return counter;
	}
	
	public void printDices(int[] Dices)
	{
		System.out.print("Here are the dies : ");
		for(int i = 0 ; i < 8; i++)
		{
			System.out.print((i+1)+". ");
			switch(Dices[i])
			{
			case 0:
				System.out.print("Skull "); break;
			case 1:
				System.out.print("Parrot "); break;
			case 2:
				System.out.print("Coin "); break;
			case 3:
				System.out.print("Monkey "); break;
			case 4:
				System.out.print("Sword "); break;
			case 5:
				System.out.print("Diamond "); break;
			}
		}
		System.out.println();
	}
	
	public int printFC(int FC)
	{
		Random r = new Random();
		System.out.print("Here are the Fortune Card : ");
		switch(FC)
		{
		case 0:
			System.out.print("Treasure Chest\n");
			return 0;
		case 1:
			System.out.print("Captain\n");
			return 0;
		case 2:
			System.out.print("Sorceress\n");
			return 0;
		case 3:
			System.out.print("Sea Battle\n");
			return r.nextInt(3)+2;
		case 4:
			System.out.print("Gold\n");
			return 1;
		case 5:
			System.out.print("Diamond\n");
			return 1;
		case 6:
			System.out.print("Monkey Business\n");
			return 0;
		 default :
			System.out.print("Skulls\n");
			return r.nextInt(2)+1;
		}
	}
	
	public int[] rollDies()
	{
		int[] Dies = new int[8];
		Random r = new Random();
		for(int i = 0; i < 8; i++)
		{Dies[i] = r.nextInt(6);}
		return Dies;
	}
	
	public void returnWin()
	{
		
	}
	
	public void printPlayerScores(int id) 
	{
		switch(id-1)
		{
		case 0:
			Player_Score[0].printScore();
			Player_Score[1].printScore();
			Player_Score[2].printScore();
			break;
		case 1:
			Player_Score[1].printScore();
			Player_Score[0].printScore();
			Player_Score[2].printScore();
			break;
		default :
			Player_Score[2].printScore();
			Player_Score[0].printScore();
			Player_Score[1].printScore();
			break;
		}
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
				socket = new Socket("localhost",3333);
				Out = new ObjectOutputStream(socket.getOutputStream());
				In = new ObjectInputStream(socket.getInputStream());
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
				Out.writeObject(new ScorePad(name));
				Out.flush();
			}
			catch (IOException e)
			{
				System.out.println("ScordPad sent fail :");
				e.printStackTrace();
			}
		}
		
		void send_ScorePads()
		{
			try
			{
				Out.writeObject(Player_Score);
				Out.flush();
			}
			catch (IOException e)
			{
				System.out.println("ScordPad sent fail :");
				e.printStackTrace();
			}
		}
		
		ScorePad[] receiveScorePad()
		{
			ScorePad[] Score_Pad = new ScorePad[3];
			try {
				ScorePad temp = (ScorePad) In.readObject();
				Score_Pad[0] = temp;
				temp = (ScorePad) In.readObject();
				Score_Pad[1] = temp;
				temp = (ScorePad) In.readObject();
				Score_Pad[2] = temp;
				return Score_Pad;

			} catch (IOException e) {
				System.out.println("Score pad is missing from sever");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("class not found");
				e.printStackTrace();
			}
			return Score_Pad;
		}
		
		int receiveRoundNum()
		{
			try
			{
				return In.readInt();
			}
			catch(IOException e)
			{
				System.out.println("Round num is missing from server :");
				e.printStackTrace();
			}
			return 0;
		}
	}

}
