package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Very basic hero agent that makes decision mostly on precepts and only
 * remembers if he has previous turned and the cells he has visited so
 * that he can retrace his steps to get out of the cave
 * @author Alex Cole
 *
 */
public class DumbHero extends AbstractHero
{
	private ArrayList<Cell> visited;
	private boolean turned;
	private Cell previousCell;
	
	/**
	 * Constructs a DumbHero agent
	 * @param direction
	 */
	public DumbHero(Direction direction)
	{
		super(direction);
		visited = new ArrayList<Cell>();
		turned = false;
		previousCell = null;
	}
	
	/**
	 * Sets the hero back to his initial state
	 * including clearing his knowledge of cells visited
	 */
	@Override
	public void reset()
	{
		turned = false;
		previousCell = null;
		visited.clear();
	}
	
	/**
	 * Updates that the hero has been killed but does not store
	 * the information on what killed the hero.
	 */
	@Override
	public void killed(Status causeOfDeath) 
	{
		alive = false;
	}
	
	/**
	 * DumbHero makes decision based on percepts passed in, if the hero has found gold
	 * or not, if the hero has turned, and visited cells (if gold found).
	 * @param percepts
	 * 		The percepts for the hero to consider when making a decision
	 */
	@Override
	public Decision makeDecision(boolean[] percepts)
	{
		Cell cell = new Cell((int) location.getX(), (int) location.getY());
		if(!cell.equalsIgnoreStatus(previousCell) && !foundGold && !percepts[2]) 
		{
			//only add a cell to visited if the hero has moved from the cell another cell
			//and this does allow duplicates
			visited.add(cell);
			previousCell = cell;
		}
		
		if(foundGold) //found gold so grab it or if already grabbed it, head for the exit
		{
			if(location.getX() == 0 && location.getY() == 0) 
			{
				return new ClimbDecision(true);
			}
			else
			{
				return retraceSteps();
			}
		}
		
		if(percepts[2]) //glitter, means gold
		{
			return new GrabDecision(true);
		}
	
		if(percepts[3]) //bump, means wall is directly forward
		{
			//if bump pick turn direction randomly
			Random rand = new Random();
			int move = rand.nextInt(2);
			if(move == 0)
			{
				turned = true;
				return new TurnDecision(Turn.LEFT);
			}
			else
			{
				turned = true;
				return new TurnDecision(Turn.RIGHT);
			}
		}
		else if(percepts[0] || percepts[1]) //wumpus or pit deteched and no wall to block forward movement
		{
			//the dumb hero doesn't have any knowledge of what is to come, so
			//moving forwarding and turning are equal options. Though if turned,
			//it is assumed that the hero turned to move forward in that direction
			//and is not trying to retreat by turning again to go backwards.
			Random rand = new Random();
			int move = rand.nextInt(3);
			if(move == 0 && !turned)
			{
				turned = true;
				return new TurnDecision(Turn.LEFT);
			}
			else if(move == 1 && !turned)
			{
				turned = true;
				return new TurnDecision(Turn.RIGHT);
			}
			else
			{
				turned = false;
				return new MoveDecision(moveForward());
			}
		}
		else
		{
			//if nothing is detected, then move forward
			turned = false;
			return new MoveDecision(moveForward());
		}
	}
	
	/**
	 * The hero retraces the visited cells. If the current cell is
	 * not directly in from of the hero, the hero turns to attemp to face
	 * the cell.
	 * @return
	 * 		The decision made when trying retrace the visited cells (turn or move forward decision)
	 */
	private Decision retraceSteps()
	{
		if(visited.isEmpty()) return new ClimbDecision(true);
		
		Cell previous = visited.get(visited.size() - 1);
		int prevRow = (int) previous.getLocation().getX();
		int prevCol = (int) previous.getLocation().getY();
		int row = (int) location.getX();
		int col = (int) location.getY();
		
		int r = row - prevRow;
		int c = col - prevCol;
		
		if(r < 0 && c == 0)
		{
			if(direction == Direction.UP)
			{
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.DOWN)
			{
				visited.remove(previous);
				return new MoveDecision(moveForward());
			}
			else if(direction == Direction.LEFT)
			{
				return new TurnDecision(Turn.LEFT);
			}
			else
			{
				return new TurnDecision(Turn.RIGHT);
			}
		}
		else if(r > 0 && c == 0)
		{
			if(direction == Direction.UP)
			{
				visited.remove(previous);
				return new MoveDecision(moveForward());
			}
			else if(direction == Direction.DOWN)
			{
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.LEFT)
			{
				return new TurnDecision(Turn.RIGHT);
			}
			else
			{
				return new TurnDecision(Turn.LEFT);
			}
		}
		else if(r == 0 && c < 0)
		{
			if(direction == Direction.UP)
			{
			
				return new TurnDecision(Turn.RIGHT);
			}
			else if(direction == Direction.DOWN)
			{
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.LEFT)
			{
				return new TurnDecision(Turn.RIGHT);
			}
			else
			{
				visited.remove(previous);
				return new MoveDecision(moveForward());
			}
		}
		else
		{
			if(direction == Direction.UP)
			{
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.DOWN)
			{
				return new TurnDecision(Turn.RIGHT);
			}
			else if(direction == Direction.LEFT)
			{
				visited.remove(previous);
				return new MoveDecision(moveForward());
			}
			else
			{
				return new TurnDecision(Turn.LEFT);
			}
		}
	}
}
