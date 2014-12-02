package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class DumbHero extends AbstractHero
{
	private ArrayList<Cell> visited;
	private boolean turned;
	private Cell previousCell;
	
	public DumbHero(Direction direction)
	{
		super(direction);
		visited = new ArrayList<Cell>();
		turned = false;
		previousCell = null;
	}
	
	@Override
	public void reset()
	{
		turned = false;
		previousCell = null;
		visited.clear();
	}
	
	@Override
	public Decision makeDecision(boolean[] percepts)
	{
		Cell cell = new Cell((int) location.getX(), (int) location.getY());
		if(percepts[2])
		{
			cell.setStatus(Status.GOLD);
		}
		else
		{
			cell.setStatus(Status.EMPTY);
		}
		if(!cell.equalsIgnoreStatus(previousCell) && !foundGold && !percepts[2]) 
		{
			visited.add(cell);
			previousCell = cell;
		}
		
		if(foundGold)
		{
			if(location.getX() == 0 && location.getY() == 0) 
			{
				System.out.println("Made climb decision");
				visitedCells();
				return new ClimbDecision(true);
			}
			else
			{
				visitedCells();
				return retraceSteps();
			}
		}
		
		if(percepts[2]) //glitter, means gold
		{
			System.out.println("Made grab decision");
			visitedCells();
			return new GrabDecision(true);
		}
	
		if(percepts[3]) //bump, means wall is directly forward
		{
			Random rand = new Random();
			int move = rand.nextInt(2);
			if(move == 0)
			{
				System.out.println("Made turn left decision");
				turned = true;
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
			else
			{
				System.out.println("Made turn right decision");
				turned = true;
				visitedCells();
				return new TurnDecision(Turn.RIGHT);
			}
		}
		else if(percepts[0] || percepts[1]) //wumpus or pit deteched and no wall to block forward movement
		{
			Random rand = new Random();
			int move = rand.nextInt(3);
			if(move == 0 && !turned)
			{
				System.out.println("Made turn left decision");
				turned = true;
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
			else if(move == 1 && !turned)
			{
				System.out.println("Made turn right decision");
				turned = true;
				visitedCells();
				return new TurnDecision(Turn.RIGHT);
			}
			else
			{
				System.out.println("Made move forward decision");
				turned = false;
				visitedCells();
				return new MoveDecision(moveForward());
			}
		}
		else
		{
			System.out.println("Made move forward decision");
			turned = false;
			visitedCells();
			return new MoveDecision(moveForward());
		}
	}
	
	
	private Point moveForward()
	{
		int row = (int) location.getX();
		int col = (int) location.getY();
		if(direction == Direction.UP)
		{
			return new Point(row - 1, col);
		}
		else if(direction == Direction.DOWN)
		{
			return new Point(row + 1, col);
		}
		else if(direction == Direction.LEFT)
		{
			return new Point(row, col - 1);
		}
		else
		{
			return new Point(row, col + 1);
		}
	}
	
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
				System.out.println("made turn left decision");
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.DOWN)
			{
				System.out.println("Made move forward decision");
				visited.remove(previous);
				visitedCells();
				return new MoveDecision(moveForward());
			}
			else if(direction == Direction.LEFT)
			{
				System.out.println("made turn left decision");
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
			else
			{
				System.out.println("made turn right decision");
				visitedCells();
				return new TurnDecision(Turn.RIGHT);
			}
		}
		else if(r > 0 && c == 0)
		{
			if(direction == Direction.UP)
			{
				System.out.println("Made move forward decision");
				visited.remove(previous);
				visitedCells();
				return new MoveDecision(moveForward());
			}
			else if(direction == Direction.DOWN)
			{
				System.out.println("made turn left decision");
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.LEFT)
			{
				visitedCells();
				return new TurnDecision(Turn.RIGHT);
			}
			else
			{
				System.out.println("made turn left decision");
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
		}
		else if(r == 0 && c < 0)
		{
			if(direction == Direction.UP)
			{
				System.out.println("made turn right decision");
				visitedCells();
				return new TurnDecision(Turn.RIGHT);
			}
			else if(direction == Direction.DOWN)
			{
				System.out.println("made turn left decision");
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.LEFT)
			{
				System.out.println("made turn right decision");
				visitedCells();
				return new TurnDecision(Turn.RIGHT);
			}
			else
			{
				System.out.println("Made move forward decision");
				visited.remove(previous);
				visitedCells();
				return new MoveDecision(moveForward());
			}
		}
		else
		{
			if(direction == Direction.UP)
			{
				System.out.println("made turn left decision");
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
			else if(direction == Direction.DOWN)
			{
				System.out.println("made turn right decision");
				visitedCells();
				return new TurnDecision(Turn.RIGHT);
			}
			else if(direction == Direction.LEFT)
			{
				System.out.println("Made move forward decision");
				visited.remove(previous);
				visitedCells();
				return new MoveDecision(moveForward());
			}
			else
			{
				System.out.println("made turn left decision");
				visitedCells();
				return new TurnDecision(Turn.LEFT);
			}
		}
	}
	
	private void visitedCells()
	{
		for(int i = 0; i < visited.size(); i++)
		{
			System.out.print(visited.get(i).toString() + " ");
		}
		System.out.println();
	}
}
