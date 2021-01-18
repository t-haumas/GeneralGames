import java.util.LinkedList;

public class Gomoku_3_3_Network extends Network
{
	public Gomoku myGame;

	public Gomoku_3_3_Network(Gomoku gomoku33Game)
	{
		super(9, 9);
		myGame = gomoku33Game;
	}

	@Override
	protected LinkedList<Double> getExternalInputs()
	{
		LinkedList<Double> spaceInputs = new LinkedList<Double>();
		int[][] spaces = myGame.getSpaces();
		
		for (int row = 0; row < 3; row++)
		{
			for (int column = 0; column < 3; column++)
			{
				spaceInputs.add((double) spaces[row][column]);
			}
		}
		return spaceInputs;
	}
	
}
