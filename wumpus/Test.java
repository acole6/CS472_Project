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
		
		//Alternate these comments for running on 20x20 boards or 50x50 boards
		
		//GameBoard[] boards = {new GameBoard(mediumBoard1),new GameBoard(mediumBoard2),new GameBoard(mediumBoard3),new GameBoard(mediumBoard4)};
		GameBoard[] boards = {new GameBoard("largeboard1.txt"), new GameBoard("largeboard2.txt"),new GameBoard("largeboard3.txt"),new GameBoard("largeboard4.txt")};
		
		//Heuristic Hero variables
		double avgHHero = 0;
		double hNodes = 0;
		double hLength = 0;
		//DFS Hero variables
		double dfsNodes = 0;
		double dfsDeath = 0;
		double dfsLength = 0;
		//BFS Hero variables
		double bfsNodes = 0;
		double bfsDeath = 0;
		double bfsLength = 0;
		
		
		
		int loop = 0;
		Random rand = new Random();
		for(; loop < 1000; loop++)
		{
			GameBoard board = boards[rand.nextInt(boards.length)];
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
				System.out.println("Solution path length: " + solutionPathLength);
				System.out.println("-----------------------------------------------");
				if(hero.getClass().getSimpleName().equals("HeuristicHero")){
					avgHHero += died;
					hNodes += hero.getNodesVisited();
					hLength += solutionPathLength;
				}
				else if (hero.getClass().getSimpleName().equals("BreadthSearchHero"))
				{
					bfsNodes += hero.getNodesVisited();
					bfsDeath += died;
					bfsLength += solutionPathLength;
				}
				else
				{
					dfsNodes += hero.getNodesVisited();
					dfsDeath += died;
					dfsLength += solutionPathLength;
				}
			}
		}
		System.out.println("Ran " + loop + " times");
		System.out.println("-----------------------------------------------");
		System.out.println("Average heuristic hero deaths: " + avgHHero/loop);
		System.out.println("Average heuristic nodes visited: " + hNodes/loop);
		System.out.println("Average heuristic path length: " + hLength/loop);
		System.out.println("Heuristic final score: " + (10000.0 - (200.0*(avgHHero/loop)) - ((hNodes/loop) * 3.0) - ((hLength/loop) * 4.0)));
		System.out.println("-----------------------------------------------");
		System.out.println("BFS average deaths: " + bfsDeath/loop);
		System.out.println("BFS average nodes visited: " + bfsNodes/loop);
		System.out.println("Average bfs path length: " + bfsLength/loop);
		System.out.println("BFS final score: " + (10000.0 - (200.0*(bfsDeath/loop)) - ((bfsNodes/loop) * 3.0)- ((bfsLength/loop) * 4.0)));
		System.out.println("-----------------------------------------------");
		System.out.println("DFS average deaths: " + dfsDeath/loop);
		System.out.println("DFS average nodes visited: " + dfsNodes/loop);
		System.out.println("Average dfs path length: " + dfsLength/loop);
		System.out.println("DFS final score: " + (10000.0 - (200.0*(dfsDeath/loop)) - ((dfsNodes/loop) * 3.0)- ((dfsLength/loop) * 4.0)));
		System.out.println("-----------------------------------------------");
	}
}
