package wumpus;
import java.awt.Point;
import java.util.ArrayList;

public class Cell
{
  private ArrayList<Cell> neighbors;
  
  private Status status;
  
  private Point location;

  public Cell(int row, int col)
  {
    location = new Point(row, col);
  }
 
  public Point getLocation()
  {
    return location;
  }
  
  public void addNeighbor(Cell m)
  {
    if (neighbors == null)
    {
      neighbors = new ArrayList<Cell>();
    }
    neighbors.add(m);
  }
  
  public ArrayList<Cell> getNeighors()
  {
    return neighbors;
  }

  public void setStatus(Status s)
  {
    status = s;
  }

  public Status getStatus()
  {
    return status;
  }

  @Override
  public String toString()
  {
    return "(" + location.x + ", " + location.y + ")";
  }
}
