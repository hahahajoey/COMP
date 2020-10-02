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
	Client Player_Client_Connect;
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
		int numSorcerss = 0;
		for(int i=0; i<8; i++)
		{Chest[i] = -1;}
		types = CountType(dices, FC, FC_counter);
		
		if(dead(types[0]))
		{
			System.out.println("You died by the first roll");
			return Player_Score;
		}
		int Skullnum = 0;
		
		//loop for game
		while(true)
		{
			if(dead2(types[0]))
			{
				System.out.println("You are dead");
				for(int i=0; i<Chest.length; i++)
				{dices[i] = 0;}
				break;
			}
			
			if (FC == 0)
			{
				System.out.print("\nDo you want to put dices into chest?(Y/N) : ");
				if(input.next().equals("Y"))
				{MoveChest(dices, Chest);}
				System.out.print("\nDo you want to get dices from chest?(Y/N) : ");
				if(input.next().equals("Y"))
				{GetChest(dices, Chest);}
			}
			else if (FC==2)
			{numSorcerss++;}
			else if(FC == 3)
			{System.out.println("You are in SeaBattle, try to get " + FC_counter + " Swords.");}
			else if(FC == 7)
			{System.out.println("You have get " + FC_counter + " Skulls from FC.");}
			else if(types[0] >= 4)
			{
				if(Skullnum == types[0])
				{break;}
				System.out.print("You have enter the island of skulls");
				Skullnum = types[0];
			}
			
			printDices(dices);
			System.out.print("\nDo you want to reroll?(Y/N) : ");
			if(input.next().equals("Y"))
			{
				dices = reroll(dices, FC,numSorcerss);
				types = CountType(dices, FC, FC_counter);
			}
			else
			{break;}
		}
		
		int Score_change = C_Score(dices, Chest, FC, FC_counter);
		System.out.println("You get " + Score_change);
		
		if(Score_change > 0 || FC == 3)
		{
			Player_Score[id-1].Score += Score_change;
			if(Player_Score[id-1].Score < 0 )
			{Player_Score[id-1].Score = 0;}
		}
		else if(Score_change < 0)
		{
			for(int i=0; i<3; i++)
			{
				if(i != (id-1))
				{
					Player_Score[i].Score -= Score_change;
					if(Player_Score[i].Score < 0 )
					{Player_Score[i].Score = 0;}
				}
			}
		}
		return Player_Score;
	}
	
	//Get Score
	public int C_Score(int[] Dices, int[] Chest,int FC, int FC_counter) 
	{
		int Score = 0;
		int time = 1;
		int[] types = CountType(Dices, FC, FC_counter);
		if(FC == 0)
		{
			for(int i=0; i<8; i++)
			{
				if(Chest[i] != -1)
				{Dices[i] = Chest[i];}
			}
		}
		else if(FC == 1)
		{time = 2;}
		else if(FC == 3)
		{
			if(types[4] == FC_counter)
			{
				if(FC_counter == 2)
				{Score += 300;}
				else if(FC_counter == 3)
				{Score += 500;}
				else
				{Score += 1000;}
				types[4] = 0;
			}
			else
			{
				if(FC_counter == 2)
				{return -300;}
				else if(FC_counter == 3)
				{return -500;}
				else
				{return -1000;}
			}
		}
		else if(types[0] > 3)
		{return (types[0] * 100 * -1);}
		
		Score += (types[2] * 100 + types[5] * 100);
				
		for(int i=1; i<6; i++)
		{
			if(types[i] >= 8 )
			{
				Score += 4000;
				types[i] = 0;
			}
			else if(types[i] == 7 )
			{
				Score += 2000;
				types[i] = 0;
			}
			else if(types[i] == 6 )
			{
				Score += 1000;
				types[i] = 0;
			}
			else if(types[i] == 5 )
			{
				Score += 500;
				types[i] = 0;
			}
			else if(types[i] == 4 )
			{
				Score += 200;
				types[i] = 0;
			}
			else if(types[i] == 3 )
			{
				Score += 100;
				types[i] = 0;
			}
		}
		Score += Full_Chest(types, Dices);
		return Score * time;
	}
	
	//give 500 if Full_chest
	public int Full_Chest(int[] types, int[] dices)
	{
		if(Count(dices,0) > 0 || Count(dices,-1) >0)
		{return 0;}
		else if((types[1]>0) || (types[3]>0) || (types[4]>0))
		{return 0;}
		else
		{return 500;}
	}
	
	//Treasure chest
	public void MoveChest(int[] Dices, int[] Chest)
	{
		System.out.println("You have these dice in chest");
		printDices(Chest);
		System.out.println("And you have these dice");
		printDices(Dices);
		System.out.println("Please entry the dice number with the one you want to put in chest :(1,2,3....)");
		String[] re = (input.next()).replaceAll("\\s", "").split(",");
		
		for(int i=0; i<re.length; i++)
		{
			int put = Integer.parseInt(re[i]) - 1;
			if(Dices[put] < 1)
			{continue;}
			Chest[put] = Dices[put];
			Dices[put] = -1;
		}
	}
	
	public void GetChest(int[] Dices, int[] Chest)
	{
		System.out.println("You have these dice in chest");
		printDices(Chest);
		System.out.println("And you have these dice");
		printDices(Dices);
		System.out.println("Please entry the dice number with the one you want get out of chest :(1,2,3....)");
		String[] re = (input.next()).replaceAll("\\s", "").split(",");
		
		for(int i=0; i<re.length; i++)
		{
			int put = Integer.parseInt(re[i]) - 1;
			if(Chest[put] < 1)
			{continue;}
			Dices[put] = Chest[put];
			Chest[put] = -1;
		}
	}
	
	
	//reroll with the user input
	public int[] reroll(int[] Dices,int FC, int numSorcerss)
	{
		System.out.println("Please entry the dice number with the one you want to reroll :(1,2,3....)");
		String[] re = (input.next()).replaceAll("\\s", "").split(",");
		while(re.length < 2)
		{
			System.out.println("You cant roll less then two dice. Cheating!");
			re = (input.next()).replaceAll("\\s", "").split(",");
		}
		
		Random r =new Random();
		for(String s: re)
		{
			int redice = Integer.parseInt(s) - 1;
			if(Dices[redice] == -1)
			{continue;}
			if((Dices[redice] == 0) && (numSorcerss == 0))
			{continue;}
			else if((Dices[redice] == 0) && (numSorcerss > 0))
			{
				numSorcerss--;
				Dices[redice] = r.nextInt(6);
				System.out.println("Sorceress used");
			}
			else
			{Dices[redice] = r.nextInt(6);}
		}
		return Dices;
	}
	
	//death boolean
	public boolean dead2(int numSkull)
	{return numSkull == 3;}
	
	public boolean dead(int numSkull)
	{return numSkull >= 3;}
	
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
			System.out.print("Coin\n");
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
	
	public boolean returnWin()
	{
		if(Player_Score[id-1].Score >= 6000)
		{
			System.out.println("You win");
			return true;
		}
		else
		{
			System.out.println("You lost");
			return false;
		}
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
	
	public class Client
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
