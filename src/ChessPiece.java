import java.awt.*;
import java.util.List;

public abstract class ChessPiece {

    protected ChessColor color;
    protected ChessPiece[][] board;



    private ChessPiece() {}

    protected ChessPiece(ChessColor color, ChessPiece[][] board) {
        this.color = color;
        this.board = board;
    }

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

    public abstract List<Integer> getMovesThisPieceCanMake();

    public abstract void moveTo(int x2, int y2);

    /**
     * @return Point value of this piece.
     */
    public abstract int getValue();

    public abstract int getX();

    public abstract int getY();
}

enum ChessColor {WHITE, BLACK}