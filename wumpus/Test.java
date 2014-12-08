package wumpus;

import java.awt.Point;
import java.util.List;

public class Test {

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
		List<DepthSearchHero.GamePath> list = hero.getPaths();
		
		for(DepthSearchHero.GamePath path : list)
		{
			List<Point> points = path.getPath();
			List<Decision> decisions = path.getDecisions();
			String str = "";
			for(Point point : points)
			{
				str += point.toString() + " ";
			}
			System.out.println(str);
			
			str = "";
			for(Decision decision : decisions)
			{
				if(decision == Decision.FORWARD)
				{
					str += "move forward, ";
				}
				else if(decision == Decision.TURN_LEFT)
				{
					str += "turn left, ";
				}
				else if(decision == Decision.TURN_RIGHT)
				{
					str += "turn right, ";
				}
				else if(decision == Decision.GRAB)
				{
					str += "grab, ";
				}
				else if(decision == Decision.CLIMB)
				{
					str += "cimb, ";
				}
			}
			System.out.println(str);
		}
	}

}
