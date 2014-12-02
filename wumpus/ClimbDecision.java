package wumpus;

public class ClimbDecision implements Decision
{
	private boolean climb;
	
	public ClimbDecision(boolean climb)
	{
		this.climb = climb;
	}

	@Override
	public Object getDecision() 
	{
		return climb;
	}
}
