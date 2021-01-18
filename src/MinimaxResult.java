import java.util.LinkedList;

public class MinimaxResult
{
	private int move;
	private int score;
	private boolean know;
	
	public MinimaxResult(int move, int score, boolean certain)
	{
		this.move = move;
		this.score = score;
		know = certain;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public int getMove()
	{
		return move;
	}

	public boolean getCertainty()
	{
		return know;
	}
}
