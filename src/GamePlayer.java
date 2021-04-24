import java.util.Scanner;

public class GamePlayer
{
	public static void main(String[] args)
	{
//		try {
		Game myGame = new Mancala();

		myGame.makeMove(myGame.translateMoveStringToInt("A6"));
		myGame.makeMove(myGame.translateMoveStringToInt("B6"));


		GameManager gameManager = new GameManager(myGame);
		gameManager.initialize(IOMethod.GUI);

		gameManager.playGame();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

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
