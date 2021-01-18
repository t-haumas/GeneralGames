import java.util.List;

public class Filler extends Game
{

    @Override
    public String getName()
    {
        return "Filler";
    }

    @Override
    protected void createNewGame() {

    }

    @Override
    public int getMaxMove()
    {
        // This is assuming that the max number of colors is 30, for no real reason.
        return 30;
    }

    @Override
    public int getNumPlayers()
    {
        // Could be more players.
        return 2;
    }


    @Override
    public List<Integer> getLegalMoves() {
        return null;
    }

    @Override
    protected boolean move(int move)
    {
    return false;
    }

    @Override
    public int getScore(int player) {
        return 0;
    }

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public boolean theOutcomeCannotChangeAndWeKnowIt() {
        return false;
    }

    @Override
    public Game clone() {
        return null;
    }

    @Override
    public String translateMoveIntToEnglish(int move) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
