import java.util.Scanner;

public class HumanPlayer extends Player
{

	private final String myName;

	public HumanPlayer(String playerName)
	{
		myName = playerName;
	}

	@Override
	public int getMove(Game playingGame)
	{
		IOManager.output(myName, OutputType.WHOSE_TURN);
		StringBuilder output = new StringBuilder();
		for (int move : playingGame.getLegalMoves())
		{
			output.append(playingGame.translateMoveIntToEnglish(move)).append(", ");
		}
		output = new StringBuilder(output.substring(0, output.length() - 2));
		IOManager.output(output.toString(), OutputType.MOVE_OPTIONS);

		String moveString;

		moveString = IOManager.getNextInput();
		int moveInt = playingGame.translateMoveStringToInt(moveString);
		boolean valid = moveInt != -1;
		while (! valid)
		{
			System.err.print("\nInvalid move inputted. Try again. ");
			moveString = IOManager.getNextInput();
			moveInt = playingGame.translateMoveStringToInt(moveString);
			valid = moveInt != -1;
		}
		return moveInt;
	}

	@Override
	public String getName()
	{
		return "Human";
	}

	@Override
	public String getInfo()
	{
		return myName;
	}

	@Override
	public String getRawName()
	{
		return "human";
	}

	@Override
	public String getRawInfo()
	{
		return myName;
	}

}
