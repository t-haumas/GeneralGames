import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessPiece {

    private int x;
    private int y;

    public Queen(ChessColor color, ChessPiece[][] board, int x, int y) {
        super(color, board);
        this.x = x;
        this.y = y;
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Queen(color, board, x, y);
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

        // Check horizontals
        //  Check right
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(1, 0);
        for (; checkingPoint.x < 8; checkingPoint.translate(1, 0)) {
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

        //  Check left
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, 0);
        for (; checkingPoint.x >= 0; checkingPoint.translate(-1, 0)) {
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

        //  Check up
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(0, 1);
        for (; checkingPoint.y < 8; checkingPoint.translate(0, 1)) {
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

        //  Check down
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(0, -1);
        for (; checkingPoint.y >= 0; checkingPoint.translate(0, -1)) {
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
        return 9;
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
        return color == ChessColor.WHITE ? "\u2655" : "\u265B";
    }
}
