public class Queen extends ChessPiece {

    public Queen(ChessColor color, ChessPiece[][] board) {
        super(color, board);
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Queen(color, board);
    }

    @Override
    public boolean canMakeMove(int x1, int y1, int x2, int y2) {
        if (! Chess.isNotMyColor(this, board[x2][y2])) return false;
        if (x1 == x2) {
            for (int y = y1; y != y2; y += y2 > y1 ? 1 : -1) {
                if (board[x1][y] != null) {
                    if (y == y1) continue;
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
        } else if (Math.abs(x1 - x2) == Math.abs(y1 - y2)) {
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
        return 9;
    }

    @Override
    public String toString() {
        return color == ChessColor.WHITE ? "\u2655" : "\u265B";
    }
}
