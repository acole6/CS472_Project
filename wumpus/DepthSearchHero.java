package wumpus;

import java.util.LinkedList;

/**
 * Will consider moving to left cell, right cell or forward cell as expanding the tree.
 * If the hero has to move backward, then the hero is going back up the tree
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
