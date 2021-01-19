import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Minimaxer
{
	private int optimizingPlayer;
	private int maxDiscoveredDepth;
	private final int depthLimit;
	private final GameDataManager dataManager;

	public Minimaxer(int maxDepth, GameDataManager dataManager)
	{
		optimizingPlayer = 0;
		this.depthLimit = maxDepth;
		this.dataManager = dataManager;
	}

	/**
	 * Returns the safest move sequence for the turnPlayer, assuming all other
	 * players are working against the turnPlayer.
	 * Calls minimax, with the turnPlayer being the optimizing player.
	 *
	 * @param game - The Game being played.
	 * @return an Linked List representing the moves the turnPlayer should make.
	 */
	public int getSafestMove(Game game)
	{
		optimizingPlayer = game.getTurnPlayer();
		maxDiscoveredDepth = 0;
		return minimaxDepthLimit(game, 0).getMove();
	}

	/**
	 * Returns the safest move  for the turnPlayer, assuming all other
	 * players are working against the turnPlayer.
	 * Calls minimax, with the turnPlayer being the optimizing player.
	 *
	 * @param game - The Game being played.
	 * @return an Linked List representing the moves the turnPlayer should make.
	 */
	public MinimaxResult getSafestMoveAndInfo(Game game)
	{
		optimizingPlayer = game.getTurnPlayer();
		maxDiscoveredDepth = 0;
		return minimaxDepthLimit(game, 0);
	}

	public MinimaxResult minimaxDepthLimit(Game gamePlaying, int parentDepth)
	{
		//TODO: add options for moves, rather than just one. >= in the game over block.

		//<editor-fold desc = "Setup">
		boolean certain = true;
		int myDepth = parentDepth + 1;
		if (myDepth > maxDiscoveredDepth)
		{
			maxDiscoveredDepth = myDepth;
		}

		int extremeMove = -1;
		int extremeScore;
		int currentScore;
		Game trialGame;
		int currentTurnPlayer = gamePlaying.getTurnPlayer();
		//</editor-fold>

		if (currentTurnPlayer == optimizingPlayer)
			extremeScore = Integer.MIN_VALUE;
		else
			extremeScore = Integer.MAX_VALUE;

		List<Integer> allMoveOptions = gamePlaying.getLegalMoves();

		for (int move : allMoveOptions)
		{
			trialGame = gamePlaying.clone();
			trialGame.makeMove(move);

			if (trialGame.isOver())
			{
				currentScore = trialGame.getScore(optimizingPlayer) - getMaxOpponentScore(trialGame);
			}
			else if (dataManager.knowMyOutcome(trialGame, optimizingPlayer))
			{
				GameInfo outcome = dataManager.getOutcome(trialGame);
				currentScore = outcome.getWinningPlayersScore();
			}
			else {
				if (myDepth >= depthLimit) {
					// This could be changed.
					currentScore = trialGame.getScore(optimizingPlayer) - getMaxOpponentScore(trialGame);
					certain = false;
				} else {
					MinimaxResult nextResult = minimaxDepthLimit(trialGame, myDepth);
					currentScore = nextResult.getScore();
					if (! nextResult.getCertainty())
					{
						certain = false;
					}
				}
			}

			// Update extreme score
			if (currentTurnPlayer == optimizingPlayer)
			{
				if (currentScore > extremeScore)
				{
					extremeScore = currentScore;
					extremeMove = move;
				}
			}
			else
			{
				if (currentScore < extremeScore)
				{
					extremeScore = currentScore;
					extremeMove = move;
				}
			}
		}
		if (certain && extremeScore > 0)
		{
			dataManager.saveDeterminedLine(gamePlaying.getMoveString(), optimizingPlayer, extremeScore);
		}
		return new MinimaxResult(extremeMove, extremeScore, certain);
	}

	/* AB pruning version: */
//	public MinimaxResult minimaxDepthLimit(Game gamePlaying, int parentDepth, int alpha, int beta)
//	{
//
//		//<editor-fold desc = "Setup">
//		boolean certain = true;
//		int myDepth = parentDepth + 1;
//		if (myDepth > maxDiscoveredDepth)
//		{
//			maxDiscoveredDepth = myDepth;
//		}
//		int bestDiscoveredScoreForMaximizer = alpha;
//		int bestDiscoveredScoreForMinimizer = beta;
//
//		int extremeMove = -1;
//		int extremeScore;
//		int currentScore;
//		Game trialGame;
//		int currentTurnPlayer = gamePlaying.getTurnPlayer();
//		//</editor-fold>
//
//		if (currentTurnPlayer == optimizingPlayer)
//			extremeScore = Integer.MIN_VALUE;
//		else
//			extremeScore = Integer.MAX_VALUE;
//
//		List<Integer> allMoveOptions = gamePlaying.getLegalMoves();
//
//		for (int move : allMoveOptions)
//		{
//			trialGame = gamePlaying.clone();
//			trialGame.makeMove(move);
//
//			if (trialGame.isOver())
//			{
//				currentScore = trialGame.getScore(optimizingPlayer) - getMaxOpponentScore(trialGame);
//			}
//			else if (dataManager.knowMyOutcome(trialGame, optimizingPlayer))
//			{
//				GameInfo outcome = dataManager.getOutcome(trialGame);
//				currentScore = outcome.getWinningPlayersScore();
//			}
//			else {
//				if (myDepth >= depthLimit) {
//					// This could be changed.
//					currentScore = trialGame.getScore(optimizingPlayer) - getMaxOpponentScore(trialGame);
//					certain = false;
//				} else {
//					MinimaxResult nextResult = minimaxDepthLimit(trialGame, myDepth, bestDiscoveredScoreForMaximizer, bestDiscoveredScoreForMinimizer);
//					currentScore = nextResult.getScore();
//					if (! nextResult.getCertainty())
//					{
//						certain = false;
//					}
//				}
//			}
//
//			if (currentScore % 2 != 0 && certain)
//			{
//				System.err.println("oh no! The minimax has a problem.");
//			}
//
//			// Update extreme score
//			if (currentTurnPlayer == optimizingPlayer)
//			{
//				if (currentScore > extremeScore)
//				{
//					extremeScore = currentScore;
//					extremeMove = move;
//				}
//				bestDiscoveredScoreForMaximizer = Math.max(bestDiscoveredScoreForMaximizer, currentScore);
//				if (bestDiscoveredScoreForMinimizer <= bestDiscoveredScoreForMaximizer)
//				{
//					break;
//				}
//			}
//			else
//			{
//				if (currentScore < extremeScore)
//				{
//					extremeScore = currentScore;
//					extremeMove = move;
//				}
//				bestDiscoveredScoreForMinimizer = Math.max(bestDiscoveredScoreForMinimizer, currentScore);
//				if (bestDiscoveredScoreForMinimizer <= bestDiscoveredScoreForMaximizer)
//				{
//					break;
//				}
//			}
//		}
//		if (certain && extremeScore > 0)
//		{
//			dataManager.saveDeterminedLine(gamePlaying.getMoveString(), optimizingPlayer, extremeScore);
//		}
//		return new MinimaxResult(extremeMove, extremeScore, certain);
//	}


	private int getMaxOpponentScore(Game game)
	{
		ArrayList<Integer> otherScores = new ArrayList<>();
		int currentPlayer = 1;
		if (currentPlayer != optimizingPlayer)
		{
			otherScores.add(game.getScore(currentPlayer));
		}
		currentPlayer = game.getNextPlayer(currentPlayer);
		while (currentPlayer != 1)
		{
			if (currentPlayer != optimizingPlayer)
			{
				otherScores.add(game.getScore(currentPlayer));
			}
			currentPlayer = game.getNextPlayer(currentPlayer);
		}
		return Collections.max(otherScores);
	}
}
