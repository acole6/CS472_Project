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
  
  public void updateHero(int r, int c)
  {
	  hero.setLocation(r, c);
	  cells[r][c].setStatus(cells[r][c].getStatus());
  }
  
  public boolean processHeroDecision(Decision decision)
  {
	  if(decision instanceof GrabDecision)
	  {
		  if((boolean) decision.getDecision())
		  {
			  int r = (int) hero.getLocation().getX();
			  int c = (int) hero.getLocation().getY();
			  if(cells[r][c].getStatus() == Status.GOLD)
			  {
				  hero.foundGold(true);
				  return true;
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
			  return false;
		  }
		  else
		  {
			  if(cells[r][c].getStatus() == Status.PIT || cells[r][c].getStatus() == Status.WUMPUS)
			  {
				  gameOver = true;
				  return false;
			  }
			  updateHero(r, c);
			  percepts[3] = false;
			  return true;
		  }
	  }
	  else if(decision instanceof TurnDecision)
	  {
		  updateHeroDirection((Turn) decision.getDecision());
		  updateHero((int) hero.getLocation().getX(), (int) hero.getLocation().getY());
		  percepts[3] = false;
		  return true;
	  }
	  else if(decision instanceof ClimbDecision)
	  {
		  if((boolean) decision.getDecision())
		  {
			  if(hero.getLocation().getX() == 0 && hero.getLocation().getY() == 0)
			  {
				  gameOver = true;
				  return true;
			  }
		  }
	  }
	  return false;
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
}
