package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import wumpus.AbstractHero.GamePath;
import wumpus.AbstractHero.SearchNode;

public class BreadthSearchHero extends AbstractHero
{
	public BreadthSearchHero(GameBoard board)
	{
		super(board);
	}

	@Override
	public void solve() 
	{
		open.add(new SearchNode(start, null));
		SearchNode currentNode = null;
		List<Point> explore = new ArrayList<Point>();
		List<Decision> decisions = new ArrayList<Decision>();
		while(!open.isEmpty())
		{
			currentNode = pop(open);
			if(board.isAccessible(currentNode.getLocation()))
			{
				Status status = board.getCell(currentNode.getLocation().x, currentNode.getLocation().y).getStatus();
				if(status == Status.GOLD)
				{
					explore = currentNode.getParent().getPath();
					break;
				}
				else if(status == Status.PIT)
				{
					pit.add(currentNode.toString());
					explore = currentNode.getPath();
					paths.add(new GamePath(explore, decisions));
				}
				else if(status == Status.WUMPUS)
				{
					wumpus = currentNode.toString();
					explore = currentNode.getPath();
					paths.add(new GamePath(explore, decisions));
				}
				else
				{
					exploring.add(currentNode.toString());
					addNewChildrenToOpen(currentNode);
				}
			}
			else
			{
				wall.add(currentNode.toString());
			}
		}
		
		open.clear();
		exploring.clear();
		open.add(new SearchNode(currentNode.getLocation(), null, currentNode.getDirection()));
		while(!open.isEmpty())
		{
			List<Point> exit = new ArrayList<Point>();
			List<Decision> exitDecisions = new ArrayList<Decision>();
			currentNode = pop(open);
			if(board.isAccessible(currentNode.getLocation()))
			{
				Status status = board.getCell(currentNode.getLocation().x, currentNode.getLocation().y).getStatus();
				if(currentNode.getLocation().equals(start))
				{
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					List<Decision> ret = new ArrayList<Decision>();
					paths.add(new GamePath(exit, ret));
					break;
				}
				else if(status == Status.PIT)
				{
					pit.add(currentNode.toString());
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					List<Decision> ret = new ArrayList<Decision>();
					paths.add(new GamePath(exit, ret));
				}
				else if(status == Status.WUMPUS)
				{
					wumpus = currentNode.toString();
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					List<Decision> ret = new ArrayList<Decision>();
					paths.add(new GamePath(exit, ret));
				}
				else
				{
					exploring.add(currentNode.toString());
					addNewChildrenToOpen(currentNode);
				}
			}
			else
			{
				wall.add(currentNode.toString());
			}
		}
	}
	
	private void addNewChildrenToOpen(SearchNode parent)
	{
		Point location = parent.getLocation();
		SearchNode node = createSearchNode(location.x, location.y - 1, parent, Direction.LEFT);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		
		node = createSearchNode(location.x + 1, location.y, parent, Direction.DOWN);
		if(canAdd(node.toString()))
		{	
			open.add(node);
		}
		
		node = createSearchNode(location.x, location.y + 1, parent, Direction.RIGHT);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		
		node = createSearchNode(location.x - 1, location.y, parent, Direction.UP);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
	}
	
	private SearchNode createSearchNode(int row, int col, SearchNode parent, Direction direction)
	{
		return new SearchNode(new Point(row, col), parent, direction);
	}

	@Override
	protected SearchNode pop(LinkedList<SearchNode> list) 
	{
		return list.removeFirst();
	}
}
