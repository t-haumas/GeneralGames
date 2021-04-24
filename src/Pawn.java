import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece {

    private int x;
    private int y;
    private boolean moved;

    public Pawn(ChessColor color, ChessPiece[][] board, int x, int y) {
        super(color, board);
        this.x = x;
        this.y = y;
        moved = false;
    }

    public Pawn(ChessColor color, ChessPiece[][] board, int x, int y, boolean moved) {
        super(color, board);
        this.x = x;
        this.y = y;
        this.moved = moved;
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Pawn(color, board, x, y, moved);
    }

    @Override
    public boolean canMakeMove(int x1, int y1, int x2, int y2) {
        if (y2 == y1 + 2 || y2 == y1 - 2) {
            if (x1 == x2) {
                if (color == ChessColor.WHITE && y1 == 1) {
                    if (y2 == 3) {
                        return (board[x2][2] == null && board[x2][3] == null);
                    }
                } else if (color == ChessColor.BLACK && y1 == 6) {
                    if (y2 == 4) {
                        return (board[x2][5] == null && board[x2][4] == null);
                    }
                }
            }
        }
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
    public List<Integer> getMovesThisPieceCanMake() {
        ArrayList<Integer> moves = new ArrayList<>();
        int changeY = color == ChessColor.WHITE ? 1 : -1;
        try {
            if (board[x][y + changeY] == null) {
                moves.add(Chess.convertToMove(x, y, x, y + changeY));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        if (! moved) {
            try {
                if (board[x][y + changeY * 2] == null) {
                    moves.add(Chess.convertToMove(x, y, x, y + changeY * 2));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
        try {
            if (board[x + 1][y + changeY] != null) {
                if (board[x + 1][y + changeY].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, x + 1, y + changeY));
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if (board[x - 1][y + changeY] != null) {
                if (board[x - 1][y + changeY].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, x - 1, y + changeY));
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        return moves;
    }

    @Override
    public void moveTo(int x2, int y2) {
        x = x2;
        y = y2;
        moved = true;
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return color == ChessColor.WHITE ? "\u2659" : "\u265F";
    }

}
