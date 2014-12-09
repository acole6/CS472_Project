package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract hero agent
 * @author Alex
 *
 */
public abstract class AbstractHero
{
	protected GameBoard board;
	protected LinkedList<SearchNode> open;
	protected HashSet<String> exploring;
	protected HashSet<String> pit;
	protected HashSet<String> wall;
	protected String wumpus;
	protected Point start;
	protected List<List<Point>> paths;
	protected SearchNode currentNode;
	
	protected AbstractHero(GameBoard board)
	{
		this.board = board;
		start = new Point(0, 0);
		open = new LinkedList<SearchNode>();
		exploring = new HashSet<String>();
		pit = new HashSet<String>();
		wall = new HashSet<String>();
		paths = new ArrayList<List<Point>>();
	}
	
	public List<List<Point>> getPaths()
	{
		return paths;
	}
	
	public void solve()
	{
		open.add(new SearchNode(start, null));
		SearchNode currentNode = null;
		List<Point> explore = new ArrayList<Point>();
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
					paths.add(currentNode.getPath());
				}
				else if(status == Status.WUMPUS)
				{
					wumpus = currentNode.toString();
					paths.add(currentNode.getPath());
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
		open.add(new SearchNode(currentNode.getLocation(), null));
		while(!open.isEmpty())
		{
			List<Point> exit = new ArrayList<Point>();
			currentNode = pop(open);
			if(board.isAccessible(currentNode.getLocation()))
			{
				Status status = board.getCell(currentNode.getLocation().x, currentNode.getLocation().y).getStatus();
				if(currentNode.getLocation().equals(start))
				{	
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					paths.add(exit);
					break;
				}
				else if(status == Status.WUMPUS)
				{
					wumpus = currentNode.toString();
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					paths.add(exit);
				}
				else if(status == Status.PIT)
				{
					pit.add(currentNode.toString());
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					paths.add(exit);
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
	
	protected abstract SearchNode pop(LinkedList<SearchNode> list);
	
	protected void addNewChildrenToOpen(SearchNode parent)
	{
		Point location = parent.getLocation();
		SearchNode node = createSearchNode(location.x, location.y - 1, parent);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		
		node = createSearchNode(location.x + 1, location.y, parent);
		if(canAdd(node.toString()))
		{	
			open.add(node);
		}
		
		node = createSearchNode(location.x, location.y + 1, parent);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		
		node = createSearchNode(location.x - 1, location.y, parent);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
	}
	
	protected SearchNode createSearchNode(int row, int col, SearchNode parent)
	{
		return new SearchNode(new Point(row, col), parent);
	}
	
	protected boolean canAdd(String node)
	{
		if(!alreadyInOpen(node) 
				&& !wall.contains(node)
				&& !pit.contains(node)
				&& !node.equals(wumpus)
				&& !exploring.contains(node))
		{
			return true;
		}
		return false;
	}
	
	protected boolean alreadyInOpen(String nodeContents)
	{
		for(int i = 0; i < open.size(); i++)
		{
			SearchNode node = open.get(i);
			String oldNodeContents = node.toString();
			if(nodeContents.equalsIgnoreCase(oldNodeContents)) return true;
		}
		return false;
	}
	
	public class SearchNode implements Comparable<SearchNode>
	{
		final SearchNode parent;
		final Point location;
		final Direction initial;
		int danger = 0;
		protected boolean safe = false;
		
		public SearchNode(Point location, SearchNode parent)
		{
			this.location = location;
			this.parent = parent;
		}
		
		public SearchNode(Point location, SearchNode parent, int d)
		{
			this(location, parent);
			danger = d;
		}
		
		public List<Point> getPath()
		{
			List<Point> ret = new ArrayList<Point>();
			ret.add(location);
			SearchNode temp = parent;
			while(temp != null)
			{
				ret.add(0, temp.getLocation());
				temp = temp.getParent();
			}
			return ret;
		}
		
		public Point getLocation()
		{
			return location;
		}
		
		public SearchNode getParent()
		{
			return parent;
		}
		
		@Override
		public String toString()
		{
			return "(" + location.x + "," + location.y + ")";
		}
		
		public int getBasicDistance(SearchNode s)
		{
			return Math.abs(s.location.x - location.x) + Math.abs(s.location.y - location.y); 
		}

		public void safetyDance()
		{
			danger = 0;
			safe = true;
		}
		
		public void setDanger(int d)
		{
			if(!safe)
				danger = d;
		}
		
		public void incrementDanger(int d)
		{
			if(d == 0)
				safetyDance();
			if(!safe)
				danger += d;
		}
		
		public int getDanger()
		{
			return danger;
		}

		@Override
		public int compareTo(SearchNode o) { 
			if(danger - o.danger == 0)
			{
				if(currentNode.getBasicDistance(this) < currentNode.getBasicDistance(o))
					return -1;
				if(currentNode.getBasicDistance(this) > currentNode.getBasicDistance(o))
					return 1;
				return 0;
			}
			return danger - o.danger;
		}
	}
}
