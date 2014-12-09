package wumpus;

import java.awt.Point;
import java.util.List;

public class Test 
{
	public static void main(String[] args) 
	{
		GameBoard board = new GameBoard(4, 4);
		board.setCellStatus(0, 1, Status.WUMPUS);
		board.setCellStatus(2, 2, Status.GOLD);
		board.setCellStatus(0, 3, Status.PIT);
		board.setCellStatus(3, 0, Status.PIT);
		board.setCellStatus(3, 3, Status.PIT);
		
		AbstractHero hero = new BreadthSearchHero(board);
		hero.solve();
		List<List<Point>> paths = hero.getPaths();
		
		for(List<Point> path : paths)
		{
			String str = "";
			for(Point point : path)
			{
				str += point.toString() + " ";
			}
			System.out.println(str);
		}
	}
}
