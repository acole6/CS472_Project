package wumpus;

import java.awt.Point;

public interface Hero 
{
	Direction getDirection();
	
	void setDirection(Direction direction);
	
	Point getLocation();
	
	void setLocation(int row, int col);
	
	boolean hasGold();
	
	void foundGold(boolean found);
	
	Decision makeDecision(boolean[] percepts);
}
