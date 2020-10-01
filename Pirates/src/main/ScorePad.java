package main;

import java.io.Serializable;

public class ScorePad implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	int Score;
	boolean win;
	
	//initialize the ScoreCard
	public ScorePad(String name)
	{
		this.name = name;
		Score = 0;
		win = false;
	}

	
	//print out the scores for player to view
	public void printScore()
	{
		System.out.println();
		System.out.println("Score for player : " + name +" " + Score);
	}
	
	//return this object
	public ScorePad getScorePad()
	{
		return this;
	}
}
