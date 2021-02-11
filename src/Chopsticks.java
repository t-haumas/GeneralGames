import java.util.ArrayList;
import java.util.List;

public class Chopsticks extends Game
{
    /*
     * Each player has 2 consecutive spots in the playerHands array.
     * The first is their left hand, the second is their right.
     * So, playerNumber * 2 = Left, and playerNumber * 2 + 1 = Right.
     */
    private int[] playerHands;
    private final boolean yesOverflow;
    private int gameLength;
    private final int numPlayers;
    private ArrayList<Integer> deadPeople;

    public Chopsticks(int numberOfPlayers, boolean handsOverflow)
    {
        numPlayers = numberOfPlayers;
        yesOverflow = handsOverflow;
        createNewGame();
    }

    protected void createNewGame()
    {
        playerHands = new int[numPlayers * 2];
        // Get rid of this with arraycopy?
        for (int i = 0; i < numPlayers * 2; i++)
        {
            playerHands[i] = 1;
        }
        turnPlayer = 1;
        deadPeople = new ArrayList<>();
        gameLength = 0;
    }

    private Chopsticks(int numberOfPlayers, boolean handsOverflow, int[] handValues, int turnPlayer, int gameLength, ArrayList<Integer> deadPeople, StringBuilder moveStringBuilder)
    {
        super(moveStringBuilder);
        numPlayers = numberOfPlayers;
        playerHands = new int[numPlayers * 2];
        if (numPlayers * 2 >= 0) System.arraycopy(handValues, 0, playerHands, 0, numPlayers * 2);
        yesOverflow = handsOverflow;
        this.turnPlayer = turnPlayer;
        this.gameLength = gameLength;
        this.deadPeople = new ArrayList<>(deadPeople);
    }

    @Override
    public String getName()
    {
        return yesOverflow ? "O" : "No-o" + "verflow Chopsticks";
    }

    @Override
    public int getMaxNumMovesForOnePlayer() {
        return 16 * (numPlayers - 1);
    }

    public int getMaxMove()
    {
        return numPlayers * 16 * (numPlayers - 1) - 1;
    }

    @Override
    public int getNumPlayers() {
        return numPlayers;
    }

    @Override
    public List<Integer> getLegalMoves()
    {
        ArrayList<Integer> legalMoves = new ArrayList<>();
        int maxMove = numPlayers * 16 - 1;
        for (int move = 0; move <= maxMove; move++)
        {
            if (isLegal(move))
            {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Helper method that determines if an individual move is legal.
     * @param move the integer representing the move being made.
     * @return a boolean saying if the move is legal or not.
     */
    private boolean isLegal(int move)
    {
        /*
        Reasons a move could be illegal:
                * hand has 0
                * moving to itself
                * moving to someone who has died.
                * giving + playerHands[toPosition] = playerHands[fromPosition] and it's moving to themself.
                * number giving is greater than the number of the position.
                * if number giving is not equal to number has and going to different player.
         */

        boolean legal = true;

        int fromPosition = (turnPlayer - 1) * 2 + (((move / 8) % 2 == 0) ? 0 : 1);
        int toPosition = move / 16 * 2 + (((move / 4) % 2) == 1 ? 1 : 0);
        int fromPositionIsGiving = (move % 4) + 1;

        if (playerHands[fromPosition] == 0)
            legal = false;
        if (fromPosition == toPosition)
            legal = false;
        // Turn this off if resurrection is allowed. But then there has to be a way to skip ppl who died.
        if (playerHands[move / 16 * 2] == 0 && playerHands[move / 16 * 2 + 1] == 0)
            legal = false;// *2
        if (((turnPlayer - 1) == move / 16) && (fromPositionIsGiving + playerHands[toPosition] == playerHands[fromPosition]))
            legal = false;// *2
        if (! ((turnPlayer - 1) == move / 16) && playerHands[toPosition] == 0)
            legal = false;
        if (! ((turnPlayer - 1) == move / 16) && playerHands[fromPosition] != fromPositionIsGiving)
            legal = false;
        if (fromPositionIsGiving > playerHands[fromPosition])
            legal = false;

        return legal;
    }

    @Override
    protected boolean move(int move)
    {
        if (! isLegal(move))
        {
            Thaumas.showErrorWindow("In chopsticks: illegal move.");
            return false;
        }
        int fromPosition = (turnPlayer - 1) * 2 + (((move / 8) % 2 == 0) ? 0 : 1);
        int toPosition = move / 16 * 2 + (((move / 4) % 2) == 1 ? 1 : 0);
        int fromPositionIsGiving = (move % 4) + 1;

        if ((turnPlayer - 1) == move / 16)
            playerHands[fromPosition] -= fromPositionIsGiving;
        playerHands[toPosition] += fromPositionIsGiving;

        // Check to see if a hand is at 5 or over.
        if (playerHands[toPosition] >= 5)
        {
            if (yesOverflow)
                playerHands[toPosition] -= 5;
            else
                playerHands[toPosition] = 0;
        }

        // Check to see if someone just died.
        if (playerHands[move / 16 * 2] == 0 && playerHands[move / 16 * 2 + 1] == 0)
        {
            deadPeople.add(move / 16 + 1);
        }

        turnPlayer = getNextPlayer();
        while (deadPeople.contains(turnPlayer))
        {
            turnPlayer = getNextPlayer();
        }
        gameLength++;
        return true;
    }

    @Override
    public int getScore(int player)
    {
        if (isOver())
        {
            // Try to elongate game length:
            //return playerHands[(player - 1) * 2] + playerHands[(player - 1) * 2 + 1] > 0 ? 1000 : gameLength;
            return playerHands[(player - 1) * 2] + playerHands[(player - 1) * 2 + 1];
        }
        else if (deadPeople.contains(player))
        {
            return -1;
        }
        else
            return 0;
    }

    @Override
    public boolean isOver()
    {
        return deadPeople.size() == numPlayers - 1;
    }

    @Override
    public boolean theOutcomeCannotChangeAndWeKnowIt() {
        return false;
    }

    @Override
    public Game clone()
    {
        return new Chopsticks(numPlayers, yesOverflow, playerHands, turnPlayer, gameLength, deadPeople, moveStringBuilder);
    }

    @Override
    public String translateMoveIntToEnglish(int move)
    {
        // 0: 1LL1, 1: 1LL2, 2: 1LL3, 3: 1LL4, 4: 1LR1, 5: 1LR2, 6: 1LR3, 7: 1LR4
        // 8: 1RL1, 9: 1RL2, 10: 1RL3, 11: 1RL4, 12: 1RR1, 13: 1RR2, 14: 1RR3, 15: 1RR4
        // 16: 2LL1, 17: 2LL2 , ...
        String output = "";
        output += move / 16 + 1;
        output += ((move / 8) % 2 == 0) ? "L" : "R";
        output += ((move / 4) % 2 == 0) ? "L" : "R";
        output += (move % 4) + 1;
        return output;
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < playerHands.length / 2; i++)
        {
            output.append(playerHands[i * 2]).append("   ").append(playerHands[i * 2 + 1]).append("\n");
        }
        return output.toString();
//        return Arrays.toString(playerHands);
    }
}
