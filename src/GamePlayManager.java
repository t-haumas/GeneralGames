import javax.swing.*;
import java.util.*;

public class GamePlayManager
{
	private final GameDataManager dataManager;
	private final Game game;
	private ArrayList<Player> players;
	private final FileManager preferences;

	public GamePlayManager(Game g, FileManager fileManager, GameDataManager dataManager)
	{
		//TODO: package it all up in a nice .jar file or something and all GUIs.
		game = g;
		preferences = fileManager;
		this.dataManager = dataManager;
	}

	public void start()
	{
		//Print game intro
		if (preferences.outputGameAndInfo)
		{
			IOManager.output("Game beginning: " + game.getNumPlayers() + "-player " + game.getName() + ".");
			StringBuilder playersString = new StringBuilder("\tThe players are: ");
			int playerID = 1;
			for (Player player : players) {
				playersString.append(playerID).append(" - ").append(player.getName()).append(", ").append(player.getInfo()).append("; ");
				playerID++;
			}
			IOManager.output(playersString.toString());
		}

		// Play the game.
		while (! game.isOver())
		{
			if (preferences.outputGameAndInfo)
			{
				IOManager.output(game.toString(), OutputType.GAMESTATE);
				StringBuilder scores = new StringBuilder();
				for (int player = 1; player <= game.getNumPlayers(); player++) {
					scores.append("Player ").append(player).append("'s score: ").append(game.getScore(player));
					scores.append("\n");
				}
				scores.delete(scores.length() - 1, scores.length());
				IOManager.output(scores.toString(), OutputType.SCORE);
			}

			// Output engine recommendation
			if (preferences.showEngineRecommendations > 0 && players.get(game.getTurnPlayer() - 1) instanceof HumanPlayer) {
				long startTime = System.currentTimeMillis();
				IOManager.output("Calculating...");
				MinimaxResult engineResult = dataManager.getRecommendedMoveAndInfo(game, preferences.engineDepth);
				IOManager.output("  Engine (depth " + preferences.engineDepth + ") recommends " +
						game.translateMoveIntToEnglish(engineResult.getMove()) +
						getGameOutcomePredictionString(engineResult) + " (" + (System.currentTimeMillis() - startTime) / 100 / 10.0 + "s)" + ".");
				if (preferences.showEngineRecommendations > 1 && players.get(game.getTurnPlayer() - 1) instanceof HumanPlayer) {
					IOManager.output(formatMoveValuations(dataManager.getValuations()), OutputType.VALUATIONS);
				}
				Thaumas.playSound(12);
			}

			// Say the AI is thinking
			Player currentPlayer = players.get(game.getTurnPlayer() - 1);
			if (preferences.outputGameAndInfo && (currentPlayer instanceof SafeOptimalAI || currentPlayer instanceof WantToDraw))
			{
				IOManager.output("Player " + game.getTurnPlayer() + " is thinking.");
			}
			long startTime = System.currentTimeMillis();
			int nextMove = getMove(game.getTurnPlayer() - 1);
			if (preferences.outputGameAndInfo && (currentPlayer instanceof SafeOptimalAI || currentPlayer instanceof RandomPlayer || currentPlayer instanceof WantToDraw))
			{
				IOManager.output("Player " + game.getTurnPlayer() + " makes the move " + game.translateMoveIntToEnglish(nextMove) +
						" (" + (System.currentTimeMillis() - startTime) / 100 / 10.0 + "s)" + ".");
			}
			game.makeMove(nextMove);

			if (preferences.playSoundAfterMoves)
			{
				Thaumas.playSound(0.1);
			}
		}

		preferences.updateDeterminedLines();

		ArrayList<Integer> scores = new ArrayList<>();
		for (int i = 1; i <= game.getNumPlayers(); i++)
		{
			scores.add(game.getScore(i));
		}

		// Print final game state, scores, and result.
		if (preferences.outputGameAndInfo)
		{
			IOManager.output(game.toString(), OutputType.GAMESTATE);
			IOManager.output("\n\n");
			int maxScorePlayerNumber = scores.indexOf(Collections.max(scores));
			if (Collections.frequency(scores, Collections.max(scores)) > 1)
			{
				IOManager.output("There's a tie!");
			}
			else
			{
				IOManager.output("Player " + (maxScorePlayerNumber + 1) + " wins.");
			}
			StringBuilder scoresOutput = new StringBuilder();
			for (int i = 0; i < scores.size(); i++)
			{
				scoresOutput.append("\tPlayer ").append(i + 1).append(" score: ").append(scores.get(i)).append("\n");
			}
			IOManager.output(scoresOutput.toString());
		}

		if (preferences.outputGameAndInfo || preferences.outputMovestring)
		{
			IOManager.output(game.getMoveString());
		}
	}

	private String formatMoveValuations(ArrayList<MinimaxResult> valuations) {
		StringBuilder valuationsString = new StringBuilder();
		for (MinimaxResult moveAndScore : valuations) {
			valuationsString.append(game.translateMoveIntToEnglish(moveAndScore.getMove())).append(": ").append(moveAndScore.getScore()).append("\n");
		}

		return valuationsString.toString();
	}

	private String getGameOutcomePredictionString(MinimaxResult result)
	{
		double score = result.getScore();
		String output = "";
		output += result.getCertainty() ? ", and knows you will be " : ", and thinks you will be ";
		if (score > 0) {
			output += "winning by at least " + score + " points";
		}
		else if (score < 0)
		{
			output += "losing by at least " + (-1 * score) + " points";
		}
		else
		{
			output += "at least tied with the opponent(s)";
		}
		return output;
	}

	private int getMove(int playerNumber)
	{
		return players.get(playerNumber).getMove(game);
	}

	public void initialize()
	{
		setPlayers(preferences.getPlayers());
	}

	public void setPlayers(ArrayList<Player> players)
	{
		this.players = new ArrayList<>(players);
	}

}