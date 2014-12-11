package wumpus;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Test 
{
	private static final String[] smallBoard = {
	      " W P",
	      "    ",
	      "  G ",
	      "P  P",
	  };
	
	private static final String[] mediumBoard = {
		"                   P",
		"        P           ",
		"                    ",
		" P                  ",
		"                    ",
		"              W     ",
		"   P                ",
		"                    ",
		"                    ",
		"   P           P    ",
		"                    ",
		"       P            ",
		"                    ",
		"           GP       ",
		"                    ",
		"    P               ",
		"                    ",
		"             P      ",
		"                    ",
		" P                  ",
	};
	 
	public static void main(String[] args) throws FileNotFoundException 
	{	
		final GameBoard board = new GameBoard("mediumBoard.txt");
		
		ArrayList<AbstractHero> heroes = new ArrayList<AbstractHero>() {{
			add(new DepthSearchHero(board));
			add(new BreadthSearchHero(board));
			add(new HeuristicHero(board));
		}};
	
		for(AbstractHero hero : heroes)
		{
			System.out.println(hero.getClass().getSimpleName());
			System.out.println("-----------------------------------------------");
			List<List<Point>> paths = hero.solve();
			int solutionPathLength = 0;
			int died = 0;
			for(int i = 0; i < paths.size(); i++)
			{
				List<Point> path = paths.get(i);
				if(i == paths.size() - 1)
				{
					solutionPathLength = path.size();
				}
				else
				{
					died++;
				}
			}
			System.out.println("Nodes visited: " + hero.getNodesVisited());
			System.out.println("Times died: " + died);
			System.out.println("Solution path length: " + solutionPathLength);
			System.out.println("-----------------------------------------------");
		}
	}
}
