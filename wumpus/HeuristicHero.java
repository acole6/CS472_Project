package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HeuristicHero extends AbstractHero {
	
	public HeuristicHero(GameBoard board) {
		super(board);
	}
	
	protected HashMap<String, SearchNode> nodes = new HashMap<String, SearchNode>(); 
	
	@Override
	public void solve() {
		open.add(new SearchNode(start, null));
		currentNode = null;
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
		boolean[] percepts = board.getPercepts(parent.location.x, parent.location.y);
		int danger = 0;
		for(int i = 0; i < 2; i++)
			if(percepts[i])
				danger++;
		Point location = parent.getLocation();
		SearchNode node = createSearchNode(location.x, location.y - 1, parent, Direction.LEFT);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		addDanger(node, danger);
		
		node = createSearchNode(location.x + 1, location.y, parent, Direction.DOWN);
		if(canAdd(node.toString()))
		{	
			open.add(node);
		}
		addDanger(node, danger);
		
		node = createSearchNode(location.x, location.y + 1, parent, Direction.RIGHT);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		addDanger(node, danger);
		
		node = createSearchNode(location.x - 1, location.y, parent, Direction.UP);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		addDanger(node, danger);
	}
	
	private SearchNode createSearchNode(int row, int col, SearchNode parent, Direction direction)
	{
		return new SearchNode(new Point(row, col), parent, direction);
	}

	private void addDanger(SearchNode n, int danger)
	{
		if(!nodes.containsKey(n.toString()))
		{
			n.incrementDanger(danger);
			nodes.put(n.toString(), n);
		}
		else
		{
			n = nodes.get(n.toString());
			n.incrementDanger(danger);
		}
	}
	
	@Override
	protected SearchNode pop(LinkedList<SearchNode> list) {
		Collections.sort(list);
		return list.removeFirst();
	}
}
