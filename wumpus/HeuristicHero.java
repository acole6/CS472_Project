package wumpus;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class HeuristicHero extends AbstractHero 
{
	protected HashMap<String, SearchNode> nodes = new HashMap<String, SearchNode>(); 	
	
	public HeuristicHero(GameBoard board) 
	{
		super(board);
	}
	
	/**
	 * Adds children to parent node. It evaluates the danger level of the each node and adds it to the mapping.
	 * Danger is increased based on the number of percepts. If stench or breeze is detected, the danger level of all children
	 * of the current node is increased by one. If both percepts are detected, it is increased by two. 
	 * 
	 * @param The parent to add children to
	 */
	@Override
	protected void addNewChildrenToOpen(SearchNode parent)
	{
		boolean[] percepts = board.getPercepts(parent.location.x, parent.location.y);
		int danger = 0;
		for(int i = 0; i < 2; i++)
			if(percepts[i])
				danger++;
		Point location = parent.getLocation();
		SearchNode node = createSearchNode(location.x, location.y - 1, parent);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		addDanger(node, danger);
		
		node = createSearchNode(location.x + 1, location.y, parent);
		if(canAdd(node.toString()))
		{	
			open.add(node);
		}
		addDanger(node, danger);
		
		node = createSearchNode(location.x, location.y + 1, parent);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		addDanger(node, danger);
		
		node = createSearchNode(location.x - 1, location.y, parent);
		if(canAdd(node.toString()))
		{
			open.add(node);
		}
		addDanger(node, danger);
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
	
	/**
	 * Retrieves the closest, least dangerous node to be searched, evaluated by the compareTo method.
	 */
	@Override
	protected SearchNode pop(LinkedList<SearchNode> list) {
		Collections.sort(list);
		return list.removeFirst();
	}
}
