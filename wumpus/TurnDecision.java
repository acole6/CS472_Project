package wumpus;

public class TurnDecision implements Decision
{
	private Turn turn;
	
	public TurnDecision(Turn turn)
	{
		this.turn = turn;
	}

	@Override
	public Object getDecision()  
	{
		return turn;
	}
}
