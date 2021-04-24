import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece {

    private int moved;
    private int x;
    private int y;

    public Rook(ChessColor color, ChessPiece[][] board, int x, int y) {
        super(color, board);
        this.x = x;
        this.y = y;
        moved = 0;
    }

    public Rook(ChessColor color, ChessPiece[][] board, int moved, int x, int y) {
        super(color, board);
        this.moved = moved;
        this.x = x;
        this.y = y;
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new Rook(color, board, moved, x, y);
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
    public List<Integer> getMovesThisPieceCanMake() {
        Point myPosition = new Point(x, y);
        Point checkingPoint = new Point();
        ArrayList<Integer> moves = new ArrayList<>();

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
        moved++;
    }

    @Override
    public int getValue() {
        return 5;
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
        return color == ChessColor.WHITE ? "\u2656" : "\u265C";
    }

    public boolean hasMoved() {
        return moved > 1;
    }

}
