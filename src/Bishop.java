import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessPiece {

    private int x;
    private int y;

    public Bishop(ChessColor color, ChessPiece[][] board, int x, int y) {
        super(color, board);
        this.x = x;
        this.y = y;
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Bishop(color, board, x, y);
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
    public List<Integer> getMovesThisPieceCanMake() {
        ArrayList<Integer> moves = new ArrayList<>();

        // Check diagonals
        //  Check top right diagonal
        Point myPosition = new Point(x, y);
        Point checkingPoint = new Point(myPosition);
        checkingPoint.translate(1, 1);
        for (; checkingPoint.x < 8 && checkingPoint.y < 8; checkingPoint.translate(1, 1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
            else  {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != this.color){
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
                break;
            }
        }

        //  Check top left diagonal
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, 1);
        for (; checkingPoint.x >= 0 && checkingPoint.y < 8; checkingPoint.translate(-1, 1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
            else  {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != this.color){
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
                break;
            }
        }

        //  Check bottom right diagonal
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(1, -1);
        for (; checkingPoint.x < 8 && checkingPoint.y >= 0; checkingPoint.translate(1, -1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
            else  {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != this.color){
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
                break;
            }
        }

        //  Check bottom left diagonal
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, -1);
        for (; checkingPoint.x >= 0 && checkingPoint.y >= 0; checkingPoint.translate(-1, -1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) {
                moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
            }
            else  {
                if (board[checkingPoint.x][checkingPoint.y].getColor() != this.color){
                    moves.add(Chess.convertToMove(x, y, checkingPoint.x, checkingPoint.y));
                }
                break;
            }
        }

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
        return color == ChessColor.WHITE ? "\u2657" : "\u265D";
    }
}
