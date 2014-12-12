package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
	protected int nodesVisited;
	protected SearchNode currentNode;
	
	protected AbstractHero(GameBoard board)
	{
		this.board = board;
		start = new Point(0, 0);
		open = new LinkedList<SearchNode>();
		exploring = new HashSet<String>();
		pit = new HashSet<String>();
		wall = new HashSet<String>();
	}
	
	public int getNodesVisited()
	{
		return nodesVisited;
	}
	
	public List<List<Point>> solve()
	{
		List<List<Point>> paths = new ArrayList<List<Point>>();
		open.add(new SearchNode(start, null));
		currentNode = null;
		while(!open.isEmpty())
		{
			List<Point> path = new ArrayList<Point>();
			currentNode = pop(open);
			nodesVisited++;
			if(board.isAccessible(currentNode.getLocation()))
			{
				Status status = board.getCell(currentNode.getLocation().x, currentNode.getLocation().y).getStatus();
				if(status == Status.GOLD)
				{
					path = currentNode.getPath();
					for(int i = path.size() - 2; i >= 0; i--) //reverse the path but skip gold square
					{
						path.add(path.get(i));
					}
					paths.add(path);
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
		return paths;
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
		int danger = 0;
		protected boolean safe = false;
		private int rand;
		
		public SearchNode(Point location, SearchNode parent)
		{
			this.location = location;
			this.parent = parent;
			rand = (new Random().nextInt(50));
		}
		
		public SearchNode(Point location, SearchNode parent, int d)
		{
			this(location, parent);
			danger = d;
			rand = (new Random().nextInt(50));
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
		
		public int getRand()
		{
			return rand;
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

		
		/**
		 *Implementation of compareTo to allow sorting. 
		 *
		 *Nodes are sorted based on their danger level, if the danger levels are equal, 
		 *the closer node is selected. If the nodes are equidistant, the node is selected at random,
		 *using an random integer assigned to the instance of the node at the time of instantiation
		 *
		 *@param The other node to be compared to
		 */
		@Override
		public int compareTo(SearchNode o) { 
			if(currentNode == null) return danger - o.danger;
			if(danger - o.danger == 0)
			{
				if(currentNode.getBasicDistance(this) < currentNode.getBasicDistance(o))
					return -1;
				if(currentNode.getBasicDistance(this) > currentNode.getBasicDistance(o))
					return 1;
				return this.getRand() - o.getRand();
			}
			return danger - o.danger;
		}
	}
}
