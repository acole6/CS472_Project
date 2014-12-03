package wumpus;

import java.awt.Point;

/**
 * Interface for a hero agent
 * @author Alex Cole
 *
 */
public interface Hero 
{
	/**
	 * Gets the direction the hero is facing (up, down, left, right).
	 * @return
	 * 		The direction the hero is facing
	 */
	Direction getDirection();
	
	/**
	 * Sets the direction the hero is facing.
	 * @param direction
	 * 		The direction the hero should face.
	 */
	void setDirection(Direction direction);
	
	/**
	 * Gets the location of the hero on the board.
	 * @return
	 * 		The location of the hero
	 */
	Point getLocation();
	
	/**
	 * Sets the location of the hero on the board.
	 * @param row
	 * 		The row on the board
	 * @param col
	 * 		The Column on the board
	 */
	void setLocation(int row, int col);
	
	/**
	 * Checks if the hero is carrying gold.
	 * @return
	 * 		True if the hero has gold; otherwise false
	 */
	boolean hasGold();
	
	/**
	 * Updates if the hero has found gold or not.
	 * @param found
	 * 		True if the hero has found gold and false if the hero has not
	 */
	void foundGold(boolean found);
	
	/**
	 * The hero makes a decision about what to do based on the hero's location on
	 * the board and the percepts passed in. The hero can decide to move forward, turn left
	 * or right, climb out of the cave or grab gold.
	 * @param percepts
	 * 		The percepts that the hero agent considers when making a decision
	 * @return
	 * 		The decision made by the hero
	 */
	Decision makeDecision(boolean[] percepts);
	
	/**
	 * Resets the hero back to his initial state
	 */
	void reset();
	
	/**
	 * Checks to see if the hero is alive
	 * @return
	 * 		True if alive; otherwise false
	 */
	boolean isAlive();
	
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
	void killed(int row, int col, Status causeOfDeath);
}
