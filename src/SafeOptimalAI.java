
public class SafeOptimalAI extends Player
{
	Minimaxer myMinimaxer;
	int depth;
	
	public SafeOptimalAI(int maxDepth, GameDataManager dataManager)
	{
		myMinimaxer = new Minimaxer(maxDepth, dataManager);
		depth = maxDepth;
	}

	@Override
	public int getMove(Game g)
	{
		// Not sure if this implementation is good for the ai. probably fine tho.
		return myMinimaxer.getSafestMove(g);
	}

	@Override
	public String getName()
	{
		return "Minimax";
	}

	@Override
	public String getInfo()
	{
		return "Depth " + Integer.toString(depth);
	}

	@Override
	public String getRawName()
	{
		return "minimax";
	}

	@Override
	public String getRawInfo()
	{
		return Integer.toString(depth);
	}

}
