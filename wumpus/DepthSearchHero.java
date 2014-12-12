package wumpus;

import java.util.LinkedList;

/**
 * Uses depth first search to explore the cave
 * @author Alex Cole
 *
 */
public class DepthSearchHero extends AbstractHero
{
	public DepthSearchHero(GameBoard board) 
	{
		super(board);
	}
	
	@Override
	protected SearchNode pop(LinkedList<SearchNode> list)
	{
		return list.removeLast();
	}
}
