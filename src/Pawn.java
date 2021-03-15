public class Pawn extends ChessPiece {

    public Pawn(ChessColor color, ChessPiece[][] board) {
        super(color, board);
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Pawn(color, board);
    }

    @Override
    public boolean canMakeMove(int x1, int y1, int x2, int y2) {
        if (color == ChessColor.WHITE && y2 == y1 + 1 || color == ChessColor.BLACK && y2 == y1 - 1) {
            if (x1 == x2) {
                return board[x2][y2] == null;
            } else if (x1 + 1 == x2) {
                return Chess.isNotMyColor(this, board[x2][y2]) && board[x2][y2] != null;
            } else if (x1 - 1 == x2) {
                return Chess.isNotMyColor(this, board[x2][y2]) && board[x2][y2] != null;
            }
        }
        return false;
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public String toString() {
        return color == ChessColor.WHITE ? "\u2659" : "\u265F";
    }

}
