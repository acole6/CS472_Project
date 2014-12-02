package wumpus;

public class GrabDecision implements Decision
{
	private boolean grab;

	public GrabDecision(boolean grab)
	{
		this.grab = grab;
	}

	@Override
	public Object getDecision() 
	{
		return grab;
	}
}
