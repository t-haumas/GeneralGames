import java.util.ArrayList;
import java.util.List;

public class Mancala extends Game
{
    private static final int[] blank14 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int TOTAL_BOARD_SPACES = 14;
    private static final int INITIAL_SPACE_DENSITY = 4;

    private static int[] originalSpaces;

    private final boolean custom;
    private int[] spaces;

    /**
     * Default constructor.
     * Sets player 1 as the turn player, and initializes spaces.
     */
    public Mancala()
    {
        if (! (TOTAL_BOARD_SPACES % 2 == 0))
            throw new RuntimeException("Mancala game must have even number of spaces.");

        createNewGame();
        for (int i = 0; i < spaces.length; i++)
        {
            if(! isScoreSpace(i))
            {
                spaces[i] = INITIAL_SPACE_DENSITY;
            }
        }
        custom = false;
    }

    public Mancala(int[] spaces)
    {
        createNewGame();
        System.arraycopy(spaces, 0, this.spaces, 0, spaces.length);
        originalSpaces = new int[TOTAL_BOARD_SPACES];

        if (! (TOTAL_BOARD_SPACES % 2 == 0) || TOTAL_BOARD_SPACES != spaces.length)
            throw new RuntimeException("Avalanche mancala game has invalid number of spaces.");

        custom = true;
        System.arraycopy(spaces, 0, originalSpaces, 0, spaces.length);
    }

    @Override
    protected void createNewGame()
    {
        turnPlayer = 1;

        spaces = new int[TOTAL_BOARD_SPACES];
    }

    /**
     * Constructor that takes in the values of every space on the game board and the first player to move.
     *
     * @param spaceValues The board[] representing the mancala board
     * @param turnPlayer The player who will move now.
     */
    private Mancala(int[] spaceValues, int turnPlayer, boolean spacesAreCustom, StringBuilder moveStringBuilder)
    {
        super(moveStringBuilder);
        custom = spacesAreCustom;
        this.turnPlayer = turnPlayer;
        spaces = new int[TOTAL_BOARD_SPACES];
        System.arraycopy(spaceValues, 0, spaces, 0, spaceValues.length);
    }

    /**
     * Prevents list index out of range exceptions by wrapping around any passed in ints.
     * These ints should represent spaces numbers.
     *
     * @param absoluteSpace the space number that may or may not be in range.
     * @return An int that represents the passed in space now wrapped around.
     */
    private int effectiveSpace(int absoluteSpace)
    {
        while (absoluteSpace > spaces.length - 1)
        {
            absoluteSpace -= TOTAL_BOARD_SPACES;
        }
        while (absoluteSpace < 0)
        {
            absoluteSpace += TOTAL_BOARD_SPACES;
        }
        return absoluteSpace;
    }

    /**
     * Gets whether or not the passed in space number is a score space.
     *
     * @param space the number of the space in question.
     * @return boolean that represents whether or not the passed in space is a score space.
     */
    public boolean isScoreSpace(int space)
    {
        return ((effectiveSpace(space) + 1) % (TOTAL_BOARD_SPACES / 2) == 0);
    }

    /**
     * Test for scenarios where the turn player needs to be swapped, and whether a piece got captured.
     * Also checks if the game is over. If it is, this sums up the final scores.
     *
     * @param lastSpace - should be returned by the moveAbsolute method, which should be passed in as a parameter to this.
     */
    private void updateGameState(int lastSpace)
    {
        boolean capture = false;
        if (turnPlayer == 1)
        {
            if (didCapture(lastSpace, 1))
            {
                //				System.out.println("Capture!");
                capture = true;
                int capturedSpaceIndex = effectiveSpace(lastSpace + (lastSpace - 6) * -2);
                int totalCaptured = 1 + spaces[capturedSpaceIndex];
                spaces[capturedSpaceIndex] = 0;
                spaces[lastSpace] = 0;
                spaces[TOTAL_BOARD_SPACES / 2 - 1] += totalCaptured;
            }
        }

        else
        {
            if (didCapture(lastSpace, 2))
            {
//				System.out.println("Capture!");
                capture = true;
                int capturedSpaceIndex = effectiveSpace(lastSpace + (lastSpace - 6) * -2);
                int totalCaptured = 1 + spaces[capturedSpaceIndex];
                spaces[capturedSpaceIndex] = 0;
                spaces[lastSpace] = 0;
                spaces[TOTAL_BOARD_SPACES - 1] += totalCaptured;
            }
        }

        if ((!(lastSpace == TOTAL_BOARD_SPACES / 2 - 1 && turnPlayer == 1) && !(lastSpace == TOTAL_BOARD_SPACES - 1 && turnPlayer == 2)) || capture)
        {
            turnPlayer = getNextPlayer();
        }

//        else
//        {
////			This means they're going again.
//        }

        if (isOver())
        {
            int totalP1Value = 0;
            for (int i = 0; i <= TOTAL_BOARD_SPACES / 2 - 1; i++)
            {
                totalP1Value += spaces[i];
            }

            int totalP2Value = 0;
            for (int i = TOTAL_BOARD_SPACES / 2; i <= TOTAL_BOARD_SPACES - 1; i++)
            {
                totalP2Value += spaces[i];
            }

            System.arraycopy(blank14, 0, spaces, 0, TOTAL_BOARD_SPACES);

            spaces[TOTAL_BOARD_SPACES / 2 - 1] = totalP1Value;
            spaces[TOTAL_BOARD_SPACES - 1] = totalP2Value;

//			System.out.println("Game Over!");
        }
    }

