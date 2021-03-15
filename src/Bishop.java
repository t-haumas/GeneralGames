public class Bishop extends ChessPiece {

    public Bishop(ChessColor color, ChessPiece[][] board) {
        super(color, board);
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Bishop(color, board);
    }

    @Override
    public boolean canMakeMove(int x1, int y1, int x2, int y2) {
        if (Math.abs(x1 - x2) == Math.abs(y1 - y2)) {
            int x = x1;
            int y = y1;
            for (; x != x2 && y != y2; y += y2 > y1 ? 1 : -1, x += x2 > x1 ? 1 : -1) {
                if (x == x1 && y == y1) {
                    continue;
                }
                if (board[x][y] != null) {
                    return false;
                }
            }
            return Chess.isNotMyColor(this, board[x][y]);
        }
        return false;
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public String toString() {
        return color == ChessColor.WHITE ? "\u2657" : "\u265D";
    }
}
