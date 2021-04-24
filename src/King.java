import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {

    private King otherKing;
    int moved;
    private final Point myPosition;

    public King(ChessColor color, ChessPiece[][] board, Point startingPoint) {
        super(color, board);
        this.myPosition = startingPoint;
        moved = 0;
    }

    public King(ChessColor color, ChessPiece[][] board, Point myPosition, int moved) {
        super(color, board);
        this.myPosition = new Point(myPosition);
        this.moved = moved;
    }

    public void setOtherKing(King otherKing) {
        this.otherKing = otherKing;
    }

    @Override
    public ChessPiece clone(ChessPiece[][] board) {
        return new King(color, board, myPosition, moved);
    }

    @Override
    public boolean canMakeMove(int x1, int y1, int x2, int y2) {
        if (Chess.distance(x1, y1, x2, y2) == 1) {
            if (board[x2][y2] != null) {
                return board[x2][y2].getColor() != getColor();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<Integer> getMovesThisPieceCanMake() {
        ArrayList<Integer> moves = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;
                try {
                    if (board[myPosition.x + x][myPosition.y + y] == null) {
                        moves.add(Chess.convertToMove(myPosition.x, myPosition.y, myPosition.x + x, myPosition.y + y));
                    } else if (board[myPosition.x + x][myPosition.y + y].getColor() != color) {
                        moves.add(Chess.convertToMove(myPosition.x, myPosition.y, myPosition.x + x, myPosition.y + y));
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }

        return moves;
    }

    @Override
    public void moveTo(int x2, int y2) {
        myPosition.setLocation(x2, y2);
        moved++;
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public int getX() {
        return myPosition.x;
    }

    @Override
    public int getY() {
        return myPosition.y;
    }

    public boolean hasMoved() {
        return moved > 0;
    }

    @Override
    public String toString() {
        return color == ChessColor.WHITE ? "\u2654" : "\u265A";
    }

    public boolean inCheck() {  //  Is there any faster way?
        // Check diagonals
        //  Check top right diagonal
        Point checkingPoint = new Point(myPosition);
        checkingPoint.translate(1, 1);
        for (; checkingPoint.x < 8 && checkingPoint.y < 8; checkingPoint.translate(1, 1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Bishop || board[checkingPoint.x][checkingPoint.y] instanceof Queen)) {
                if (getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                    return true;
                } else break;
            } else {
                break;
            }
        }

        //  Check top left diagonal
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, 1);
        for (; checkingPoint.x >= 0 && checkingPoint.y < 8; checkingPoint.translate(-1, 1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Bishop || board[checkingPoint.x][checkingPoint.y] instanceof Queen)) {
                if (getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                    return true;
                } else break;
            } else {
                break;
            }
        }

        //  Check bottom right diagonal
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(1, -1);
        for (; checkingPoint.x < 8 && checkingPoint.y >= 0; checkingPoint.translate(1, -1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Bishop || board[checkingPoint.x][checkingPoint.y] instanceof Queen)) {
                if (getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                    return true;
                } else break;
            } else {
                break;
            }
        }

        //  Check bottom left diagonal
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, -1);
        for (; checkingPoint.x >= 0 && checkingPoint.y >= 0; checkingPoint.translate(-1, -1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Bishop || board[checkingPoint.x][checkingPoint.y] instanceof Queen)) {
                if (getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                    return true;
                } else break;
            } else {
                break;
            }
        }

        // Check horizontals
        //  Check right
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(1, 0);
        for (; checkingPoint.x < 8; checkingPoint.translate(1, 0)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Rook || board[checkingPoint.x][checkingPoint.y] instanceof Queen) && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } else {
                break;
            }
        }

        //  Check left
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, 0);
        for (; checkingPoint.x >= 0; checkingPoint.translate(-1, 0)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Rook || board[checkingPoint.x][checkingPoint.y] instanceof Queen) && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } else {
                break;
            }
        }

        //  Check up
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(0, 1);
        for (; checkingPoint.y < 8; checkingPoint.translate(0, 1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Rook || board[checkingPoint.x][checkingPoint.y] instanceof Queen) && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } else {
                break;
            }
        }

        //  Check down
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(0, -1);
        for (; checkingPoint.y >= 0; checkingPoint.translate(0, -1)) {
            if (board[checkingPoint.x][checkingPoint.y] == null) continue;
            if ((board[checkingPoint.x][checkingPoint.y] instanceof Rook || board[checkingPoint.x][checkingPoint.y] instanceof Queen) && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } else {
                break;
            }
        }

        // Check for knight
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(1, 2);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, 2);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(1, -2);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-1, -2);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(2, 1);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-2, 1);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(2, -1);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
        checkingPoint.setLocation(myPosition);
        checkingPoint.translate(-2, -1);
        if (board[checkingPoint.x][checkingPoint.y] instanceof Knight && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) return true; } catch (ArrayIndexOutOfBoundsException ignored) {}

        // Check for pawns
        if (color == ChessColor.WHITE) {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(1,1);
            try {
            if (board[checkingPoint.x][checkingPoint.y] instanceof Pawn && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } } catch (ArrayIndexOutOfBoundsException ignored) {}
            checkingPoint.translate(-2,0);
            try {
            if (board[checkingPoint.x][checkingPoint.y] instanceof Pawn && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } } catch (ArrayIndexOutOfBoundsException ignored) {}
        } else {
            checkingPoint.setLocation(myPosition);
            checkingPoint.translate(1,-1);
            try {
            if (board[checkingPoint.x][checkingPoint.y] instanceof Pawn && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } } catch (ArrayIndexOutOfBoundsException ignored) {}
            try {
            checkingPoint.translate(-2,0);
            if (board[checkingPoint.x][checkingPoint.y] instanceof Pawn && getColor() != board[checkingPoint.x][checkingPoint.y].getColor()) {
                return true;
            } } catch (ArrayIndexOutOfBoundsException ignored) {}
        }

        // Check for adjacent king
        if (Chess.distance(myPosition.x, myPosition.y, otherKing.getPosition().x, otherKing.getPosition().y) == 1) {
            return true;
        }
        return false;
    }

    public Point getPosition() {
        return myPosition;
    }

    public void setPosition(Point p) {
        myPosition.setLocation(p);
    }
}
