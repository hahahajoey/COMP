package main;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

class TestFC extends TestCase
{
	PlayerClient P1 = new PlayerClient("P1"); 

	//roll 2 skulls, reroll one of them due to sorceress, then go to next round of turn
	@Test
	void Sorceress1() 
	{
		// 0:skull 1:Parrot 2:Coin 3:Monkey 4:Sword 5:Diamond
		// 0:Treasure Chest 1:Captain 2:Sorceress 3:Sea Battle 4:Coin 5:Diamond 6:Monkey Business 7:Skulls
		int[] dice = {0,0,-1,-1,-1,-1,-1,-1};
		int numSorcerss = 1;
		dice = P1.reroll(dice, -1/,1);
		assertNotSame(1, numSorcerss);
	}
	
	//roll no skulls, then next round roll 1 skull and reroll for it, then score 
	@Test
	void Sorceress2() 
	{
		
	}
	
	//roll no skulls, then next round roll 1 skull and reroll for it, then go to next round 
	@Test
	void Sorceress3() 
	{

	}

}