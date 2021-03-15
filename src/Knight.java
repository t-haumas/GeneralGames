public class Knight extends ChessPiece {

    public Knight(ChessColor color, ChessPiece[][] board) {
        super(color, board);
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Knight(color, board);
    }

    @Override
    public boolean canMakeMove(int x1, int y1, int x2, int y2) {
        if (x2 == x1 + 1 && y2 == y1 + 2 && Chess.isNotMyColor(this, board[x2][y2])) return true;
        if (x2 == x1 - 1 && y2 == y1 + 2 && Chess.isNotMyColor(this, board[x2][y2])) return true;
        if (x2 == x1 + 1 && y2 == y1 - 2 && Chess.isNotMyColor(this, board[x2][y2])) return true;
        if (x2 == x1 - 1 && y2 == y1 - 2 && Chess.isNotMyColor(this, board[x2][y2])) return true;
        if (x2 == x1 + 2 && y2 == y1 + 1 && Chess.isNotMyColor(this, board[x2][y2])) return true;
        if (x2 == x1 - 2 && y2 == y1 + 1 && Chess.isNotMyColor(this, board[x2][y2])) return true;
        if (x2 == x1 + 2 && y2 == y1 - 1 && Chess.isNotMyColor(this, board[x2][y2])) return true;
        return x2 == x1 - 2 && y2 == y1 - 1 && Chess.isNotMyColor(this, board[x2][y2]);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public String toString() {
        return color == ChessColor.WHITE ? "\u2658" : "\u265E";
    }
}
