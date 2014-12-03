package wumpus;

import java.awt.Point;
import java.util.ArrayList;

public class GameBoard
{
  
  private Cell[][] cells;
  private Cell start;
  private Hero hero;
  private boolean gameOver;
  private boolean[] percepts;
  
  public GameBoard(int rows, int columns, DumbHero hero)
  {
	  this.hero = hero;
	  percepts = new boolean[5];
	  
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
  
  public boolean[] getPercepts()
  {
	  int row = (int) hero.getLocation().getX();
	  int col = (int) hero.getLocation().getY();
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
  
  public Hero getHero()
  {
	  return hero;
  }
  
  public void reset()
  {
	  for(int i = 0; i < getRows(); i++)
	  {
		  for(int j = 0; j < getColumns(); j++)
		  {
			cells[i][j].setStatus(Status.EMPTY);
		  }
	  }
	  gameOver = false;
	  hero.reset();
	  hero.foundGold(false);
	  hero.setLocation(0, 0);
	  hero.setDirection(Direction.DOWN);
	  for(int i = 0; i < percepts.length; i++)
	  {
		  percepts[i] = false;
	  }
  }
  
  public void updateHero(int r, int c)
  {
	  hero.setLocation(r, c);
  }
  
  public Result advanceBoard()
  {
	  Decision decision = hero.makeDecision(getPercepts());
	  if(decision instanceof GrabDecision)
	  {
		  if((boolean) decision.getDecision())
		  {
			  int r = (int) hero.getLocation().getX();
			  int c = (int) hero.getLocation().getY();
			  if(cells[r][c].getStatus() == Status.GOLD)
			  {
				  hero.foundGold(true);
				  cells[r][c].setStatus(Status.EMPTY);
				  return new Result("Grab decision", true, "The hero grabbed the gold.");
			  }
			  else
			  {
				  return new Result("Grab decision", false, "The hero attempted to grab the gold, but there was no gold to be found");
			  }
		  }  
	  }
	  else if(decision instanceof MoveDecision)
	  {
		  Point point = (Point) decision.getDecision();
		  int r = (int) point.getX();
		  int c = (int) point.getY();
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
				  hero.setLocation(-1, -1);
				  cells[r][c].setStatus(Status.DEAD_HERO);
				  gameOver = true;
				  return result;
			  }
			  else
			  {
				  updateHero(r, c);
				  percepts[3] = false;
				  return new Result("Move decision", true, "The hero explored even deeper into the cave.");
			  }
		  }
	  }
	  else if(decision instanceof TurnDecision)
	  {
		  Turn turn = (Turn) decision.getDecision();
		  updateHeroDirection(turn);
		  updateHero((int) hero.getLocation().getX(), (int) hero.getLocation().getY());
		  percepts[3] = false;
		  if(turn == Turn.LEFT)
		  {
			  return new Result("Turn decision", true, "The hero decided that the left path was the better option");
		  }
		  else
		  {
			  return new Result("Turn decision", true, "The hero decided that the right path was the better option");
		  }
	  }
	  else if(decision instanceof ClimbDecision)
	  {
		  if((boolean) decision.getDecision())
		  {
			  if(hero.getLocation().getX() == 0 && hero.getLocation().getY() == 0)
			  {
				  gameOver = true;
				  return new Result("Climb decision", true, "The hero has escaped the cave with the gold. He will no doubt spend the gold wisely.");
			  }
			  else
			  {
				  return new Result("Climb decision", false, "The hero must be confused. There is no hole to climb out of here.");
			  }
		  }
	  }
	  return null;
  }
  
  private void updateHeroDirection(Turn turn)
  {
	  Direction direction = hero.getDirection();
	  if(turn == Turn.LEFT)
	  {
		  if(direction == Direction.UP)
		  {
			  hero.setDirection(Direction.LEFT);
		  }
		  else if(direction == Direction.DOWN)
		  {
			  hero.setDirection(Direction.RIGHT);
		  }
		  else if(direction == Direction.LEFT)
		  {
			  hero.setDirection(Direction.DOWN);
		  }
		  else
		  {
			  hero.setDirection(Direction.UP);
		  }
	  }
	  else
	  {
		  if(direction == Direction.UP)
		  {
			  hero.setDirection(Direction.RIGHT);
		  }
		  else if(direction == Direction.DOWN)
		  {
			  hero.setDirection(Direction.LEFT);
		  }
		  else if(direction == Direction.LEFT)
		  {
			  hero.setDirection(Direction.UP);
		  }
		  else
		  {
			  hero.setDirection(Direction.DOWN);
		  }
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
		  sb.append("Hero location: (" + hero.getLocation().x + ", " + hero.getLocation().y + ")\n");
		  sb.append("Percepts: ");
		  getPercepts();
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
