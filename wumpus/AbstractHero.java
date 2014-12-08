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
	protected boolean ranIntoWall = false;
	protected List<GamePath> paths;
	
	protected AbstractHero(GameBoard board)
	{
		this.board = board;
		start = new Point(0, 0);
		open = new LinkedList<SearchNode>();
		exploring = new HashSet<String>();
		pit = new HashSet<String>();
		wall = new HashSet<String>();
		paths = new ArrayList<GamePath>();
	}
	
	public List<GamePath> getPaths()
	{
		return paths;
	}
	
	public abstract void solve();
	
	protected abstract SearchNode pop(LinkedList<SearchNode> list);
	
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
	
	public class SearchNode
	{
		final SearchNode parent;
		final Point location;
		final Direction initial;
		List<Decision> initialDecisions;
		Direction direction;
		List<Decision> decisions;
		
		public SearchNode(Point location, SearchNode parent)
		{
			this.location = location;
			this.parent = parent;
			direction = Direction.DOWN;
			initial = direction;
			this.decisions = new ArrayList<Decision>();
			initialDecisions = new ArrayList<Decision>();
		}
		
		public SearchNode(Point location, SearchNode parent, Direction direction)
		{
			this.location = location;
			this.parent = parent;
			this.direction = direction;
			initial = this.direction;
			this.decisions = new ArrayList<Decision>();
			initialDecisions = new ArrayList<Decision>();
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
		
		public void setDirection(Direction direction)
		{
			this.direction = direction;
		}
		
		public void setDecisions(List<Decision> decisions)
		{
			this.decisions = decisions;
		}
		
		public Direction getDirection()
		{
			return direction;
		}
		
		public List<Decision> getDecisions()
		{
			return decisions;
		}
		
		public boolean addToDecisions(List<Decision> toAdd)
		{
			return decisions.addAll(toAdd);
		}
		
		public void setInitialDecisions(List<Decision> initialDecisions)
		{
			this.initialDecisions.clear();
			this.initialDecisions.addAll(initialDecisions);
		}
		
		public void setToInitial()
		{
			direction = initial;
			decisions.clear();
			decisions.addAll(initialDecisions);
		}
		
		@Override
		public String toString()
		{
			return "(" + location.x + "," + location.y + ")";
		}
	}
	
	public class GamePath
	{
		List<Point> path;
		List<Decision> decisions;
		
		public GamePath(List<Point> path, List<Decision> decisions)
		{
			this.path = path;
			this.decisions = decisions;
		}
		
		public List<Point> getPath()
		{
			return path;
		}
		
		public List<Decision> getDecisions()
		{
			return decisions;
		}
	}
}
