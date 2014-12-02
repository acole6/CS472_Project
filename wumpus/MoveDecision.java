package wumpus;

import java.awt.Point;

public class MoveDecision implements Decision
{
	private Point location;
	
	public MoveDecision(Point location)
	{
		this.location = location;
	}
	
	@Override
	public Object getDecision() 
	{
		return location;
	}

}
