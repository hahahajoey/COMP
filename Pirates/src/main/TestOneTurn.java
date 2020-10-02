package main;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;


//Unit test for hole game
public class TestOneTurn extends TestCase
{
	
	//Test Case for one turn wining for Player 3 the server have to run out side of the testing
	@Test
	public void OneTurn() throws ClassNotFoundException
	{
		
		PlayerClient P1 = new PlayerClient("P1");
		PlayerClient P2 = new PlayerClient("P2");
		PlayerClient P3 = new PlayerClient("P3");
		
		P1.connectClient();
		P2.connectClient();
		P3.connectClient();

		
		P1.Player_Score = P1.Player_Client_Connect.receiveScorePad();
		P2.Player_Score = P2.Player_Client_Connect.receiveScorePad();
		P3.Player_Score = P3.Player_Client_Connect.receiveScorePad();
		
		//1st turn
		P1.Player_Client_Connect.receiveRoundNum();
		P1.Player_Score = P1.Player_Client_Connect.receiveScorePad();
		// 0:skull 1:Parrot 2:Coin 3:Monkey 4:Sword 5:Diamond
		int[] Dices = {0,1,3,4,0,1,4,3};
		int[] chest = {-1,-1,-1,-1,-1,-1,-1,-1};
		P1.Player_Score[0].Score += P1.C_Score(Dices,chest,-1,0);
		P1.Player_Client_Connect.send_ScorePads();
		assertEquals(0, P1.Player_Score[0].Score);
		
		P2.Player_Client_Connect.receiveRoundNum();
		P2.Player_Score = P2.Player_Client_Connect.receiveScorePad();
		int[] Dices2 = {1,1,1,3,4,3,0,4};
		P2.Player_Score[1].Score += P2.C_Score(Dices2,chest,-1,0);
		P2.Player_Client_Connect.send_ScorePads();
		assertEquals(100, P2.Player_Score[1].Score);
		
		P3.Player_Client_Connect.receiveRoundNum();
		P3.Player_Score = P3.Player_Client_Connect.receiveScorePad();
		int[] Dices3 = {4,4,4,4,4,4,4,4};
		P3.Player_Score[2].Score += P3.C_Score(Dices3,chest,-1,0);
		P3.Player_Client_Connect.send_ScorePads();
		assertEquals(4000, P3.Player_Score[2].Score);
		
	}
}
