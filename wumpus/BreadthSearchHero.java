package wumpus;

import java.util.LinkedList;

public class BreadthSearchHero extends AbstractHero
{
	public BreadthSearchHero(GameBoard board)
	{
		super(board);
	}

	@Override
	protected SearchNode pop(LinkedList<SearchNode> list) 
	{
		return list.removeFirst();
	}
}

