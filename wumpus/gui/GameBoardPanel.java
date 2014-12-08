package wumpus.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import wumpus.Cell;
import wumpus.Direction;
import wumpus.GameBoard;

/**
 * Panel that determines how the game board for Wumpus World is displayed
 * @author Alex Cole
 *
 */
public class GameBoardPanel extends JPanel
{
  public static final int cellSize = 100;
  public static final String path = "C:/cs336/workspace/images/";
  private GameBoard board;
  
  /**
   * Constructor for the GameBoardPanel.
   * @param board
   * 	The game board
   */
  public GameBoardPanel(GameBoard board)
  {
    this.board = board;
  }
  
  /**
   * Updates the panel if a new board needs to be used.
   * @param board
   * 		The new game board to be used
   */
  public void setBoard(GameBoard board)
  {
	  this.board = board;
	  this.setPreferredSize(new Dimension(board.getRows() * cellSize, board.getColumns() * cellSize));
  }

  @Override
  public void paintComponent(Graphics g)
  {
	  g.clearRect(0, 0, getWidth(), getHeight());
	    
	  for (int row = 0; row < board.getRows(); ++row)
	  {
		  for (int col = 0; col < board.getColumns(); ++col)
	      {
			  int r = board.getHeroLocation().getLocation().x;
	    	  int c = board.getHeroLocation().getLocation().y;
	    	  BufferedImage image = null;
	    	  try 
	    	  {
	    		  if(r == row && c == col)
	    		  {
	    			  image = getHeroImage(board.getHeroDirection());
	    		  }
	    		  else
	    		  {
	    			  image = getImage(board.getCell(row, col));
	    		  }
	    	  }
	    	  catch (IOException e) {}
	    	  g.drawImage(image, col * cellSize, row * cellSize, cellSize, cellSize, null);
	      }
	  }
	    
	  // draw grid
	  g.setColor(Color.WHITE);
	  for (int row = 0; row < board.getRows(); ++row)
	  {
		  for (int col = 0; col < board.getColumns(); ++col)
	      {
	        g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
	      }
	  }
  }
    
  private BufferedImage getImage(Cell m) throws IOException
  {
	 if(m == null) return ImageIO.read(new File(path + "white.png"));
	 switch(m.getStatus())
	 {
	 	case WUMPUS: return ImageIO.read(new File(path + "wumpus.png"));
	  	case PIT: return ImageIO.read(new File(path + "pit.png"));
	  	case GOLD: return ImageIO.read(new File(path + "gold.png"));
	  	case EMPTY: return ImageIO.read(new File(path + "white.png"));
	  	case DEAD_HERO: return ImageIO.read(new File(path + "dead_hero.png"));
	  	case DEAD_WUMPUS: return ImageIO.read(new File(path + "dead_wumpus.png"));
	  }
	  return ImageIO.read(new File(path + "white.png"));
  }
  
  private BufferedImage getHeroImage(Direction direction) throws IOException
  {
	  if(direction == null) ImageIO.read(new File(path + "hero_down.png"));
	  switch(direction)
	  {
	  	case DOWN: return ImageIO.read(new File(path + "hero_down.png"));
	  	case UP: return ImageIO.read(new File(path + "hero_up.png"));
	  	case LEFT: return ImageIO.read(new File(path + "hero_left.png"));
	  	case RIGHT: return ImageIO.read(new File(path + "hero_right.png"));
	  }
	  return ImageIO.read(new File(path + "hero_down.png"));
  }
}