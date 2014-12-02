package wumpus;

import java.awt.Point;

public abstract class AbstractHero implements Hero
{
	protected Direction direction;
	protected Point location;
	protected boolean foundGold;
	
	protected AbstractHero(Direction direction)
	{
		this.direction = direction;
		location = new Point(0, 0);
		foundGold = false;
	}
	
	@Override
	public Direction getDirection() 
	{
		return direction;
	}

	@Override
	public void setDirection(Direction direction) 
	{
		this.direction = direction;
	}

	@Override
	public Point getLocation() 
	{
		return location;
	}

	@Override
	public void setLocation(int row, int col) 
	{
		location = new Point(row, col);
	}

	@Override
	public boolean hasGold() 
	{
		return foundGold;
	}

	@Override
	public void foundGold(boolean found) 
	{
		foundGold = found;
	}
	
	public abstract Decision makeDecision(boolean[] perceptes);
}
