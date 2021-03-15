import java.util.Arrays;
import java.util.LinkedList;

public class TicTacToe extends Game
{

	private int numMovesMade;
	private String[][] spaces;
	private int winner;

	public TicTacToe()
	{
		createNewGame();
	}

	protected void createNewGame()
	{
		turnPlayer = 1;
		spaces = new String[3][3];
		winner = 0;
		numMovesMade = 0;
		for (int row = 0; row < 3; row++)
		{
			for (int column = 0; column < 3; column++)
			{
				spaces[row][column] = "_";
			}
		}
	}

	public TicTacToe(int numMoves, int currentPlayer, String[][] mySpace, StringBuilder moveStringBuilder)
	{
		super(moveStringBuilder);
		numMovesMade = numMoves;
		turnPlayer = currentPlayer;
		spaces = new String[3][3];
		for (int row = 0; row < 3; row++)
		{
			for (int column = 0; column < 3; column++)
			{
				spaces[row][column] = mySpace[row][column];
			}
		}
		winner = 0;
	}

	@Override
	public LinkedList<Integer> getLegalMoves()
	{
		LinkedList<Integer> legalMoves = new LinkedList<Integer>();
		if (! isOver())
		{
			for (int row = 0; row < 3; row++)
			{
				for (int column = 0; column < 3; column++)
				{
					if (spaces[row][column].equals("_"))
					{
						legalMoves.add(row + column * 3);
					}
				}
			}
		}
		return legalMoves;
	}

	@Override
	protected boolean move(int move)
	{
		if (! isOver())
		{
			if (getLegalMoves().contains(move))
			{
				int y = move / 3;
				int x = move % 3;

				if (turnPlayer == 1)
				{
					spaces[x][y] = "X";
				}
				else if (turnPlayer == 2)
				{
					spaces[x][y] = "O";
				}
				else
				{
					throw new RuntimeException("For some reason there's a 3rd player??? (TicTacToe game)");
				}

				numMovesMade += 1;

				turnPlayer = getNextPlayer();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public int getScore(int player)
	{
		if (isOver())
		{
			if (player == winner)
				return 1;
			else
				return 0;
		}
		else
			return 0;
	}

	@Override
	public boolean isOver()
	{
		if (numMovesMade < 9)
		{	
			//	Check rows and columns

			for (int index = 0; index < spaces.length; index++)
			{
				if (spaces[index][0].equals(spaces[index][1])
						&& spaces[index][1].equals(spaces[index][2]))
				{
					if (spaces[index][0].equals("X"))
					{
						winner = 1;
						return true;
					}
					else if (spaces[index][0].equals("O"))
					{
						winner = 2;
						return true;
					}
				}

				if (spaces[0][index].equals(spaces[1][index])
						&& spaces[1][index].equals(spaces[2][index]))
				{
					if (spaces[0][index].equals("X"))
					{
						winner = 1;
						return true;
					}
					else if (spaces[0][index].equals("O"))
					{
						winner = 2;
						return true;
					}
				}
			}

			// Check diagonals
			boolean topLeft = spaces[0][0].equals(spaces[1][1]) && spaces[1][1].equals(spaces[2][2]);
			boolean topRight = spaces[2][0].equals(spaces[1][1]) && spaces[1][1].equals(spaces[0][2]);

			if (topLeft || topRight)
			{
				if (spaces[1][1].equals("X"))
				{
					winner = 1;
					return true;
				}
				else if (spaces[1][1].equals("O"))
				{
					winner = 2;
					return true;
				}
			}
			return false;
		}
		else if (numMovesMade == 9)
		{
			winner = 0;
			return true;
		}
		else
		{
			throw new RuntimeException("For some reason a tenth move was made at some point.");
		}
	}

	@Override
	public boolean theOutcomeCannotChangeAndWeKnowIt() {
		// Could probably do it
		return false;
	}

	@Override
	public Game clone()
	{
		return new TicTacToe(numMovesMade, turnPlayer, spaces, moveStringBuilder);
	}

	@Override
	public String translateMoveIntToEnglish(int move)
	{
		int y = move / 3;
		int x = move % 3;
		return Integer.toString(x) + "-" + Integer.toString(y);
	}

	public String toString()
	{
		return spaces[0][2] + "|" + spaces[1][2] + "|" + spaces[2][2] + "\n"
				+ spaces[0][1] + "|" + spaces[1][1] + "|" + spaces[2][1] + "\n"
				+ spaces[0][0] + "|" + spaces[1][0] + "|" + spaces[2][0];
	}

	@Override
	public int getNumPlayers()
	{
		return 2;
	}

	@Override
	public String getName()
	{
		return "Tic-Tac-Toe";
	}

	@Override
	public int getMaxNumMovesForOnePlayer()
	{
		return 9;
	}

	@Override
	public int getMaxMove()
	{
		return 8;
	}

}
