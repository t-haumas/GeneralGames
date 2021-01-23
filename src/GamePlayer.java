import java.util.Scanner;

public class GamePlayer
{ //TODO: make chess and maybe checkers.
	//TODO: make an engine that shows all available lines and their ratings.
	//TODO: create a trainer?
	//TODO: create an analysis engine?
	public static void main(String[] args)
	{
//		if (args.length != 1)
//		{
//			System.err.println("Usage: java GamePlayer config_file_path");
//			System.exit(1);
//		}
//		String filepath = args[0];
//		Game myGame = new Chopsticks(2, false);
//		Game myGame = new Mancala(new int[]{4, 4, 0, 0, 6, 1, 3, 1, 2, 7, 7, 6, 6, 1}, 2);
		Game myGame = new Mancala();

		GameManager gameManager = new GameManager(myGame);
//		expand(myGame);

		//gameManager.initialize();
//		gameManager.playGame();

//		for (int p1Depth = 1; p1Depth < 14; p1Depth++)
//		{
//			for (int p2Depth = 1; p2Depth < 14; p2Depth++)
//			{
//				gamePlayManager.setPlayers("minimax, " + p1Depth + ", minimax, " + p2Depth);
//				gamePlayManager.setGame(new Mancala(new int[]{4, 4, 0, 0, 6, 1, 3, 1, 2, 7, 7, 6, 6, 1}, 2), "A3,A6,B2,B1,A4,");


//////		//T ODO: Every A after the b4 near the end.
//////		String[] moves = new String[]{"A3", "A6", "B2", "B1", "A5", "B1", "A1", "B3", "A1", "B6", "A1", "B4", "A1"};
//////		for (String move : moves)
//////		{
//////			myGame.makeMove(myGame.translateMoveStringToInt(move));
//////		}


//		gameManager.setPlayers("minimax, 12, minimax, 10");
		gameManager.initialize(IOMethod.GUI);
		gameManager.playGame();
//		for (int depth1 = 1; depth1 <= 12; depth1++) {
//			for (int depth2 = 1; depth2 <= 12; depth2++) {
//				for (String move : moves)
//				{
//					myGame.makeMove(myGame.translateMoveStringToInt(move));
//				}
//				IOManager.output("P1 depth: " + 12 + "   P2 depth: " + depth2);
//				gameManager.setPlayers("minimax, " + 12 + ", minimax, " + depth2);
//				gameManager.playGame();
//				gameManager.restartGame();
//			}

//	//	gameManager.printShortLines("A3,A6,B2,B1,A5,B1,A1,B3", 43);

//		}
//		gameManager.playGame();
//		gamePlayManager.deleteExtraneousForceableWinPositions();

//			}
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
