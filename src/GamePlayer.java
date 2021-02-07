import java.util.Scanner;

public class GamePlayer
{ //TODO: make chess and maybe checkers.
	//TODO: make an engine that shows all available lines and their ratings.
	//TODO: create a trainer?
	//TODO: create an analysis engine?
	public static void main(String[] args)
	{

		Game myGame = new Mancala();

		GameManager gameManager = new GameManager(myGame);
		gameManager.initialize(IOMethod.GUI);

		gameManager.playGame();

	}

	public static void expand(Game game)
	{
		for (int move : game.getLegalMoves())
		{
			Game newGame = game.clone();
			newGame.makeMove(move);
			if (newGame.isOver() && newGame.getScore(1) > 40)
				System.out.println(newGame.getMoveString() + "  " + newGame.getScore(1));
			else
			{
				if (newGame.getTurnPlayer() == 1)
					expand(newGame);
			}
		}
	}
}
