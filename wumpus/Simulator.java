package wumpus;

import java.awt.Point;

public class Simulator
{ 
	private GameBoard board;
	
	public Simulator(GameBoard board)
	{
		this.board = board;
	}
	
	public boolean gameOver()
	{
		return board.gameOver();
	}
	
	public void move()
	{	
		Hero hero = board.getHero();
		Point location = hero.getLocation();
		board.processHeroDecision(hero.makeDecision(board.getPercepts()));
		System.out.println("Hero location: " + (int) hero.getLocation().getX() + ", " + (int) hero.getLocation().getY());
	} 
}
