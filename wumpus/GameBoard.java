package wumpus;

import java.awt.Point;
import java.util.ArrayList;

public class GameBoard
{
  
  private Cell[][] cells;
  private Cell start;
  private boolean gameOver;
  private boolean[] percepts;
  private Point heroLocation;
  private Direction heroDirection;
  
  public GameBoard(int rows, int columns)
  {
	  percepts = new boolean[5];
	  heroLocation = new Point(0, 0);
	  heroDirection = Direction.DOWN;
	  
	  cells = new Cell[rows][columns];
	  for (int row = 0; row < rows; ++row)
	  {
		  for (int col = 0; col < columns; ++col)
		  {
			  Cell current = new Cell(row, col);
			  if(row == 0 && col == 0) start = current;
			  current.setStatus(Status.EMPTY);
			  cells[row][col] = current;
		  }
	  }
    
	  for (int row = 0; row < rows; ++row)
	  {
		  for (int col = 0; col < columns; ++col)
		  {
			  Cell square = cells[row][col];
			  if (isAccessible(square))
			  {
				  // add edges from this to neighbors
				  if (row > 0 && isAccessible(cells[row - 1][col]))
				  {
					  square.addNeighbor(cells[row - 1][col]);
				  }
				  if (col > 0 && isAccessible(cells[row][col - 1]))
				  {
					  square.addNeighbor(cells[row][col - 1]);
				  }
				  if (row < rows - 1 && isAccessible(cells[row + 1][col]))
				  {
					  square.addNeighbor(cells[row + 1][col]);
				  }
				  if (col < columns - 1 && isAccessible(cells[row][col + 1]))
				  {
					  square.addNeighbor(cells[row][col + 1]);
				  }
			  }
		  }
	  }
   }
  
  public GameBoard(String[] rows)
  {
	  percepts = new boolean[5];
	  heroLocation = new Point(0, 0);
	  heroDirection = Direction.DOWN;
	  
	  int width = rows[0].length();
	  int height = rows.length;
	  cells = new Cell[height][width];
	  for (int row = 0; row < height; ++row)
	  {
		  String s = rows[row];
	      for (int col = 0; col < width; ++col)
	      {
	    	Cell current = new Cell(row, col);
	        char c = s.charAt(col);
	        if (c == 'G')
	        {
	          current.setStatus(Status.GOLD);;
	        }
	        else if (c == 'P')
	        {
	          current.setStatus(Status.PIT);
	        }
	        else if (c == 'W')
	        {
	          current.setStatus(Status.WUMPUS);
	          start = current;
	        }
	        else 
	        {
	          current.setStatus(Status.EMPTY);
	        }  
	        cells[row][col] = current;
	        if(row == 0 && col == 0) start = current;
	      }
	    }
	  
	  for (int row = 0; row < height; ++row)
	  {
		  for (int col = 0; col < width; ++col)
		  {
			  Cell square = cells[row][col];
			  if (isAccessible(square))
			  {
				  // add edges from this to neighbors
				  if (row > 0 && isAccessible(cells[row - 1][col]))
				  {
					  square.addNeighbor(cells[row - 1][col]);
				  }
				  if (col > 0 && isAccessible(cells[row][col - 1]))
				  {
					  square.addNeighbor(cells[row][col - 1]);
				  }
				  if (row < height - 1 && isAccessible(cells[row + 1][col]))
				  {
					  square.addNeighbor(cells[row + 1][col]);
				  }
				  if (col < width - 1 && isAccessible(cells[row][col + 1]))
				  {
					  square.addNeighbor(cells[row][col + 1]);
				  }
			  }
		  }
	  }
  }
  
  public boolean[] getPercepts(int row, int col)
  {
	  Cell cell = cells[row][col];
	  
	  ArrayList<Cell> neighbors = cell.getNeighors();
	  percepts[0] = false;
	  percepts[1] = false;
	  percepts[2] = false;
	  
	  if(cell.getStatus() == Status.WUMPUS) percepts[0] = true;
	  if(cell.getStatus() == Status.GOLD) percepts[2] = true;
	  
	  for(Cell c : neighbors)
	  {
		  Status status = c.getStatus();
		  if(status == Status.WUMPUS)
		  {
			  percepts[0] = true;
		  }
		  else if(status == Status.PIT)
		  {
			  percepts[1] = true;
		  }
	  }
	  return percepts;
  }
  
  public boolean gameOver()
  {
	  return gameOver;
  }
  
  public void reset()
  {
	  heroLocation.setLocation(0, 0);
	  heroDirection = Direction.DOWN;
	  for(int i = 0; i < getRows(); i++)
	  {
		  for(int j = 0; j < getColumns(); j++)
		  {
			cells[i][j].setStatus(Status.EMPTY);
		  }
	  }
	  gameOver = false;
	  for(int i = 0; i < percepts.length; i++)
	  {
		  percepts[i] = false;
	  }
  }
  
  public Point getHeroLocation()
  {
	  return heroLocation;
  }
  
  public Direction getHeroDirection()
  {
	  return heroDirection;
  }
  
