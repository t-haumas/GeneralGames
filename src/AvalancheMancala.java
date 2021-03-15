import java.util.ArrayList;
import java.util.List;

public class AvalancheMancala extends Game
{
    // Don't use this if not using 14 spaces.
    private static final int[] blank14 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int turnPlayer;

    private final boolean customSpaces;
    public static int[] originalSpaces;
    private int[] spaces;
    private final int TOTAL_NUM_SPACES = 14;
    private final int INITIAL_DENSITY = 4;

    public AvalancheMancala()
    {
        customSpaces = false;
        if (! (TOTAL_NUM_SPACES % 2 == 0))
            throw new RuntimeException("Mancala game cannot have odd number of spaces.");

        createNewGame();

        for (int i = 0; i < TOTAL_NUM_SPACES; i++)
        {
            spaces[i] = INITIAL_DENSITY;
        }
        spaces[TOTAL_NUM_SPACES - 1] = 0;
        spaces[TOTAL_NUM_SPACES / 2 - 1] = 0;

    }

    public AvalancheMancala(int[] spaces)
    {
        customSpaces = true;
        originalSpaces = new int[spaces.length];

        if (! (TOTAL_NUM_SPACES % 2 == 0) || TOTAL_NUM_SPACES != spaces.length)
            throw new RuntimeException("Avalanche mancala game has invalid number of spaces.");

        System.arraycopy(spaces, 0, originalSpaces, 0, spaces.length);

        createNewGame();

        System.arraycopy(spaces, 0, this.spaces, 0, spaces.length);
    }

    public AvalancheMancala(int turnPlayer, int[] spaces, boolean customSpaces, StringBuilder moveStringBuilder)
    {
        super(moveStringBuilder);
        this.customSpaces = customSpaces;
        this.turnPlayer = turnPlayer;
        this.spaces = new int[TOTAL_NUM_SPACES];
        System.arraycopy(spaces, 0, this.spaces, 0, spaces.length);
    }

    @Override
    public String getName()
    {
        if (! customSpaces) {
            return "Avalanche-mode Mancala";
        }
        else {
            StringBuilder output = new StringBuilder("Avalanche-mode Mancala custom spaces  ");
            for (int space : originalSpaces)
            {
                output.append(space).append(" ");
            }
            output.delete(output.length() - 1, output.length());
            return output.toString();
        }
    }

    @Override
    protected void createNewGame()
    {
        spaces = new int[TOTAL_NUM_SPACES];
        turnPlayer = 1;
    }

    @Override
    public int getMaxMove()
    {
        return TOTAL_NUM_SPACES - 2;
    }

    @Override
    public int getNumPlayers()
    {
        return 2;
    }

    @Override
    public int getTurnPlayer() {
        return turnPlayer;
    }

    @Override
    public List<Integer> getLegalMoves()
    {
        ArrayList<Integer> legalMovesList = new ArrayList<>();
        for (int i = 0; i < TOTAL_NUM_SPACES - 1; i++)
        {
            if (spaces[i] != 0 && ! isScoreSpace(i))
            {
                if (turnPlayer == 1 && i < TOTAL_NUM_SPACES / 2 || turnPlayer == 2 && i >= TOTAL_NUM_SPACES / 2)
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
            int movingIndex = move;
            boolean start = true;

            while (spaces[movingIndex] > 1 && ! isScoreSpace(movingIndex) || start)
            {
                start = false;
                int holdingStones = spaces[movingIndex];
                spaces[movingIndex] = 0;
                while (holdingStones > 0) {
                    movingIndex++;
                    if (turnPlayer == 1 && movingIndex == TOTAL_NUM_SPACES - 1 || turnPlayer == 2 && movingIndex == TOTAL_NUM_SPACES / 2 - 1) {
                        movingIndex++;
                    }
                    if (movingIndex == TOTAL_NUM_SPACES) {
                        movingIndex = 0;
                    }
                    holdingStones--;
                    spaces[movingIndex]++;
                }
            }
            if (! isScoreSpace(movingIndex))
            {
                turnPlayer = getNextPlayer();
            }
            if (isOver())
            {
                int totalP1Value = 0;
                for (int i = 0; i <= TOTAL_NUM_SPACES / 2 - 1; i++)
                {
                    totalP1Value += spaces[i];
                }

                int totalP2Value = 0;
                for (int i = TOTAL_NUM_SPACES / 2; i <= TOTAL_NUM_SPACES - 1; i++)
                {
                    totalP2Value += spaces[i];
                }

                System.arraycopy(blank14, 0, spaces, 0, TOTAL_NUM_SPACES);

                spaces[TOTAL_NUM_SPACES / 2 - 1] = totalP1Value;
                spaces[TOTAL_NUM_SPACES - 1] = totalP2Value;
            }

            return true;
        }
        else
            return false;
    }

    @Override
    public int getScore(int player)
    {
        return spaces[player == 1 ? TOTAL_NUM_SPACES / 2 - 1 : TOTAL_NUM_SPACES - 1];
    }

    @Override
    public boolean isOver()
    {
        int totalP1Value = 0;
        for (int i = 0; i < TOTAL_NUM_SPACES / 2 - 1; i++)
        {
            totalP1Value += spaces[i];
        }

        if (totalP1Value == 0)
            return true;

        int totalP2Value = 0;
        for (int i = TOTAL_NUM_SPACES / 2; i < TOTAL_NUM_SPACES - 1; i++)
        {
            totalP2Value += spaces[i];
        }

        return (totalP2Value == 0);
    }

    @Override
    public boolean theOutcomeCannotChangeAndWeKnowIt()
    {
        return getScore(1) > (TOTAL_NUM_SPACES - 2) / 2 * INITIAL_DENSITY || getScore(2) > (TOTAL_NUM_SPACES - 2) / 2 * INITIAL_DENSITY;
    }

    @Override
    public Game clone()
    {
        return new AvalancheMancala(turnPlayer, spaces, customSpaces, moveStringBuilder);
    }

    @Override
    public String translateMoveIntToEnglish(int move)
    {
        if (isScoreSpace(move))
            return "Illegal move lol.";
        else if (move < TOTAL_NUM_SPACES / 2)
            return "A" + (move + 1);
        else
            return "B" + (move - 6);
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder(" " + spaces[TOTAL_NUM_SPACES - 1] + " \n");
        for (int i = 0; i <= TOTAL_NUM_SPACES / 2 - 2; i++)
        {
            output.append(spaces[i]).append(" ").append(spaces[TOTAL_NUM_SPACES - 2 - i]).append("\n");
        }
        output.append(" ").append(spaces[TOTAL_NUM_SPACES / 2 - 1]).append(" ");
        return output.toString();
    }

    public boolean isScoreSpace(int space)
    {
        return (space + 1) == (TOTAL_NUM_SPACES / 2) || space + 1 == TOTAL_NUM_SPACES;
    }
}
