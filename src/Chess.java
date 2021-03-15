import java.util.List;

public class Chess extends Game {

    public Chess() {

    }

    @Override
    public String getName() {
        return "Chess";
    }

    @Override
    public int getMaxNumMovesForOnePlayer() {
        return 4080;    // ? It's 64^2 + 48 for different pawn promotions, - 64 for moves to the same space.
    }

    @Override
    protected void createNewGame() {

    }

    @Override
    public int getMaxMove() {
        return 4080;
    }

    @Override
    public int getNumPlayers() {
        return 0;
    }

    @Override
    public List<Integer> getLegalMoves() {
        return null;
    }

    @Override
    protected boolean move(int move) {
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