  public Result advanceBoard(Decision decision)
  {
	  if(decision == Decision.GRAB)
	  {  
		  int r = heroLocation.x;
		  int c = heroLocation.y;
		  if(cells[r][c].getStatus() == Status.GOLD)
		  {
			  cells[r][c].setStatus(Status.EMPTY);
			  return new Result("Grab decision", true, "The hero grabbed the gold.");
		  }
		  else
		  {
			  return new Result("Grab decision", false, "The hero attempted to grab the gold, but there was no gold to be found");
		  }  
	  }
	  else if(decision == Decision.FORWARD)
	  {  
		  Point dest = moveForward();
		  int r = dest.x;
		  int c = dest.y;
		  System.out.println("trying to go here: " + r + "," + c);
		  if(r < 0 || r > cells.length - 1 || c < 0 || c > cells[0].length - 1)
		  {
			  percepts[3] = true;
			  return new Result("Move decision", false, "The hero clumsily ran into a wall");
		  }
		  else
		  {
			  if(cells[r][c].getStatus() == Status.PIT || cells[r][c].getStatus() == Status.WUMPUS)
			  {
				  Result result = null;
				  if(cells[r][c].getStatus() == Status.PIT)
				  {
					  result = new Result("Move decision", false, "The hero fell for what seemed liked forever to his death");
				  }
				  else
				  {
					  result = new Result("Move decision", false, "The hero met his grisly end by the hands of the Wumpus");
				  }
				  heroLocation.setLocation(-1, -1);
				  cells[r][c].setStatus(Status.DEAD_HERO);
				  gameOver = true;
				  return result;
			  }
			  else
			  {
				  heroLocation.setLocation(r, c);
				  percepts[3] = false;
				  return new Result("Move decision", true, "The hero explored even deeper into the cave.");
			  }
		  }
	  }
	  else if(decision == Decision.TURN_LEFT)
	  {
		  updateHeroDirection(decision);
		  percepts[3] = false;
		  return new Result("Turn decision", true, "The hero decided that the left path was the better option");
	  }
  	  else if(decision == Decision.TURN_RIGHT)
  	  {
  		  updateHeroDirection(decision);
  		  percepts[3] = false;
  		  return new Result("Turn decision", true, "The hero decided that the right path was the better option");
  	  }
	  else if(decision == Decision.CLIMB)
	  {
		  if(heroLocation.x == 0 && heroLocation.y == 0)
		  {
			  gameOver = true;
			  return new Result("Climb decision", true, "The hero has escaped the cave with the gold. He will no doubt spend the gold wisely.");
		  }
		  else
		  {
			  return new Result("Climb decision", false, "The hero must be confused. There is no hole to climb out of here.");
		  }
	  }
	  return null;
  }
  
  private void updateHeroDirection(Decision turn)
  {
	  if(turn == Decision.TURN_LEFT)
	  {
		  if(heroDirection == Direction.UP)
		  {
			  heroDirection = Direction.LEFT;
		  }
		  else if(heroDirection == Direction.DOWN)
		  {
			  heroDirection = Direction.RIGHT;
		  }
		  else if(heroDirection == Direction.LEFT)
		  {
			  heroDirection = Direction.DOWN;
		  }
		  else
		  {
			  heroDirection = Direction.UP;
		  }
	  }
	  else
	  {
		  if(heroDirection == Direction.UP)
		  {
			  heroDirection = Direction.RIGHT;
		  }
		  else if(heroDirection == Direction.DOWN)
		  {
			  heroDirection = Direction.LEFT;
		  }
		  else if(heroDirection == Direction.LEFT)
		  {
			  heroDirection = Direction.UP;
		  }
		  else
		  {
			  heroDirection = Direction.DOWN;
		  }
	  }
  }
  
  private Point moveForward()
	{
		int row = heroLocation.x;
		int col = heroLocation.y;
		
		if(heroDirection == Direction.UP)
		{
			return new Point(row - 1, col);
		}
		else if(heroDirection == Direction.DOWN)
		{
			return new Point(row + 1, col);
		}
		else if(heroDirection == Direction.LEFT)
		{
			return new Point(row, col - 1);
		}
		else
		{
			return new Point(row, col + 1);
		}
	}
  
  public void setCellStatus(int row, int col, Status status)
  {
  
	  cells[row][col].setStatus(status);
  }
 
  public Cell getCell(int row, int col)
  {
    return cells[row][col];
  }
  
  public int getRows()
  {
    return cells.length;
  }
  
  public int getColumns()
  {
    return cells[0].length;
  }
  
  public Cell getStart()
  {
    return start;
  }
  
  public boolean isAccessible(Point point)
  {
	  int row = getRows();
	  int col = getColumns();
	  
	  return !(point.x < 0) && !(point.x > (row - 1)) && !(point.y < 0) && !(point.y > (col - 1));
  }

  private boolean isAccessible(Cell cell)
  {
    return cell != null;
  }
  
  @Override
  public String toString()
  {
	  StringBuilder sb = new StringBuilder();
	  if(!gameOver())
	  {
		 sb.append("Hero location: (" + heroLocation.x + ", " + heroLocation.y + ")\n");
		 sb.append("Percepts: ");
		 getPercepts(heroLocation.x, heroLocation.y);
		 if(percepts[0])
		 {
			 sb.append("Stench ");
		 }
		 if(percepts[1])
		 {
			 sb.append("Breeze ");
		 }
		 if(percepts[2])
		 {
			 sb.append("Glitter ");
		 }
		 if(percepts[3])
		 {
			 sb.append("Bump ");
		 }
		 if(percepts[4])
		 {
			 sb.append("Scream ");
		 }
	 }
	 else
	 {
		 sb.append("The game is over");
	 }
	 return sb.toString();
  }
  
  public class Result
  {
	  private String decision;
	  private boolean successful;
	  private String outcome;
	  
	  public Result(String decision, boolean successful, String outcome)
	  {
		  this.decision = decision;
		  this.successful = successful;
		  this.outcome = outcome;
	  }
	  
	  @Override
	  public String toString()
	  {
		  return "Decision: " + decision + "\nSuccessful: " + (successful ? "Yes" : "No") + "\nOutcome: " + outcome; 
	  }
  }
}
