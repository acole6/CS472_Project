package wumpus;

import java.awt.Point;
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
	 
	public static void main(String[] args) 
	{	
		final GameBoard board = new GameBoard(mediumBoard);
		
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
			for(List<Point> path : paths)
			{
				String str = "";
				for(Point point : path)
				{
					str += point.toString() + " ";
				}
				System.out.println(str);
				solutionPathLength = path.size();
			}
			System.out.println("Nodes visited: " + hero.getNodesVisited());
			System.out.println("Solution path length: " + solutionPathLength);
			System.out.println("-----------------------------------------------");
		}
	}
}
