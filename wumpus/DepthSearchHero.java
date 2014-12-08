package wumpus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Will consider moving to left cell, right cell or forward cell as expanding the tree.
 * If the hero has to move backward, then the hero is going back up the tree
 * @author Alex Cole
 *
 */
public class DepthSearchHero extends AbstractHero
{
	public DepthSearchHero(GameBoard board) 
	{
		super(board);
	}
	
	@Override
	public void solve() 
	{
		open.add(new SearchNode(start, null));
		SearchNode currentNode = null;
		List<Point> explore = new ArrayList<Point>();
		List<Decision> decisions = new ArrayList<Decision>();
		while(!open.isEmpty())
		{
			currentNode = pop(open);
			if(ranIntoWall)
			{
				currentNode.setDecisions(goToLocation(currentNode.getParent(), currentNode));
				ranIntoWall = false;
			}
			if(board.isAccessible(currentNode.getLocation()))
			{
				Status status = board.getCell(currentNode.getLocation().x, currentNode.getLocation().y).getStatus();
				if(status == Status.GOLD)
				{
					explore = currentNode.getParent().getPath();
					addToDecisions(currentNode, decisions);
					decisions.add(Decision.GRAB);
					break;
				}
				else if(status == Status.PIT)
				{
					pit.add(currentNode.toString());
					explore = currentNode.getPath();
					addToDecisions(currentNode, decisions);
					paths.add(new GamePath(explore, decisions));
					decisions = new ArrayList<Decision>();
					setToInitial(currentNode.getParent());
				}
				else if(status == Status.WUMPUS)
				{
					wumpus = currentNode.toString();
					explore = currentNode.getPath();
					addToDecisions(currentNode, decisions);
					paths.add(new GamePath(explore, decisions));
					decisions = new ArrayList<Decision>();
					setToInitial(currentNode.getParent());
				}
				else
				{
					exploring.add(currentNode.toString());
					addNewChildrenToOpen(currentNode);
				}
			}
			else
			{
				currentNode.getParent().addToDecisions(goToLocation(currentNode.getParent(), currentNode));
				wall.add(currentNode.toString());
				ranIntoWall = true;
			}
		}
		
		open.clear();
		exploring.clear();
		open.add(new SearchNode(currentNode.getLocation(), null, currentNode.getDirection()));
		while(!open.isEmpty())
		{
			List<Point> exit = new ArrayList<Point>();
			List<Decision> exitDecisions = new ArrayList<Decision>();
			currentNode = pop(open);
			if(ranIntoWall)
			{
				currentNode.setDecisions(goToLocation(currentNode.getParent(), currentNode));
				ranIntoWall = false;
			}
			if(board.isAccessible(currentNode.getLocation()))
			{
				Status status = board.getCell(currentNode.getLocation().x, currentNode.getLocation().y).getStatus();
				if(currentNode.getLocation().equals(start))
				{	
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					List<Decision> ret = new ArrayList<Decision>();
					ret.addAll(decisions);
					addToDecisions(currentNode, exitDecisions);
					ret.addAll(exitDecisions);
					ret.add(Decision.CLIMB);
					paths.add(new GamePath(exit, ret));
					break;
				}
				else if(status == Status.WUMPUS)
				{
					wumpus = currentNode.toString();
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					List<Decision> ret = new ArrayList<Decision>();
					ret.addAll(decisions);
					addToDecisions(currentNode, exitDecisions);
					ret.addAll(exitDecisions);
					paths.add(new GamePath(exit, ret));
					setToInitial(currentNode.getParent());
				}
				else if(status == Status.PIT)
				{
					pit.add(currentNode.toString());
					exit.addAll(explore);
					exit.addAll(currentNode.getPath());
					List<Decision> ret = new ArrayList<Decision>();
					ret.addAll(decisions);
					addToDecisions(currentNode, exitDecisions);
					ret.addAll(exitDecisions);
					paths.add(new GamePath(exit, ret));
					setToInitial(currentNode.getParent());
				}
				else
				{
					exploring.add(currentNode.toString());
					addNewChildrenToOpen(currentNode);
				}
			}
			else
			{
				currentNode.getParent().addToDecisions(goToLocation(currentNode.getParent(), currentNode));
				wall.add(currentNode.toString());
				ranIntoWall = true;
			}
		}
	}
	
	private void setToInitial(SearchNode node)
	{
		while(node != null)
		{
			node.setToInitial();
			node = node.getParent();
		}
	}
	
