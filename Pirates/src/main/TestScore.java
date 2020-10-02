package main;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class TestScore extends TestCase
{
	PlayerClient P1 = new PlayerClient("P1"); 

	//die with 3 skulls on first roll  -> interface reports death & end of turn  
	@Test
	public void test1() 
	{
		// 0:skull 1:Parrot 2:Coin 3:Monkey 4:Sword 5:Diamond
		int[] dices = {0,0,0,1,2,3,5,5};
		assertEquals(true, P1.dead2(P1.Count(dices, 0)));
	}
	
	//roll 1 skull, 4 parrots, 3 swords, hold parrots, reroll swords, get 2 skulls 1 sword  die
	@Test
	public void test2() 
	{
		int[] dice = {0,1,1,1,1,4,4,4};
		int[] dice2 = {0,1,1,1,1,0,0,4};
		assertEquals(true, P1.dead2(P1.Count(dice2, 0)));
	}

	//roll 2 skulls, 4 parrots, 2 swords, hold parrots, reroll swords, get 1 skull 1 sword  die
	@Test
	public void test3() 
	{
		int[] dice = {0,0,1,1,1,1,4,4};
		int[] dice2 = {0,0,1,1,1,1,0,4};
		assertEquals(true, P1.dead2(P1.Count(dice2, 0)));
	}
	
	//roll 1 skull, 4 parrots, 3 swords, hold parrots, reroll swords, get 1 skull 2 monkeys, reroll 2 monkeys, get 1 skull 1 monkey and die
	@Test
	public void test4() 
	{
		// 0:skull 1:Parrot 2:Coin 3:Monkey 4:Sword 5:Diamond
		int[] dice = {0,1,1,1,1,4,4,4};
		int[] dice2 = {0,1,1,1,1,0,3,3};
		int[] dice3 = {0,1,1,1,1,0,0,3};
		assertEquals(true, P1.dead2(P1.Count(dice3, 0)));
	}
	
	//score first roll with nothing but 2 diamonds and 2 coins and FC is captain (SC 800)
	@Test
	public void test5() 
	{

	}
	
	//
	@Test
	public void test6() 
	{

	}
	
	//
	@Test
	public void test7() 
	{

	}
	
	//
	@Test
	public void test8() 
	{

	}
	
	//
	@Test
	public void test9() 
	{

	}
	
	//
	@Test
	public void test10() 
	{

	}
	
	//
	@Test
	public void test11() 
	{

	}
	
	//
	@Test
	public void test12() 
	{

	}
	
	//
	@Test
	public void test13() 
	{

	}
	
	//
	@Test
	public void test14() 
	{

	}
	
	//
	@Test
	public void test15() 
	{

	}
	
	//
	@Test
	public void test16() 
	{

	}
	
	//
	@Test
	public void test17() 
	{

	}
	
	//
	@Test
	public void test18() 
	{

	}
	
	//
	@Test
	public void test19() 
	{

	}
	
	//
	@Test
	public void test20() 
	{

	}
	
	//
	@Test
	public void test21() 
	{

	}
	
	//
	@Test
	public void test22() 
	{

	}
	
	//
	@Test
	public void test23() 
	{

	}
	
	//
	@Test
	public void test24() 
	{

	}
	
	//
	@Test
	public void test25() 
	{

	}
}
