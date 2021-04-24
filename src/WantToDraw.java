public class WantToDraw extends Player{

    private Minimaxer drawingMinimaxer;
    private final int depth;

    public WantToDraw(int maxDepth) {
        drawingMinimaxer = new Minimaxer(maxDepth);
        depth = maxDepth;
    }

    @Override
    public int getMove(Game playingGame) {
        return drawingMinimaxer.getDrawingMove(playingGame);
    }

    @Override
    public String getName() {
        return "Stalemater";
    }

    @Override
    public String getInfo() {
        return "Depth: " + depth;
    }

    @Override
    public String getRawName() {
        return "stalemater";
    }

    @Override
    public String getRawInfo() {
        return Integer.toString(depth);
    }
}