	private void addToDecisions(SearchNode node, List<Decision> decisions)
	{
		while(node != null)
		{
			decisions.addAll(0, node.getDecisions());
			node = node.getParent();
		}
	}
	
	private List<Decision> goToLocation(SearchNode start, SearchNode end)
	{	
		int row = start.getLocation().x - end.getLocation().x;
		int col = start.getLocation().y - end.getLocation().y;
		
		List<Decision> decisions = new ArrayList<Decision>();
		Direction direction = start.getDirection();
		while(true)
		{
			if(row < 0 && col == 0)
			{
				if(direction == Direction.UP)
				{
					decisions.add(Decision.TURN_LEFT);
					direction = Direction.LEFT;
				}
				else if(direction == Direction.DOWN)
				{
					start.setDirection(direction);
					end.setDirection(direction);
					end.setDecisions(decisions);
					decisions.add(Decision.FORWARD);
					return decisions;
				}
				else if(direction == Direction.LEFT)
				{
					decisions.add(Decision.TURN_LEFT);
					direction = Direction.DOWN;
				}
				else
				{
					decisions.add(Decision.TURN_RIGHT);
					direction = Direction.DOWN;
				}
			}
			else if(row > 0 && col == 0)
			{
				if(direction == Direction.UP)
				{
					start.setDirection(direction);
					end.setDirection(direction);
					end.setDecisions(decisions);
					decisions.add(Decision.FORWARD);
					return decisions;
				}
				else if(direction == Direction.DOWN)
				{
					decisions.add(Decision.TURN_LEFT);
					direction = Direction.RIGHT;
				}
				else if(direction == Direction.LEFT)
				{
					decisions.add(Decision.TURN_RIGHT);
					direction = Direction.UP;
				}
				else
				{
					decisions.add(Decision.TURN_LEFT);
					direction = Direction.UP;
				}
			}
			else if(row == 0 && col < 0)
			{
				if(direction == Direction.UP)
				{
					decisions.add(Decision.TURN_RIGHT);
					direction = Direction.RIGHT;
				}
				else if(direction == Direction.DOWN)
				{
					decisions.add(Decision.TURN_LEFT);
					direction = Direction.RIGHT;
				}
				else if(direction == Direction.LEFT)
				{
					decisions.add(Decision.TURN_RIGHT);
					direction = Direction.UP;
				}
				else
				{
					start.setDirection(direction);
					end.setDirection(direction);
					end.setDecisions(decisions);
					decisions.add(Decision.FORWARD);
					return decisions;
				}
			}
			else
			{
				if(direction == Direction.UP)
				{
					decisions.add(Decision.TURN_LEFT);
					direction = Direction.LEFT;
				}
				else if(direction == Direction.DOWN)
				{
					decisions.add(Decision.TURN_RIGHT);
					direction = Direction.LEFT;
				}
				else if(direction == Direction.LEFT)
				{
					start.setDirection(direction);
					end.setDirection(direction);
					end.setDecisions(decisions);
					decisions.add(Decision.FORWARD);
					return decisions;
				}
				else
				{
					decisions.add(Decision.TURN_LEFT);
					direction = Direction.UP;
				}
			}
		}
	}
	
	private void addNewChildrenToOpen(SearchNode parent)
	{
		Point location = parent.getLocation();
		SearchNode node = createSearchNode(location.x, location.y - 1, parent, Direction.LEFT);
		if(canAdd(node.toString()))
		{
			node.setInitialDecisions(goToLocation(parent, node));
			open.add(node);
			parent.setToInitial();
		}
		
		node = createSearchNode(location.x + 1, location.y, parent, Direction.DOWN);
		if(canAdd(node.toString()))
		{	
			node.setInitialDecisions(goToLocation(parent, node));
			open.add(node);
			parent.setToInitial();
		}
		
		node = createSearchNode(location.x, location.y + 1, parent, Direction.RIGHT);
		if(canAdd(node.toString()))
		{
			node.setInitialDecisions(goToLocation(parent, node));
			open.add(node);
			parent.setToInitial();
		}
		
		node = createSearchNode(location.x - 1, location.y, parent, Direction.UP);
		if(canAdd(node.toString()))
		{
			node.setInitialDecisions(goToLocation(parent, node));
			open.add(node);
			parent.setToInitial();
		}
	}
		
	private SearchNode createSearchNode(int row, int col, SearchNode parent, Direction direction)
	{
		return new SearchNode(new Point(row, col), parent, direction);
	}
	
	@Override
	protected SearchNode pop(LinkedList<SearchNode> list)
	{
		return list.removeLast();
	}
}
