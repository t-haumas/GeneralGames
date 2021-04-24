import java.util.LinkedList;

public class MinimaxResult implements Comparable<MinimaxResult>
{
	private int move;
	private double score;
	private boolean know;
	
	public MinimaxResult(int move, double score, boolean certain)
	{
		this.move = move;
		this.score = score;
		know = certain;
	}
	
	public double getScore()
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

	@Override
	public int compareTo(MinimaxResult o) {
		return Double.compare(score, o.score);
	}
}
