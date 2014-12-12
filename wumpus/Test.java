package wumpus;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test 
{
	
	
	/**
	 * Game boards the game might run on. 
	 * W represents a Wumpus, P represents a pit,
	 * G represents the gold, and the player begins
	 * in the upper left corner
	 */
	private static final String[] smallBoard = {
	      " W P",
	      "    ",
	      "  G ",
	      "P  P",
	  };
	
	private static final String[] mediumBoard1 = {
		" P                 P",
		"  P     P           ",
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
	 
	
	private static final String[] mediumBoard2 = {
		" P                 P",
		"  P     P           ",
		"             G      ",
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
		"            P       ",
		"                    ",
		"    P               ",
		"                    ",
		"             P      ",
		"                    ",
		" P                  ",
	};
	
	
	private static final String[] mediumBoard3 = {
		" P                 P",
		"  P     P           ",
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
		"            P       ",
		"                    ",
		"    P               ",
		"                    ",
		"             P      ",
		"       G            ",
		" P                  ",
	};
	
	private static final String[] mediumBoard4 = {
		" PG                P",
		"  P     P           ",
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
		"            P       ",
		"                    ",
		"    P               ",
		"                    ",
		"             P      ",
		"                    ",
		" P                  ",
	};
	
	
	
	
	public static void main(String[] args) throws FileNotFoundException 
	{	
		GameBoard[] boards = {new GameBoard(mediumBoard1),new GameBoard(mediumBoard2),new GameBoard(mediumBoard3),new GameBoard(mediumBoard4)};
		double avgHHero = 0;
		double avgPath = 0;
		int dfsPath = 0;
		int bfsPath = 0;
		int dfsDeath = 0;
		int bfsDeath = 0;
		int loop = 0;
		Random rand = new Random();
		for(; loop < 1000; loop++)
		{
			GameBoard board = boards[rand.nextInt(4)];
			ArrayList<AbstractHero> heroes = new ArrayList<AbstractHero>() ;
				heroes.add(new DepthSearchHero(board));
				heroes.add(new BreadthSearchHero(board));
				heroes.add(new HeuristicHero(board));
		
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
					if(!path.isEmpty() && path.get(path.size()-1).equals(new Point(0,0)))
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
				if(hero.getClass().getSimpleName().equals("HeuristicHero")){
					avgHHero += died;
					avgPath += hero.getNodesVisited();
				}
				else if (hero.getClass().getSimpleName().equals("BreadthSearchHero"))
				{
					bfsPath = hero.getNodesVisited();
					bfsDeath += died;
				}
				else
				{
					dfsPath = hero.getNodesVisited();
					dfsDeath += died;
				}
				System.out.println("Solution path length: " + solutionPathLength);
				System.out.println("-----------------------------------------------");
			}
		}
		System.out.println("Run " + loop + " times");
		System.out.println("Average heuristic hero deaths: " + avgHHero/1000);
		System.out.println("Average heuristic hero path length: " + avgPath/1000);
		System.out.println("Heuristic final score: " + (10000.0 - (200.0*(avgHHero/1000)) - (avgPath/1000) * 3.0));
		System.out.println("BFS average deaths: " + bfsDeath/1000);
		System.out.println("BFS average path length: " + bfsPath/1000);
		System.out.println("Heuristic final score: " + (10000.0 - (200.0*(avgHHero/1000)) - (avgPath/1000) * 3.0));
		System.out.println("DFS average deaths: " + dfsDeath/1000);
		System.out.println("DFS average path length: " + dfsPath/1000);
		System.out.println("Heuristic final score: " + (10000.0 - (200.0*(avgHHero/1000)) - (avgPath/1000) * 3.0));
	}
}
