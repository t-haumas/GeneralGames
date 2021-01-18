import java.util.List;

/**
 * This is a generalized interface for games, for use by my minimax algorithm.
 * 
 * @author Thomas Ricks
 *
 */
public abstract class Game implements Cloneable
{
	// numPlayers should be like a final variable, I wonder if there's a way to do that.
	
	// Player are 1-INDEXED! First player is player 1.
	int turnPlayer;
	protected StringBuilder moveStringBuilder;

	public Game()
	{
		moveStringBuilder = new StringBuilder();
	}

	public Game(StringBuilder moveStringBuilder)
	{
		this.moveStringBuilder = new StringBuilder(moveStringBuilder);
	}

	//TODO: add restart() method?
	//TODO: add getpreviousplayer() method?

	/**
	 * Returns a String name of the game.
	 * @return The name of the game as a string.
	 */
	public abstract String getName();

	public void restart()
	{
		moveStringBuilder.delete(0, moveStringBuilder.length());
		createNewGame();
	}

	protected abstract void createNewGame();

	/**
	 * A slow way of translating a string move back to its integer form.
	 * This is so the developer doesn't have to think so hard to make a backwards translation,
	 * but they can if they want. This is O(n) efficiency.
	 *
	 * @param moveString The String representation of a move to be translated
	 * @return The int representation of the String move, or -1 if no such move exists.
	 */
	public int translateMoveStringToInt(String moveString)
	{
		String currentMoveString;
		for (int currentMoveInt = 0; currentMoveInt <= getMaxMove(); currentMoveInt++)
		{
			currentMoveString = translateMoveIntToEnglish(currentMoveInt);
			if (moveString.equals(currentMoveString))
			{
				return currentMoveInt;
			}
		}
		return -1;
	}

	/**
	 * This is to be overridden.
	 * ~~~ Moves are supposed to be non-negative. ~~~
	 *
	 * @return The maximum possible valid integer move.
	 */
	public abstract int getMaxMove();

	/**
	 * @return The number of players in this game.
	 */
	public abstract int getNumPlayers();

	/**
	 * Returns the number ID of the player after a given player.
	 * 
	 * @param nowPlayer - The number ID of the 'current' player.
	 * @return The number ID of the next player.
	 */
	public int getNextPlayer(int nowPlayer)
	{
		return nowPlayer == getNumPlayers() ? 1 : nowPlayer + 1;
	}
	
	/**
	 * Returns the number ID of the next player.
	 *
	 * @return The number ID of the next player.
	 */
	public int getNextPlayer()
	{
		return turnPlayer == getNumPlayers() ? 1 : turnPlayer + 1;
	}
	
	/**
	 * Returns 1 if it's player 1's turn, 2 if it's player 2's...
	 * Optionally, returns 0 if the game is over.
	 * 
	 * @return - Number ID of the turn player. Can also return 0 if game is over.
	 */
	public int getTurnPlayer()
	{
		return turnPlayer;
	}
	
	/**
	 * Overloaded method - this version returns legal moves for the turn player.
	 * Returns a List containing integer representations of all possible
	 * legal move the given player can make.
	 *
	 * ~~~ Moves are supposed to be non-negative. ~~~
	 * 
	 * @return - A List of all given moves represented as integers that
	 * the turn player can make in the current gamestate.
	 */
	public abstract List<Integer> getLegalMoves();
	
	/**
	 * Make the given move in the game. Moves are represented by integers.
	 * Yes, check to make sure moves are legal.
	 *
	 * ~~~ Moves are supposed to be non-negative. ~~~
	 *
	 * @param move - Move to make, represented by an integer.
	 * @return if the move was successful.
	 */
	protected abstract boolean move(int move);

	public void makeMove(int move)
	{
		if (move(move))
			moveStringBuilder.append(translateMoveIntToEnglish(move)).append(",");
	}
	
	/**
	 * Returns the score of the given player (an integer).
	 * 
	 * @param player - The number ID of the player to get the score of.
	 * @return The score of the given player.
	 */
	public abstract int getScore(int player);
	
	/**
	 * Tells if the game is over.
	 * 
	 * @return true if the game is over, false if the game is not over.
	 */
	public abstract boolean isOver();

	public abstract boolean theOutcomeCannotChangeAndWeKnowIt();
	
	/**
	 * Returns an exact copy of the game and moveStringBuilder (use super(moveStringBuilder)).
	 * @return - an exact copy of the game.
	 */
	public abstract Game clone();

	/**
	 * Since all moves are represented as integers, there should be a way to
	 * intepret those numbers as actual actions, in english. That's what this
	 * method is for.
	 *
	 * ~~~ Moves are supposed to be non-negative. ~~~
	 *
	 * @param move - The integer representation of a move,
	 * @return - The english translation of that move as a string.
	 */
	public abstract String translateMoveIntToEnglish(int move);

	public abstract String toString();

	public String getMoveString()
	{
		String moveString;
		try {
			moveString = moveStringBuilder.toString().substring(0, moveStringBuilder.length() - 1);
		}
		catch (IndexOutOfBoundsException e)
		{
			moveString = "";
		}
		return moveString;
	}

	public int[] getScores()
	{
		int[] scoresArray = new int[getNumPlayers()];
		for (int i = 0; i < getNumPlayers(); i++)
		{
			scoresArray[i] = getScore(i + 1);
		}
		return scoresArray;
	}
}
