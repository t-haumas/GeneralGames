import java.awt.*;

public abstract class ChessPiece {

    protected ChessColor color;
    protected ChessPiece[][] board;

    private ChessPiece() {}

    protected ChessPiece(ChessColor color, ChessPiece[][] board) {
        this.color = color;
        this.board = board;
    }

//    public void moveTo(Point newPosition) {
//        myPosition.setLocation(newPosition);
//    }

    public ChessColor getColor() {
        return color;
    }

    public abstract ChessPiece clone(ChessPiece[][] board);

    /**
     * Gotta see logically and if the board has stuff in the way.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public abstract boolean canMakeMove(int x1, int y1, int x2, int y2);

    /**
     * @return Point value of this piece.
     */
    public abstract int getValue();
}

enum ChessColor {WHITE, BLACK}