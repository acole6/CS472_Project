package wumpus.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import wumpus.Direction;
import wumpus.GameBoard;
import wumpus.DumbHero;
import wumpus.Hero;
import wumpus.Status;

public class WumpusWorldUI extends JPanel
{
	public static void main(String[] args)
	{ 
	    WumpusWorldUI.start(500);
	}
	
	private GameBoard board;
	private GameBoardPanel panel;
	
	private Point wumpus;
	private Point gold;
	private Point[] pits;
	private int pitsFilled;
	private int sleepTime;
	private boolean paused = false;
	
	private JButton gridConfirmButton;
	private JButton wumpusConfirmButton;
	private JButton goldConfirmButton;
	private JButton pitConfirmButton;
	
	private JComboBox<Integer> rowCB;
	private JComboBox<Integer> columnCB;
	private JComboBox<String> wumpusCB;
	private JComboBox<String> pitCB;
	private JComboBox<String> goldCB;
	
	private JButton play;
	private JButton restart;
	private JButton step;
	private JButton pause;
	
	private JLabel pitLabel;
	private JTextArea status;
	
	private WumpusWorldUI(int sleepTime)
	{
		this.sleepTime = sleepTime;
		this.board = new GameBoard(4, 4, new DumbHero(Direction.DOWN));
		
		wumpus = new Point(0, 1);
		gold = new Point(0, 2);
		pits = new Point[board.getColumns() - 1];
		pitsFilled = 0;
		
		panel = new GameBoardPanel(board);
		panel.setPreferredSize(new Dimension(board.getRows() * GameBoardPanel.cellSize, board.getColumns() * GameBoardPanel.cellSize));
		
		JPanel boardSize = new JPanel(new FlowLayout(FlowLayout.LEFT));
		boardSize.setPreferredSize(new Dimension(290, 40));
		boardSize.setMaximumSize(boardSize.getPreferredSize());
		
		Integer[] rowColumnRange = fillRowColumnCombobox();
		JLabel rowLabel = new JLabel("Rows: ");
		rowLabel.setVisible(true);
		
		rowCB = new JComboBox<Integer>(rowColumnRange);
		
		JLabel columnLabel = new JLabel("Columns: ");
		columnLabel.setVisible(true);
		columnCB = new JComboBox<Integer>(rowColumnRange);
		
		gridConfirmButton = new JButton("OK");
		gridConfirmButton.addActionListener(new gridConfirmButtonHandler());
		
		boardSize.add(rowLabel);
		boardSize.add(rowCB);
		boardSize.add(columnLabel);
		boardSize.add(columnCB);
		boardSize.add(gridConfirmButton);
		
		JPanel wumpusLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
		wumpusLocation.setPreferredSize(new Dimension(290, 40));
		wumpusLocation.setMaximumSize(wumpusLocation.getPreferredSize());
		
		JLabel wumpusLabel = new JLabel("Wumpus Location: ");
		wumpusCB = new JComboBox<String>();
		wumpusCB.addActionListener(new wumpusCBHandler());
		
		wumpusConfirmButton = new JButton("OK");
		wumpusConfirmButton.addActionListener(new wumpusConfirmButtonHandler());
		
		wumpusLocation.add(wumpusLabel);
		wumpusLocation.add(wumpusCB);
		wumpusLocation.add(wumpusConfirmButton);
		wumpusCB.setEnabled(false);
		wumpusConfirmButton.setEnabled(false);
		
		JPanel goldLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
		goldLocation.setPreferredSize(new Dimension(290, 40));
		goldLocation.setMaximumSize(goldLocation.getPreferredSize());
		
		JLabel goldLabel = new JLabel("Gold Location: ");
		goldCB = new JComboBox<String>();
		goldCB.addActionListener(new goldCBHandler());
		
		goldConfirmButton = new JButton("OK");
		goldConfirmButton.addActionListener(new goldConfirmButtonHandler());
		
		goldLocation.add(goldLabel);
		goldLocation.add(goldCB);
		goldLocation.add(goldConfirmButton);
		goldCB.setEnabled(false);
		goldConfirmButton.setEnabled(false);
		
		JPanel pitLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pitLocation.setPreferredSize(new Dimension(290, 40));
		pitLocation.setMaximumSize(pitLocation.getPreferredSize());
		
		pitLabel = new JLabel("Pit 1 Location: ");
		pitCB = new JComboBox<String>();
		pitCB.addActionListener(new pitCBHandler());
		
		pitConfirmButton = new JButton("OK");
		pitConfirmButton.addActionListener(new pitConfirmButtonHandler());
		
		pitLocation.add(pitLabel);
		pitLocation.add(pitCB);
		pitLocation.add(pitConfirmButton);
		pitCB.setEnabled(false);
		pitConfirmButton.setEnabled(false);
		
		JPanel startButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		play = new JButton("Play");
		startButtons.add(play);
		play.setEnabled(false);
		play.addActionListener(new playButtonHandler());
		
		step = new JButton("Step");
		startButtons.add(step);
		step.setEnabled(false);
		step.addActionListener(new stepButtonHandler());
		
		pause = new JButton("Pause");
		startButtons.add(pause);
		pause.setEnabled(false);
		pause.addActionListener(new pauseButtonHandler());
		
		restart = new JButton("Restart");
		startButtons.add(restart);
		restart.setEnabled(false);
		restart.addActionListener(new restartButtonHandler());
	    
	    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	    
	    JPanel controls = new JPanel();
	    controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
	    
	    controls.add(boardSize);
	    controls.add(wumpusLocation);
	    controls.add(goldLocation);
	    controls.add(pitLocation);
	    controls.add(startButtons);
	    
	    JPanel game = new JPanel();
	    game.setLayout(new BoxLayout(game, BoxLayout.LINE_AXIS));
	    
	    game.add(controls);
	    game.add(panel);
	    
	    JPanel output = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    status = new JTextArea();
	    output.add(status);
		output.setPreferredSize(new Dimension(this.getWidth(), 200));
		output.setMaximumSize(boardSize.getPreferredSize());
		
	    this.add(game);
	    this.add(output);
	}
	
