package wumpus;

import java.awt.Point;

/**
 * Abstract hero agent
 * @author Alex
 *
 */
public abstract class AbstractHero implements Hero
{
	protected Direction direction;
	protected Point location;
	protected boolean foundGold;
	protected boolean alive;
	
	/**
	 * Abstract hero constructor
	 * @param direction
	 */
	protected AbstractHero(Direction direction)
	{
		this.direction = direction;
		location = new Point(0, 0);
		foundGold = false;
		alive = true;
	}
	
	/**
	 * Gets the direction the hero is facing (up, down, left, right).
	 * @return
	 * 		The direction the hero is facing
	 */
	@Override
	public Direction getDirection() 
	{
		return direction;
	}

	/**
	 * Sets the direction the hero is facing.
	 * @param direction
	 * 		The direction the hero should face.
	 */
	@Override
	public void setDirection(Direction direction) 
	{
		this.direction = direction;
	}

	/**
	 * Gets the location of the hero on the board.
	 * @return
	 * 		The location of the hero
	 */
	@Override
	public Point getLocation() 
	{
		return location;
	}

	/**
	 * Sets the location of the hero on the board.
	 * @param row
	 * 		The row on the board
	 * @param col
	 * 		The Column on the board
	 */
	@Override
	public void setLocation(int row, int col) 
	{
		location = new Point(row, col);
	}

	/**
	 * Checks if the hero is carrying gold.
	 * @return
	 * 		True if the hero has gold; otherwise false
	 */
	@Override
	public boolean hasGold() 
	{
		return foundGold;
	}

	/**
	 * Updates if the hero has found gold or not.
	 * @param found
	 * 		True if the hero has found gold and false if the hero has not
	 */
	@Override
	public void foundGold(boolean found) 
	{
		foundGold = found;
	}
	
	/**
	 * Checks to see if the hero is alive
	 * @return
	 * 		True if alive; otherwise false
	 */
	@Override
	public boolean isAlive()
	{
		return alive;
	}
	
	/**
	 * The hero makes a decision about what to do based on the hero's location on
	 * the board and the percepts passed in. The hero can decide to move forward, turn left
	 * or right, climb out of the cave or grab gold.
	 * @param percepts
	 * 		The percepts that the hero agent considers when making a decision
	 * @return
	 * 		The decision made by the hero
	 */
	public abstract Decision makeDecision(boolean[] perceptes);
	
	/**
	 * Resets the hero back to his initial state
	 */
	public abstract void reset();
	
	/**
	 * Marks that the hero was killed in the cave and passes in the location
	 * the hero was killed and what killed the hero (Wumpus or pit).
	 * @param row
	 * 		The row 
	 * @param col
	 * 		The column
	 * @param causeOfDeath
	 * 		What killed the hero (Wumpus or pit)
	 */
	public abstract void killed(int row, int col, Status causeOfDeath);
}
