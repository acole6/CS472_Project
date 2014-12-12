//package wumpus.gui;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Point;
//import java.awt.TextArea;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextArea;
//import javax.swing.SwingUtilities;
//
//import wumpus.Direction;
//import wumpus.GameBoard;
//import wumpus.DepthSearchHero;
//import wumpus.Status;
//
///**
// * This is GUI for Wumpus World. The parameter passed into
// * start is the delay in milliseconds between when the state
// * of the world is updated, so decrease it to make it update
// * faster or increase it to make it update slower.
// * 
// * @author Alex Cole
// *
// */
//public class WumpusWorldUI extends JPanel
//{
//	/**
//	 * Entry point. Edit the delay here.
//	 */
//	public static void main(String[] args)
//	{ 
//	    WumpusWorldUI.start(1000);
//	}
//	
//	private GameBoard board;
//	private GameBoardPanel panel;
//	
//	private DepthSearchHero hero;
//	private List<DepthSearchHero.GamePath> gamePaths;
//	private int gamePathIndex = 0;
//	private int moveIndex = 0;
//	
//	private Point wumpus;
//	private Point gold;
//	private Point[] pits;
//	private int pitsFilled;
//	private int sleepTime;
//	
//	private JButton gridConfirmButton;
//	private JButton wumpusConfirmButton;
//	private JButton goldConfirmButton;
//	private JButton pitConfirmButton;
//	
//	private JComboBox<Integer> rowCB;
//	private JComboBox<Integer> columnCB;
//	private JComboBox<String> wumpusCB;
//	private JComboBox<String> pitCB;
//	private JComboBox<String> goldCB;
//	
//	private JButton play;
//	private JButton restart;
//	private JButton step;
//	
//	private JLabel pitLabel;
//	private JTextArea status;
//	
//	/**
//	 * Puts a task on the Swing event queue to instantiate
//	 * all the components. 
//	 * @param sleepTime
//	 * 		the delay between world updates
//	 */
//	public static void start(final int sleepTime)
//	{
//		Runnable r = new Runnable()
//		{
//			public void run()
//			{
//				createAndShow(sleepTime);
//			}
//		};
//		SwingUtilities.invokeLater(r);
//	}
//	
//	/**
//	 * Creates the enclosing frame and starts up the UI.
//	 * @param sleepTime
//	 * 		the delay between world updates
//	 */
//	private static void createAndShow(int sleepTime)
//	{
//		JFrame frame = new JFrame("Wumpus World");
//		frame.getContentPane().add(new WumpusWorldUI(sleepTime));
//		frame.pack();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//	}
//	
//	/**
//	 * Constructor sets up Swing components and initializes the world.
//	 * @param sleepTime
//	 * 		the delay between world updates
//	 */
//	private WumpusWorldUI(int sleepTime)
//	{
//		this.sleepTime = sleepTime;
//		this.board = new GameBoard(4, 4);
//		hero = null;
//		gamePaths = new ArrayList<DepthSearchHero.GamePath>();
//		
//		wumpus = new Point(0, 1);
//		gold = new Point(0, 2);
//		pits = new Point[board.getColumns() - 1];
//		pitsFilled = 0;
//		
//		panel = new GameBoardPanel(board);
//		panel.setPreferredSize(new Dimension(board.getRows() * GameBoardPanel.cellSize, board.getColumns() * GameBoardPanel.cellSize));
//		
//		JPanel boardSize = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		boardSize.setPreferredSize(new Dimension(290, 40));
//		boardSize.setMaximumSize(boardSize.getPreferredSize());
//		
//		Integer[] rowColumnRange = fillRowColumnCombobox();
//		JLabel rowLabel = new JLabel("Rows: ");
//		rowLabel.setVisible(true);
//		
//		rowCB = new JComboBox<Integer>(rowColumnRange);
//		
//		JLabel columnLabel = new JLabel("Columns: ");
//		columnLabel.setVisible(true);
//		columnCB = new JComboBox<Integer>(rowColumnRange);
//		
//		gridConfirmButton = new JButton("OK");
//		gridConfirmButton.addActionListener(new gridConfirmButtonHandler());
//		
//		boardSize.add(rowLabel);
//		boardSize.add(rowCB);
//		boardSize.add(columnLabel);
//		boardSize.add(columnCB);
//		boardSize.add(gridConfirmButton);
//		
//		JPanel wumpusLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		wumpusLocation.setPreferredSize(new Dimension(290, 40));
//		wumpusLocation.setMaximumSize(wumpusLocation.getPreferredSize());
//		
//		JLabel wumpusLabel = new JLabel("Wumpus Location: ");
//		wumpusCB = new JComboBox<String>();
//		wumpusCB.addActionListener(new wumpusCBHandler());
//		
//		wumpusConfirmButton = new JButton("OK");
//		wumpusConfirmButton.addActionListener(new wumpusConfirmButtonHandler());
//		
//		wumpusLocation.add(wumpusLabel);
//		wumpusLocation.add(wumpusCB);
//		wumpusLocation.add(wumpusConfirmButton);
//		wumpusCB.setEnabled(false);
//		wumpusConfirmButton.setEnabled(false);
//		
//		JPanel goldLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		goldLocation.setPreferredSize(new Dimension(290, 40));
//		goldLocation.setMaximumSize(goldLocation.getPreferredSize());
//		
//		JLabel goldLabel = new JLabel("Gold Location: ");
//		goldCB = new JComboBox<String>();
//		goldCB.addActionListener(new goldCBHandler());
//		
//		goldConfirmButton = new JButton("OK");
//		goldConfirmButton.addActionListener(new goldConfirmButtonHandler());
//		
//		goldLocation.add(goldLabel);
//		goldLocation.add(goldCB);
//		goldLocation.add(goldConfirmButton);
//		goldCB.setEnabled(false);
//		goldConfirmButton.setEnabled(false);
//		
//		JPanel pitLocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		pitLocation.setPreferredSize(new Dimension(290, 40));
//		pitLocation.setMaximumSize(pitLocation.getPreferredSize());
//		
//		pitLabel = new JLabel("Pit 1 Location: ");
//		pitCB = new JComboBox<String>();
//		pitCB.addActionListener(new pitCBHandler());
//		
//		pitConfirmButton = new JButton("OK");
//		pitConfirmButton.addActionListener(new pitConfirmButtonHandler());
//		
//		pitLocation.add(pitLabel);
//		pitLocation.add(pitCB);
//		pitLocation.add(pitConfirmButton);
//		pitCB.setEnabled(false);
//		pitConfirmButton.setEnabled(false);
//		
//		JPanel startButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		play = new JButton("Play");
//		startButtons.add(play);
//		play.setEnabled(false);
//		play.addActionListener(new playButtonHandler());
//		
//		step = new JButton("Step");
//		startButtons.add(step);
//		step.setEnabled(false);
//		step.addActionListener(new stepButtonHandler());
//		
//		restart = new JButton("Restart");
//		startButtons.add(restart);
//		restart.setEnabled(false);
//		restart.addActionListener(new restartButtonHandler());
//	    
//	    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
//	    
//	    JPanel controls = new JPanel();
//	    controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
//	    
//	    controls.add(boardSize);
//	    controls.add(wumpusLocation);
//	    controls.add(goldLocation);
//	    controls.add(pitLocation);
//	    controls.add(startButtons);
//	    
//	    JPanel game = new JPanel();
//	    game.setLayout(new BoxLayout(game, BoxLayout.LINE_AXIS));
//	    
//	    game.add(controls);
//	    game.add(panel);
//	    
//	    JPanel output = new JPanel(new BorderLayout());
//	    status = new JTextArea();
//	    status.setAlignmentX(TextArea.LEFT_ALIGNMENT);
//	    status.setLineWrap(true);
//	    status.setEditable(false);
//	    output.add(status, BorderLayout.CENTER);
//		output.setPreferredSize(new Dimension(this.getWidth(), 200));
//		output.setMaximumSize(boardSize.getPreferredSize());
//		
//	    this.add(game);
//	    this.add(output);
//	}
//	
//	/**
//	 * Fills the row and column comboboxes with the selectable row and column sizes
//	 * @return
//	 * 		An Integer array with the valid row and column selections
//	 */
//	protected Integer[] fillRowColumnCombobox()
//	{
//		Integer[] arr = new Integer[17];
//		for(int i = 0; i < arr.length; i++)
//		{
//			arr[i] = i + 4;
//		}
//		return arr;
//	}
//	
//	/**
//	 * Fills the wumpus location combobox with valid locations. The initial start spot
//	 * of the hero is skipped over.
//	 * @param rows
//	 * 		The number of rows in the board
//	 * @param cols
//	 * 		The number of columns in the board
//	 */
//	protected void fillWumpusCombobox(int rows, int cols)
//	{
//		wumpusCB.removeAllItems();
//		for(int i = 0; i < rows; i++)
//		{
//			for(int j = 0; j < cols; j++)
//			{
//				if(!(i == 0 && j == 0)) 
//				{
//					wumpusCB.addItem(i + "," + j);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * Fills the gold location combobox with valid locations. The initial start spot
//	 * of the hero and the start spot of wumpus are skipped over.
//	 * @param rows
//	 * 		The number of rows in the board
//	 * @param cols
//	 * 		The number of columns in the board
//	 */
//	protected void fillGoldCombobox(int rows, int cols)
//	{
//		goldCB.removeAllItems();
//		for(int i = 0; i < rows; i++)
//		{
//			for(int j = 0; j < cols; j++)
//			{
//				if(!(i == 0 && j == 0) && !(isWumpus(i, j)))
//				{
//					goldCB.addItem(i + "," + j);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * Fills the pit location combobox with valid locations. The initial start spot
//	 * of the hero, the start spot of the wumpus, the start spot of the gold, and any
//	 * pit already placed on the board are skipped over.
//	 * @param rows
//	 * 		The number of rows in the board
//	 * @param cols
//	 * 		The number of columns in the board.
//	 */
//	protected void fillPitCombobox(int rows, int cols)
//	{
//		pitCB.removeAllItems();
//		for(int i = 0; i < rows; i++)
//		{
//			for(int j = 0; j < cols; j++)
//			{
//				if(!(i == 0 && j == 0) && !(isWumpus(i, j)) && !(isGold(i, j)) && !(isPit(i, j)))
//				{
//					pitCB.addItem(i + "," + j);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * Checks to see if the wumpus occupies the given location.
//	 * @param row
//	 * 		The row of the cell
//	 * @param col
//	 * 		The column of the cell
//	 * @return
//	 * 		True if the wumpus occupies the cell; otherwise false
//	 */
//	private boolean isWumpus(int row, int col)
//	{
//		return row == (int)wumpus.getX() && col == (int)wumpus.getY();
//	}
//	
//	/**
//	 * Checks to see if the gold occupies the given location.
//	 * @param row
//	 * 		The row of the cell
//	 * @param col
//	 * 		The column of the cell
//	 * @return
//	 * 		True if the gold occupies the cell; otherwise false
//	 */
//	private boolean isGold(int row, int col)
//	{
//		return row == (int)gold.getX() && col == (int)gold.getY();
//	}
//	
//	/**
//	 * Checks to see if a pit occupies the given location.
//	 * @param row
//	 * 		The row of the cell
//	 * @param col
//	 * 		The column of the cell
//	 * @return
//	 * 		True if a pit is in the cell; otherwise false
//	 */
//	private boolean isPit(int row, int col)
//	{
//		for(Point pit : pits)
//		{
//			if(pit == null) return false;
//			if(row == (int)pit.getX() && col == (int)pit.getY()) return true;
//		}
//		return false;
//	}
//	
//	/**
//	 * Handler for button to confirm the board's number of rows and columns.
//	 */
//	private class gridConfirmButtonHandler implements ActionListener
//	{
//		
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			int rows = (int) rowCB.getSelectedItem();
//			int cols = (int) columnCB.getSelectedItem();
//			pits = new Point[cols - 1];
//			board = new GameBoard(rows, cols);
//			panel.setBoard(board);
//			fillWumpusCombobox(rows, cols);
//			wumpusCB.setEnabled(true);
//			wumpusConfirmButton.setEnabled(true);
//			rowCB.setEnabled(false);
//			columnCB.setEnabled(false);
//			gridConfirmButton.setEnabled(false);
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler for button to confirm the location of the Wumpus.
//	 */
//	private class wumpusConfirmButtonHandler implements ActionListener
//	{
//		
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			wumpusCB.setEnabled(false);
//			wumpusConfirmButton.setEnabled(false);
//			fillGoldCombobox(board.getRows(), board.getColumns());
//			goldCB.setEnabled(true);
//			goldConfirmButton.setEnabled(true);
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler for button to confirm the location of the gold.
//	 */
//	private class goldConfirmButtonHandler implements ActionListener
//	{
//		
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			goldCB.setEnabled(false);
//			goldConfirmButton.setEnabled(false);
//			fillPitCombobox(board.getRows(), board.getColumns());
//			pitCB.setEnabled(true);
//			pitConfirmButton.setEnabled(true);
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler for button to confirm the location of a pit.
//	 */
//	private class pitConfirmButtonHandler implements ActionListener
//	{
//		
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			pitsFilled++;
//			if(pitsFilled == pits.length)
//			{
//				pitCB.setEnabled(false);
//				pitConfirmButton.setEnabled(false);
//				play.setEnabled(true);
//				step.setEnabled(true);
//				status.setText(board.toString());
//				hero = new DepthSearchHero(board);
//				hero.solve();
//				gamePaths = hero.getPaths();
//			}
//			else
//			{
//				pitLabel.setText("Pit " + (pitsFilled + 1) + " Location:");
//				fillPitCombobox(board.getRows(), board.getColumns());
//			}
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler for the play button.
//	 */
//	private class playButtonHandler implements ActionListener
//	{
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			play.setEnabled(false);
//			step.setEnabled(false);
//			restart.setEnabled(true);
//			new Thread()
//			{
//				public void run()
//				{
//					DepthSearchHero.GamePath path = gamePaths.get(gamePathIndex);
//					while(!board.gameOver())
//					{
//						String result = board.advanceBoard(path.getDecisions().get(moveIndex)).toString();
//						status.setText(board.toString() + "\n" + result);
//						moveIndex++;
//						if(moveIndex > path.getDecisions().size() - 1)
//						{
//							break;
//						}
//						repaint();
//						try 
//						{
//							Thread.sleep(sleepTime);
//						} 
//						catch(InterruptedException e1) {}
//					}
//					gamePathIndex++;
//					if(gamePathIndex > gamePaths.size() - 1) 
//					{
//						gamePathIndex = gamePaths.size() - 1;
//					}
//					moveIndex = 0;
//					play.setEnabled(false);
//					step.setEnabled(false);
//				}
//			}.start();
//		}
//	}
//	
//	/**
//	 * Handler for the restart button.
//	 */
//	private class restartButtonHandler implements ActionListener
//	{
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			play.setEnabled(true);
//			step.setEnabled(true);
//			restart.setEnabled(false);
//			board.reset();
//			board.getCell((int) wumpus.getX(), (int) wumpus.getY()).setStatus(Status.WUMPUS);
//			board.getCell((int) gold.getX(), (int) gold.getY()).setStatus(Status.GOLD);
//			for(int i = 0; i < pits.length; i++)
//			{
//				Point pit = pits[i];
//				board.getCell((int) pit.getX(), (int) pit.getY()).setStatus(Status.PIT);
//			}
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler for the step button.
//	 */
//	private class stepButtonHandler implements ActionListener
//	{
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{	
//			play.setEnabled(true);
//			restart.setEnabled(true);
//			DepthSearchHero.GamePath path = gamePaths.get(gamePathIndex);
//			String result = board.advanceBoard(path.getDecisions().get(moveIndex)).toString();
//			status.setText(board.toString() + "\n" + result);
//			moveIndex++;
//
//			if(board.gameOver())
//			{
//				gamePathIndex++;
//				if(gamePathIndex > gamePaths.size() - 1)
//				{
//					gamePathIndex = gamePaths.size() - 1;
//				}
//				moveIndex = 0;
//				play.setEnabled(false);
//				step.setEnabled(false);
//			}
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler when a location is selected in the Wumpus combobox
//	 */
//	private class wumpusCBHandler implements ActionListener
//	{
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			String[] selected = ((String) wumpusCB.getSelectedItem()).split(",");
//			int row = Integer.parseInt(selected[0]);
//			int col = Integer.parseInt(selected[1]);
//			board.setCellStatus(wumpus.x, wumpus.y, Status.EMPTY);
//			wumpus.setLocation(row, col);
//			board.setCellStatus(row, col, Status.WUMPUS);
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler when a location is selected in the gold combobox
//	 */
//	private class goldCBHandler implements ActionListener
//	{
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			String[] selected = ((String) goldCB.getSelectedItem()).split(",");
//			int row = Integer.parseInt(selected[0]);
//			int col = Integer.parseInt(selected[1]);
//			board.setCellStatus(gold.x, gold.y, Status.EMPTY);
//			gold.setLocation(row, col);
//			board.setCellStatus(row, col, Status.GOLD);
//			repaint();
//		}
//	}
//	
//	/**
//	 * Handler when a location is selected in the pit combobox
//	 */
//	private class pitCBHandler implements ActionListener
//	{
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			String location = (String) pitCB.getSelectedItem();
//			if(location != null)
//			{
//				String[] selected = location.split(",");
//				int row = Integer.parseInt(selected[0]);
//				int col = Integer.parseInt(selected[1]);
//				if(pits[pitsFilled] == null)
//				{
//					pits[pitsFilled] = new Point(row, col);
//				}
//				else
//				{
//					board.setCellStatus(pits[pitsFilled].x, pits[pitsFilled].y, Status.EMPTY);
//					pits[pitsFilled].setLocation(row, col);
//				}
//				pits[pitsFilled].setLocation(row, col);
//				board.setCellStatus(row, col, Status.PIT);
//			}
//			repaint();
//		}
//	}
//}