	public static void start(final int sleepTime)
	{
		Runnable r = new Runnable()
		{
			public void run()
			{
				createAndShow(sleepTime);
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	private static void createAndShow(int sleepTime)
	{
		JFrame frame = new JFrame("Wumpus World");
		frame.getContentPane().add(new WumpusWorldUI(sleepTime));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private Integer[] fillRowColumnCombobox()
	{
		Integer[] arr = new Integer[17];
		for(int i = 0; i < arr.length; i++)
		{
			arr[i] = i + 4;
		}
		return arr;
	}
	
	protected void fillWumpusCombobox(int rows, int cols)
	{
		wumpusCB.removeAllItems();
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				if(!(i == 0 && j == 0)) 
				{
					wumpusCB.addItem(i + "," + j);
				}
			}
		}
	}
	
	protected void fillGoldCombobox(int rows, int cols)
	{
		goldCB.removeAllItems();
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				if(!(i == 0 && j == 0) && !(isWumpus(i, j)))
				{
					goldCB.addItem(i + "," + j);
				}
			}
		}
	}
	
	private void fillPitCombobox(int rows, int cols)
	{
		pitCB.removeAllItems();
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				if(!(i == 0 && j == 0) && !(isWumpus(i, j)) && !(isGold(i, j)) && !(isPit(i, j)))
				{
					pitCB.addItem(i + "," + j);
				}
			}
		}
	}
	
	private boolean isWumpus(int row, int col)
	{
		return row == (int)wumpus.getX() && col == (int)wumpus.getY();
	}
	
	private boolean isGold(int row, int col)
	{
		return row == (int)gold.getX() && col == (int)gold.getY();
	}
	
	private boolean isPit(int row, int col)
	{
		for(Point pit : pits)
		{
			if(pit == null) return false;
			if(row == (int)pit.getX() && col == (int)pit.getY()) return true;
		}
		return false;
	}
	
	private class gridConfirmButtonHandler implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int rows = (int) rowCB.getSelectedItem();
			int cols = (int) columnCB.getSelectedItem();
			pits = new Point[cols - 1];
			board = new GameBoard(rows, cols, new DumbHero(Direction.DOWN));
			panel.setBoard(board);
			fillWumpusCombobox(rows, cols);
			wumpusCB.setEnabled(true);
			wumpusConfirmButton.setEnabled(true);
			rowCB.setEnabled(false);
			columnCB.setEnabled(false);
			gridConfirmButton.setEnabled(false);
			repaint();
		}
	}
	
	private class wumpusConfirmButtonHandler implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			wumpusCB.setEnabled(false);
			wumpusConfirmButton.setEnabled(false);
			fillGoldCombobox(board.getRows(), board.getColumns());
			goldCB.setEnabled(true);
			goldConfirmButton.setEnabled(true);
			repaint();
		}
	}
	
	private class goldConfirmButtonHandler implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			goldCB.setEnabled(false);
			goldConfirmButton.setEnabled(false);
			fillPitCombobox(board.getRows(), board.getColumns());
			pitCB.setEnabled(true);
			pitConfirmButton.setEnabled(true);
			repaint();
		}
	}
	
	private class pitConfirmButtonHandler implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			pitsFilled++;
			if(pitsFilled == pits.length)
			{
				pitCB.setEnabled(false);
				pitConfirmButton.setEnabled(false);
				play.setEnabled(true);
			}
			else
			{
				pitLabel.setText("Pit " + (pitsFilled + 1) + " Location:");
				fillPitCombobox(board.getRows(), board.getColumns());
			}
			repaint();
		}
	}
	
	
	private class playButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			play.setEnabled(false);
			restart.setEnabled(true);
			pause.setEnabled(true);
			paused = false;
			new Thread()
			{
				public void run()
				{
					while(!board.gameOver() && !paused)
					{
						Hero hero = board.getHero();
						board.advanceBoard();
						System.out.println("Hero location: " + (int) hero.getLocation().getX() + ", " + (int) hero.getLocation().getY());
						repaint();
						try 
						{
							Thread.sleep(sleepTime);
						} 
						catch(InterruptedException e1) {}
					}
				}
			}.start();
		}
	}
	
	private class restartButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			play.setEnabled(true);
			restart.setEnabled(false);
			pause.setEnabled(false);
			board.reset();
			board.getCell((int) wumpus.getX(), (int) wumpus.getY()).setStatus(Status.WUMPUS);
			board.getCell((int) gold.getX(), (int) gold.getY()).setStatus(Status.GOLD);
			for(int i = 0; i < pits.length; i++)
			{
				Point pit = pits[i];
				board.getCell((int) pit.getX(), (int) pit.getY()).setStatus(Status.PIT);
			}
			repaint();
		}
	}
	
	private class pauseButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			paused = true;
			play.setEnabled(true);
			step.setEnabled(true);
			restart.setEnabled(true);
		}
	}
	
	private class stepButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			play.setEnabled(true);
			pause.setEnabled(false);
			restart.setEnabled(true);
			Hero hero = board.getHero();
			board.advanceBoard();
			System.out.println("Hero location: " + (int) hero.getLocation().getX() + ", " + (int) hero.getLocation().getY());
			repaint();
		}
	}
	
	private class wumpusCBHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String[] selected = ((String) wumpusCB.getSelectedItem()).split(",");
			int row = Integer.parseInt(selected[0]);
			int col = Integer.parseInt(selected[1]);
			board.setCellStatus(wumpus.x, wumpus.y, Status.EMPTY);
			wumpus.setLocation(row, col);
			board.setCellStatus(row, col, Status.WUMPUS);
			repaint();
		}
	}
	
	private class goldCBHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String[] selected = ((String) goldCB.getSelectedItem()).split(",");
			int row = Integer.parseInt(selected[0]);
			int col = Integer.parseInt(selected[1]);
			board.setCellStatus(gold.x, gold.y, Status.EMPTY);
			gold.setLocation(row, col);
			board.setCellStatus(row, col, Status.GOLD);
			repaint();
		}
	}
	
	private class pitCBHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String location = (String) pitCB.getSelectedItem();
			if(location != null)
			{
				String[] selected = location.split(",");
				int row = Integer.parseInt(selected[0]);
				int col = Integer.parseInt(selected[1]);
				if(pits[pitsFilled] == null)
				{
					pits[pitsFilled] = new Point(row, col);
				}
				else
				{
					board.setCellStatus(pits[pitsFilled].x, pits[pitsFilled].y, Status.EMPTY);
					pits[pitsFilled].setLocation(row, col);
				}
				pits[pitsFilled].setLocation(row, col);
				board.setCellStatus(row, col, Status.PIT);
			}
			repaint();
		}
	}
}
