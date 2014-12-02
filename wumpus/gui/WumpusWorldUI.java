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
import javax.swing.SwingUtilities;

import wumpus.Direction;
import wumpus.GameBoard;
import wumpus.DumbHero;
import wumpus.Simulator;
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
	
	private JButton okButton;
	
	private JComboBox<Integer> rowCB;
	
	private JComboBox<Integer> columnCB;
	
	private JComboBox<String> wumpusCB;
	
	private JButton okButton2;
	
	private JComboBox<String> goldCB;
	
	private JButton okButton3;
	
	private JComboBox<String> pitCB;
	
	private int pitsFilled;
	
	private JButton okButton4;
	
	private JButton start;
	
	private JLabel pitLabel;
	
	private int sleepTime;
	
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
		
		okButton = new JButton("OK");
		
		boardSize.add(rowLabel);
		boardSize.add(rowCB);
		boardSize.add(columnLabel);
		boardSize.add(columnCB);
		boardSize.add(okButton);
		
		okButton.addActionListener(new ActionListener()
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
				okButton2.setEnabled(true);
				rowCB.setEnabled(false);
				columnCB.setEnabled(false);
				okButton.setEnabled(false);
				repaint();
			}
		});
		
		JPanel wumpusLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
		wumpusLocation.setPreferredSize(new Dimension(290, 40));
		wumpusLocation.setMaximumSize(wumpusLocation.getPreferredSize());
		
		JLabel wumpusLabel = new JLabel("Wumpus Location: ");
		wumpusCB = new JComboBox<String>();
		
		okButton2 = new JButton("OK");
		
		wumpusLocation.add(wumpusLabel);
		wumpusLocation.add(wumpusCB);
		wumpusLocation.add(okButton2);
		wumpusCB.setEnabled(false);
		okButton2.setEnabled(false);
		
		okButton2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String[] selected = ((String) wumpusCB.getSelectedItem()).split(",");
				int row = Integer.parseInt(selected[0]);
				int col = Integer.parseInt(selected[1]);
				wumpus.setLocation(row, col);
				board.setCellStatus(row, col, Status.WUMPUS);
				wumpusCB.setEnabled(false);
				okButton2.setEnabled(false);
				fillGoldCombobox(board.getRows(), board.getColumns());
				goldCB.setEnabled(true);
				okButton3.setEnabled(true);
				repaint();
			}
		});
		
		JPanel goldLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
		goldLocation.setPreferredSize(new Dimension(290, 40));
		goldLocation.setMaximumSize(goldLocation.getPreferredSize());
		
		JLabel goldLabel = new JLabel("Gold Location: ");
		goldCB = new JComboBox<String>();
		
		okButton3 = new JButton("OK");
		
		goldLocation.add(goldLabel);
		goldLocation.add(goldCB);
		goldLocation.add(okButton3);
		goldCB.setEnabled(false);
		okButton3.setEnabled(false);
		
		okButton3.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String[] selected = ((String) goldCB.getSelectedItem()).split(",");
				int row = Integer.parseInt(selected[0]);
				int col = Integer.parseInt(selected[1]);
				gold.setLocation(row, col);
				board.setCellStatus(row, col, Status.GOLD);
				goldCB.setEnabled(false);
				okButton3.setEnabled(false);
				fillPitCombobox(board.getRows(), board.getColumns());
				pitCB.setEnabled(true);
				okButton4.setEnabled(true);
				repaint();
			}
		});
		
		JPanel pitLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pitLocation.setPreferredSize(new Dimension(290, 40));
		pitLocation.setMaximumSize(pitLocation.getPreferredSize());
		
		pitLabel = new JLabel("Pit 1 Location: ");
		pitCB = new JComboBox<String>();
		
		okButton4 = new JButton("OK");
		
		pitLocation.add(pitLabel);
		pitLocation.add(pitCB);
		pitLocation.add(okButton4);
		pitCB.setEnabled(false);
		okButton4.setEnabled(false);
		
		okButton4.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String[] selected = ((String) pitCB.getSelectedItem()).split(",");
				int row = Integer.parseInt(selected[0]);
				int col = Integer.parseInt(selected[1]);
				pits[pitsFilled] = new Point(row, col);
				pitsFilled++;
				board.setCellStatus(row, col, Status.PIT);
				if(pitsFilled == pits.length)
				{
					pitCB.setEnabled(false);
					okButton4.setEnabled(false);
					start.setEnabled(true);
				}
				else
				{
					pitLabel.setText("Pit " + (pitsFilled + 1) + " Location:");
					fillPitCombobox(board.getRows(), board.getColumns());
				}
				repaint();
			}
		});
		
		JPanel startButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		start = new JButton("Start");
		startButtons.add(start);
		start.setEnabled(false);
		start.addActionListener(new startButtonHandler());
	    
	    this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	    
	    JPanel controls = new JPanel();
	    controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
	    
	    controls.add(boardSize);
	    controls.add(wumpusLocation);
	    controls.add(goldLocation);
	    controls.add(pitLocation);
	    controls.add(startButtons);
	    
	    this.add(controls);
	    this.add(panel);
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
	
	private class startButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			new Thread()
			{
				public void run()
				{
					Simulator sim = new Simulator(board);
					while(!sim.gameOver())
					{
						sim.move();
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
}
