import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player{

    @Override
    public int getMove(Game playingGame) {
        List<Integer> allMoves = playingGame.getLegalMoves();
        Random random = new Random();
        return allMoves.get(random.nextInt(allMoves.size()));
    }

    @Override
    public String getName() {
        return "Random Computer Player";
    }

    @Override
    public String getInfo() {
        return "@";
    }

    @Override
    public String getRawName() {
        return "random";
    }

    @Override
    public String getRawInfo() {
        return "$";
    }
}
