public class Rook extends ChessPiece {

    private int moved;

    public Rook(ChessColor color, ChessPiece[][] board) {
        super(color, board);
    }

    public Rook(ChessColor color, ChessPiece[][] board, int moved) {
        super(color, board);
        this.moved = moved;
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Rook(color, board, moved);
    }

    @Override
    public boolean canMakeMove(int x1, int y1, int x2, int y2) {
        if (! Chess.isNotMyColor(this, board[x2][y2])) return false;
        if (x1 == x2) {
            for (int y = y1; y != y2; y += y2 > y1 ? 1 : -1) {
                if (y == y1) continue;
                if (board[x1][y] != null) {
                    return false;
                }
            }
            return true;
        } else if (y1 == y2) {
            for (int x = x1; x != x2; x += x2 > x1 ? 1 : -1) {
                if (x == x1) continue;
                if (board[x][y1] != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getValue() {
        return 5;
    }

    @Override
    public String toString() {
        return color == ChessColor.WHITE ? "\u2656" : "\u265C";
    }

    public boolean hasMoved() {
        return moved > 1;
    }

    public void increaseTimesMoved() {
        moved++;
    }

}
