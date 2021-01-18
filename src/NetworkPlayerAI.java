
public class NetworkPlayerAI extends Player
{
	Gomoku_3_3_Network myNetwork;

	public NetworkPlayerAI(Game game)
	{
		myNetwork = new Gomoku_3_3_Network((Gomoku) game);
	}
	
	@Override
	public int getMove(Game g)
	{
		return myNetwork.getGreatestOutput();
	}

	@Override
	public String getName() {
		return "Stupid thing";
	}

	@Override
	public String getInfo() {
		return "It's a neural network apparently.";
	}

	@Override
	public String getRawName()
	{
		return "network";
	}

	@Override
	public String getRawInfo()
	{
		return "idk";
	}
}
