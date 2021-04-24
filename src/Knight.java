import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece {

    private int x;
    private int y;

    public Knight(ChessColor color, ChessPiece[][] board, int x, int y) {
        super(color, board);
        this.x = x;
        this.y = y;
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Knight(color, board, x, y);
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
    public List<Integer> getMovesThisPieceCanMake() {
        Point checkingPoint = new Point();
        Point myPosition = new Point(x, y);
        ArrayList<Integer> moves = new ArrayList<>();

        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(1, 2);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(-1, 2);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(1, -2);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(-1, -2);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(2, 1);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(-2, 1);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(2, -1);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(-2, -1);
            if (board[checkingPoint.x][checkingPoint.y] != null) {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != color) {
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
            } else {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        return moves;
    }

    @Override
    public void moveTo(int x2, int y2) {
        x = x2;
        y = y2;
    }

    @Override
    public int getValue() {
        return 3;
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
        return color == ChessColor.WHITE ? "\u2658" : "\u265E";
    }
}
