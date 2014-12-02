package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class DumbHero extends AbstractHero
{
	private ArrayList<Cell> visited;
	private boolean turned;
	
	public DumbHero(Direction direction)
	{
		super(direction);
		visited = new ArrayList<Cell>();
		turned = false;
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
		if(!visited.contains(cell)) visited.add(cell);
		
		if(foundGold)
		{
			if(location.getX() == 0 && location.getY() == 0) 
			{
				System.out.println("Made climb decision");
				return new ClimbDecision(true);
			}
			//else follow path back, so need to figure out direction and how to retrace steps
		}
		
		if(percepts[2]) //glitter, means gold
		{
			System.out.println("Made grab decision");
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
				return new TurnDecision(Turn.LEFT);
			}
			else
			{
				System.out.println("Made turn right decision");
				turned = true;
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
				return new TurnDecision(Turn.LEFT);
			}
			else if(move == 1 && !turned)
			{
				System.out.println("Made turn right decision");
				turned = true;
				return new TurnDecision(Turn.RIGHT);
			}
			else
			{
				System.out.println("Made move forward decision");
				turned = false;
				return new MoveDecision(moveForward());
			}
		}
		else
		{
			System.out.println("Made move forward decision");
			turned = false;
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
}