    private boolean didCapture(int spaceInQuestion, int movingPlayer)
    {
        boolean lastSpaceHas1 = spaces[spaceInQuestion] == 1;
        int getTo6 = (spaceInQuestion - 6) * -1;
        int capturedSpaceIndex = effectiveSpace(spaceInQuestion + getTo6 * 2);
        boolean capturedSpaceHasSomething = spaces[capturedSpaceIndex] > 0;
        if (movingPlayer == 1)
        {
            return lastSpaceHas1 && capturedSpaceHasSomething && effectiveSpace(spaceInQuestion) < TOTAL_BOARD_SPACES / 2 - 1;
        }
        else
        {
            return lastSpaceHas1 && capturedSpaceHasSomething && effectiveSpace(spaceInQuestion) > TOTAL_BOARD_SPACES / 2 - 1 && effectiveSpace(spaceInQuestion) < TOTAL_BOARD_SPACES - 1;
        }
    }

    /**
     * Makes an absolute move on the board after checking it is valid.
     *
     * @param space - This is an absolute position on the board, not relative to any player.
     * @return lastSpace - The space where the last piece was placed in the move that was made.
     */
    private int moveAbsolute(int space)
    {
        int dropPoint = space;

        if(spaces[space] != 0 && ! isScoreSpace(space))
        {
            int piecesToMove = spaces[space];
            spaces[space] = 0;
            int additions = 0;
            for (int i = 1; i <= piecesToMove; i++)
            {
                dropPoint = space + i + additions;
                // Don't let them place a stone in opponent's mancala.
                if (isScoreSpace(effectiveSpace(dropPoint)) && space / (TOTAL_BOARD_SPACES / 2) != effectiveSpace(dropPoint) / (TOTAL_BOARD_SPACES / 2))
                    additions++;
                dropPoint = space + i + additions;
                spaces[effectiveSpace(dropPoint)] += 1;
            }
        }

        return effectiveSpace(dropPoint);
    }

    @Override
    public String getName()
    {
        if (! custom) {
            return "capture-mode Mancala";
        }
        else {
            StringBuilder output = new StringBuilder("capture-mode Mancala custom spaces  ");
            for (int space : originalSpaces)
            {
                output.append(space).append(" ");
            }
            output.delete(output.length() - 1, output.length());
            return output.toString();
        }
    }

    @Override
    public int getMaxMove() {
        return TOTAL_BOARD_SPACES - 2;
    }

    @Override
    public int getNumPlayers()
    {
        return 2;
    }

    @Override
    public List<Integer> getLegalMoves()
    {
        ArrayList<Integer> legalMovesList = new ArrayList<>();
        for (int i = 0; i < TOTAL_BOARD_SPACES - 1; i++)
        {
            if (spaces[i] != 0 && ! isScoreSpace(i))
            {
                if (turnPlayer == 1 && i < TOTAL_BOARD_SPACES / 2 || turnPlayer == 2 && i >= TOTAL_BOARD_SPACES / 2)
                    legalMovesList.add(i);
            }
        }
        return legalMovesList;
    }

    @Override
    protected boolean move(int move)
    {
        if (getLegalMoves().contains(move))
        {
            updateGameState(moveAbsolute(move));
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int getScore(int player)
    {
        if (player == 1)
        {
            return spaces[TOTAL_BOARD_SPACES / 2 - 1];
        }
        else
        {
            return spaces[TOTAL_BOARD_SPACES - 1];
        }
    }

    @Override
    public boolean isOver()
    {
        int totalP1Value = 0;
        for (int i = 0; i < TOTAL_BOARD_SPACES / 2 - 1; i++)
        {
            totalP1Value += spaces[i];
        }

        if (totalP1Value == 0)
            return true;

        int totalP2Value = 0;
        for (int i = TOTAL_BOARD_SPACES / 2; i < TOTAL_BOARD_SPACES - 1; i++)
        {
            totalP2Value += spaces[i];
        }

        return (totalP2Value == 0);
    }

    @Override
    public boolean theOutcomeCannotChangeAndWeKnowIt()
    {
        return getScore(1) > (TOTAL_BOARD_SPACES - 2) / 2 * INITIAL_SPACE_DENSITY || getScore(2) > (TOTAL_BOARD_SPACES - 2) / 2 * INITIAL_SPACE_DENSITY;
    }

    @Override
    public Game clone()
    {
        return new Mancala(spaces, turnPlayer, custom, moveStringBuilder);
    }

    @Override
    public String translateMoveIntToEnglish(int move)
    {
        if (isScoreSpace(move))
            return "Illegal move lol.";
        else if (move < TOTAL_BOARD_SPACES / 2)
            return "A" + (move + 1);
        else
            return "B" + (move - 6);
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder(" " + spaces[TOTAL_BOARD_SPACES - 1] + " \n");
        for (int i = 0; i <= TOTAL_BOARD_SPACES / 2 - 2; i++)
        {
            output.append(spaces[i]).append(" ").append(spaces[TOTAL_BOARD_SPACES - 2 - i]).append("\n");
        }
        output.append(" ").append(spaces[TOTAL_BOARD_SPACES / 2 - 1]).append(" ");
        return output.toString();
    }
}
