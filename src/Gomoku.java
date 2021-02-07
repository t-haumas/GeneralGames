import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Gomoku extends Game
{
	//TODO: double check the diagonal win conditions.
	/*
	 *  Some properties:
	 *  Gomoku(3,3) is normal tic-tac-toe. - 1st player cannot guarantee a win, but can guarantee a tie. Same with 2nd player.
	 *  The first player in Gomoku(4,3) can guarantee a win. 
	 */
	private final int boardLength;
	private int[][] spaces;
	private int winner;
	private final int winLength;
	private ArrayList<Integer> legalMoves;

	//TODO: add checking that winLength <= length.
	public Gomoku(int length, int winLength)
	{
		this.winLength = winLength;
		boardLength = length;
		spaces = new int[length][length];
		createNewGame();
	}

	@Override
	protected void createNewGame()
	{
		legalMoves = new ArrayList<>();
		turnPlayer = 1;
		winner = 0;
		//Might have to add setting maxMove to boardLength squared if there's issues. Same with the other constructor.

		for (int row = 0; row < boardLength; row++)
		{
			for (int column = 0; column < boardLength; column++)
			{
				spaces[row][column] = 0;
				legalMoves.add(column * boardLength + row);
			}
		}
	}

	private Gomoku(int length, int[][] mySpaces, int tPlayer, int winLength, List<Integer> parentLegalMoves, StringBuilder moveStringBuilder)
	{
		super(moveStringBuilder);
		legalMoves = new ArrayList<>(parentLegalMoves);
		this.winLength = winLength;
		winner = 0;
		spaces = new int[length][length];
		boardLength = length;
		turnPlayer = tPlayer;
		for (int row = 0; row < boardLength; row++)
		{
			System.arraycopy(mySpaces[row], 0, spaces[row], 0, boardLength);
		}
	}

	@Override
	public List<Integer> getLegalMoves()
	{
//		LinkedList<Integer> legalMoves = new LinkedList<Integer>();
//		if (! isOver())
//		{
//			for (int row = 0; row < boardLength; row++)
//			{
//				for (int column = 0; column < boardLength; column++)
//				{
//					if (spaces[row][column] == 0)
//					{
//						legalMoves.add(row + column * boardLength);
//					}
//				}
//			}
//		}
		return legalMoves;
	}

	@Override
	protected boolean move(int move)
	{
		if (! isOver())
		{
			if (legalMoves.contains(move))
			{
				int y = move / boardLength;
				int x = move % boardLength;

				spaces[x][y] = turnPlayer;

				turnPlayer = getNextPlayer();
				
				legalMoves.remove(Integer.valueOf(move));
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
		return winner == player ? 1 : 0;
	}

	@Override
	public boolean isOver()
	{
		int empties = 0;
		for (int row = 0; row < boardLength; row++)
		{
			for (int column = 0; column < boardLength; column++)
			{
				int cell = spaces[row][column];

				if (cell == 0)
				{
					empties += 1;
				}
				else
				{

					//	Check for horizontal victory
					boolean isWinningHead = true;
					for (int i = 1; i < winLength; i++)
					{
						try
						{
							if (cell != spaces[row + i][column])
							{
								isWinningHead = false;
							}
						}
						catch (IndexOutOfBoundsException e)
						{
							isWinningHead = false;
						}
					}
					if (isWinningHead)
					{
						winner = cell;
					}

					//	Check for vertical victory
					isWinningHead = true;
					for (int i = 1; i < winLength; i++)
					{
						try
						{
							if (cell != spaces[row][column + i])
							{
								isWinningHead = false;
							}
						}
						catch (IndexOutOfBoundsException e)
						{
							isWinningHead = false;
						}
					}
					if (isWinningHead)
					{
						winner = cell;
					}

					//	Check for top-left diagonal victory
					isWinningHead = true;
					for (int i = 1; i < winLength; i++)
					{
						try
						{
							if (cell != spaces[row + i][column - i])
							{
								isWinningHead = false;
							}
						}
						catch (IndexOutOfBoundsException e)
						{
							isWinningHead = false;
						}
					}
					if (isWinningHead)
					{
						winner = cell;
					}

					//	Check for top-right diagonal victory
					isWinningHead = true;
					for (int i = 1; i < winLength; i++)
					{
						try
						{
							if (cell != spaces[row + i][column + i])
							{
								isWinningHead = false;
							}
						}
						catch (IndexOutOfBoundsException e)
						{
							isWinningHead = false;
						}
					}
					if (isWinningHead)
					{
						winner = cell;
					}
				}
			}
		}
		if (empties == 0)
			return true;
		return winner != 0;
	}

	@Override
	public boolean theOutcomeCannotChangeAndWeKnowIt() {
		return true;
	}

	@Override
	public Game clone()
	{
		return new Gomoku(boardLength, spaces, turnPlayer, winLength, legalMoves, moveStringBuilder);
	}

	@Override
	public String translateMoveIntToEnglish(int move)
	{
		int y = move / boardLength;
		int x = move % boardLength;
		return x + "-" + y;
	}

	@Override
	public String getName()
	{
		return boardLength + "x" + boardLength + " " + winLength + "-in-a-row" + " Gomoku";
	}

	@Override
	public int getMaxNumMovesForOnePlayer()
	{
		return boardLength * boardLength;
	}

	@Override
	public JPanel getPanelRepresentingThisGame() {
		return null;
	}

	@Override
	public int getMaxMove()
	{
		return boardLength * boardLength;
	}

	@Override
	public int getNumPlayers()
	{
		return 2;
	}

	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		for (int column = boardLength - 1; column > -1; column--)
		{
			for (int row = 0; row < boardLength; row++)
			{
				output.append(spaces[row][column]).append("|");
			}
			output = new StringBuilder(output.substring(0, output.length() - 1) + "\n");
		}
		return output.toString();
	}
	
	public int[][] getSpaces()
	{
		return spaces;
	}

}
