import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chess extends Game {

    private static final int DEATH_PENALTY = 100;
    private ChessPiece[][] board;
    private int turnPlayer;
    private King whiteKing;
    private King blackKing;
    private int p1PieceValue;
    private int p2PieceValue;

    public Chess() {
        createNewGame();
    }

    public Chess(ChessPiece[][] oldBoard, int turnPlayer, StringBuilder moveStringBuilder, int p1PieceValue, int p2PieceValue) {
        super(moveStringBuilder);
        this.turnPlayer = turnPlayer;
        this.p1PieceValue = p1PieceValue;
        this.p2PieceValue = p2PieceValue;

        board = new ChessPiece[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (oldBoard[x][y] != null) {
                    board[x][y] = oldBoard[x][y].clone(board);
                    if (board[x][y] instanceof King) {
                        if (board[x][y].getColor() == ChessColor.WHITE) {
                            whiteKing = (King) board[x][y];
                        } else {
                            blackKing = (King) board[x][y];
                        }
                    }
                }
            }
        }

        this.whiteKing.setOtherKing(this.blackKing);
        this.blackKing.setOtherKing(this.whiteKing);
    }

    public static int distance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public static boolean isNotMyColor(ChessPiece me, ChessPiece otherPiece) {
        if (otherPiece == null) {
            return true;
        } else {
            return (otherPiece.getColor() != me.getColor());
        }

    }

    @Override
    public String getName() {
        return "Chess";
    }

    @Override
    protected void createNewGame() {
        turnPlayer = 1;
        board = new ChessPiece[8][8];

        //  Add pawns to both sides
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(ChessColor.WHITE, board);
            board[i][6] = new Pawn(ChessColor.BLACK, board);
            p1PieceValue += (new Pawn(ChessColor.WHITE, board)).getValue();
            p2PieceValue += (new Pawn(ChessColor.BLACK, board)).getValue();
        }

        //  Add rooks
        board[0][0] = new Rook(ChessColor.WHITE, board);
        board[7][0] = new Rook(ChessColor.WHITE, board);

        board[0][7] = new Rook(ChessColor.BLACK, board);
        board[7][7] = new Rook(ChessColor.BLACK, board);

        p1PieceValue += (new Rook(ChessColor.WHITE, board)).getValue() * 2;
        p2PieceValue += (new Rook(ChessColor.BLACK, board)).getValue() * 2;

        //  Add knights
        board[1][0] = new Knight(ChessColor.WHITE, board);
        board[6][0] = new Knight(ChessColor.WHITE, board);

        board[1][7] = new Knight(ChessColor.BLACK, board);
        board[6][7] = new Knight(ChessColor.BLACK, board);

        p1PieceValue += (new Knight(ChessColor.WHITE, board)).getValue() * 2;
        p2PieceValue += (new Knight(ChessColor.BLACK, board)).getValue() * 2;

        //  Add bishops
        board[2][0] = new Bishop(ChessColor.WHITE, board);
        board[5][0] = new Bishop(ChessColor.WHITE, board);

        board[2][7] = new Bishop(ChessColor.BLACK, board);
        board[5][7] = new Bishop(ChessColor.BLACK, board);

        p1PieceValue += (new Bishop(ChessColor.WHITE, board)).getValue() * 2;
        p2PieceValue += (new Bishop(ChessColor.BLACK, board)).getValue() * 2;

        //  Add royalty
        board[3][0] = new Queen(ChessColor.WHITE, board);
        whiteKing = new King(ChessColor.WHITE, board, new Point(4, 0));
        board[4][0] = whiteKing;
        /*  Don't need to swap their spots! Cuz this is just a reflection.    */
        board[3][7] = new Queen(ChessColor.BLACK, board);
        blackKing = new King(ChessColor.BLACK, board, new Point(4, 7));
        board[4][7] = blackKing;

        p1PieceValue += (new Queen(ChessColor.WHITE, board)).getValue();
        p2PieceValue += (new Queen(ChessColor.BLACK, board)).getValue();

        whiteKing.setOtherKing(blackKing);
        blackKing.setOtherKing(whiteKing);

    }

    @Override
    public int getMaxMove() {
        return 4162;    //  64^2 + 64 + 2. The 2 are the castles. The 64 are pawn promotions.
    }

    @Override
    public int getNumPlayers() {
        return 2;
    }

    @Override
    public int getTurnPlayer() {
        return turnPlayer;
    }

    @Override
    public List<Integer> getLegalMoves() {  // Any faster way?
        ArrayList<Integer> legalMoves = new ArrayList<>();
        for (int move = 0; move <= getMaxMove(); move++)
        {
            if (isLegal(move))
            {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    @Override
    protected boolean move(int move) {
        if (isLegal(move)) {
            if (4160 <= move && move < 4162) {
                // Castling move
                if (turnPlayer == 1) {
                    if (move == 4160) {
                        //  Kingside
                        whiteKing.moveTo(new Point(6, 0));
                        ((Rook)board[7][0]).increaseTimesMoved();
                        board[6][0] = whiteKing;
                        board[5][0] = board[7][0];
                        board[4][0] = null;
                        board[7][0] = null;
                    } else {
                        //  Queenside
                        whiteKing.moveTo(new Point(2, 0));
                        ((Rook)board[0][0]).increaseTimesMoved();
                        board[2][0] = whiteKing;
                        board[3][0] = board[0][0];
                        board[4][0] = null;
                        board[0][0] = null;
                    }
                } else {
                    if (move == 4160) {
                        //  Kingside
                        blackKing.moveTo(new Point(6, 7));
                        ((Rook)board[7][7]).increaseTimesMoved();
                        board[6][7] = blackKing;
                        board[5][7] = board[7][7];
                        board[4][7] = null;
                        board[7][7] = null;
                    } else {
                        //  Queenside
                        blackKing.moveTo(new Point(2, 7));
                        ((Rook)board[0][7]).increaseTimesMoved();
                        board[2][7] = blackKing;
                        board[3][7] = board[0][7];
                        board[4][7] = null;
                        board[0][7] = null;
                    }
                }
            } else {
                int x1 = move / 64 / 8;
                int y1 = move / 64 % 8;
                int x2 = move % 64 / 8;
                int y2 = move % 64 % 8;
                if (board[x2][y2] != null) {
                    if (turnPlayer == 1) {
                        p2PieceValue -= board[x2][y2].getValue();
                    } else {
                        p1PieceValue -= board[x2][y2].getValue();
                    }
                }
                if (board[x2][y2] instanceof King) {
                    System.out.println(board[x1][y1]);
                    System.out.println(turnPlayer);
                    System.out.println(whiteKing.getPosition());
                    JFrame errorShower = new JFrame();
                    errorShower.add(getPanelRepresentingThisGame());
                    errorShower.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
                    System.out.println(this.moveStringBuilder.toString());
                    System.out.println(translateMoveIntToEnglish(move));
                    errorShower.setVisible(true);
                    JOptionPane.showMessageDialog(null, "King die!");
                }
                board[x2][y2] = board[x1][y1];
                board[x1][y1] = null;

                if (board[x2][y2] instanceof King) {
                    ((King) board[x2][y2]).moveTo(new Point(x2, y2));
                } else if (board[x2][y2] instanceof Rook) {
                    ((Rook) board[x2][y2]).increaseTimesMoved();
                }
            }
            turnPlayer = getNextPlayer();
            return true;
        }
        turnPlayer = getNextPlayer();
        return false;
    }

    private boolean isLegal(int move) {
        if (4160 <= move && move < 4162) {
            // Castling move
            if (turnPlayer == 1) {
                if (! whiteKing.hasMoved()) {
                    if (move == 4160) {
                        //  Kingside
                        if (board[7][0] instanceof Rook) {
                            if (! ((Rook) board[7][0]).hasMoved()) {
                                if (board[6][0] == null && board[5][0] == null && ! whiteKing.inCheck()) {
                                    if (moveDoesNotResultInCheck(whiteKing.getPosition().x, whiteKing.getPosition().y, 5, 0)) {
                                        return moveDoesNotResultInCheck(whiteKing.getPosition().x, whiteKing.getPosition().y, 6, 0);
                                    }
                                }
                            }
                        }
                    } else {
                        //  Queenside
                        if (board[0][0] instanceof Rook) {
                            if (! ((Rook) board[0][0]).hasMoved()) {
                                if (board[1][0] == null && board[2][0] == null && board[3][0] == null && ! whiteKing.inCheck()) {
                                    if (moveDoesNotResultInCheck(whiteKing.getPosition().x, whiteKing.getPosition().y, 1, 0)) {
                                        if (moveDoesNotResultInCheck(whiteKing.getPosition().x, whiteKing.getPosition().y, 2, 0)) {
                                            return moveDoesNotResultInCheck(whiteKing.getPosition().x, whiteKing.getPosition().y, 3, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (!blackKing.hasMoved()) {
                    if (move == 4160) {
                        //  Kingside
                        if (board[7][7] instanceof Rook) {
                            if (! ((Rook) board[7][7]).hasMoved()) {
                                if (board[6][7] == null && board[5][7] == null && ! blackKing.inCheck()) {
                                    if (moveDoesNotResultInCheck(blackKing.getPosition().x, blackKing.getPosition().y, 5, 7)) {
                                        return moveDoesNotResultInCheck(blackKing.getPosition().x, blackKing.getPosition().y, 6, 7);
                                    }
                                }
                            }
                        }
                    } else {
                        //  Queenside
                        if (board[0][7] instanceof Rook) {
                            if (! ((Rook) board[0][7]).hasMoved()) {
                                if (board[1][7] == null && board[2][7] == null && board[3][7] == null && ! blackKing.inCheck()) {
                                    if (moveDoesNotResultInCheck(blackKing.getPosition().x, blackKing.getPosition().y, 1, 7)) {
                                        if (moveDoesNotResultInCheck(blackKing.getPosition().x, blackKing.getPosition().y, 2, 7)) {
                                            return moveDoesNotResultInCheck(blackKing.getPosition().x, blackKing.getPosition().y, 3, 7);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (move < 4096){
            int x1 = (move / 64) / 8;
            int y1 = (move / 64) % 8;
            int x2 = (move % 64) / 8;
            int y2 = (move % 64) % 8;
            if (x1 != x2 || y1 != y2) {
                if (board[x1][y1] != null) {
                    if (board[x1][y1].getColor() == ChessColor.WHITE && turnPlayer == 1 || board[x1][y1].getColor() == ChessColor.BLACK && turnPlayer == 2) {
                        if (board[x1][y1].canMakeMove(x1, y1, x2, y2)) {
                            return moveDoesNotResultInCheck(x1, y1, x2, y2);
                        }
                    }
                }
            }
        } else {
            //  Pawn promotion.
            int pawnPromotionMove = move - 4096;
            int topOrBottom = pawnPromotionMove / 32;
            int spotAndValue = pawnPromotionMove % 32;
            int promotionValue = spotAndValue / 8;
            int spot = spotAndValue % 4;
            Point pawnWillEndUpAt = new Point(spot, topOrBottom == 0 ? 0 : 7);
            return false;   //TODO: FIX THIS!
        }
        return false;
    }

    private boolean moveDoesNotResultInCheck(int x1, int y1, int x2, int y2) {
        //  Make simulated move
        ChessPiece savedSpot = board[x2][y2];
        board[x2][y2] = board[x1][y1];
        if (board[x2][y2] instanceof King) {
            ((King)board[x2][y2]).setPosition(new Point(x2, y2));
        }
        board[x1][y1] = null;

        boolean willBeInCheck = (turnPlayer == 1 ? whiteKing : blackKing).inCheck();

        //  Undo simulated move
        board[x1][y1] = board[x2][y2];
        if (board[x1][y1] instanceof King) {
            ((King)board[x1][y1]).setPosition(new Point(x1, y1));
        }
        board[x2][y2] = savedSpot;

        return !willBeInCheck;
    }

    @Override
    public int getScore(int player) {
        int myValue;
        if (player == 1) {
            myValue = p1PieceValue;
            if (isOver()) {
                if (whiteKing.inCheck()) {
                    myValue -= DEATH_PENALTY;
                } else if (blackKing.inCheck()) {
                    myValue += DEATH_PENALTY;
                } else {
                    myValue = 0;
                }
            }
        } else {
            myValue = p2PieceValue;
            if (isOver()) {
                if (blackKing.inCheck()) {
                    myValue -= DEATH_PENALTY;
                } else if (whiteKing.inCheck()) {
                    myValue += DEATH_PENALTY;
                } else {
                    myValue = 0;
                }
            }
        }
        return myValue;
    }

    @Override
    public boolean isOver() {
        return getLegalMoves().size() == 0;
    }

    @Override
    public boolean theOutcomeCannotChangeAndWeKnowIt() {
        return false;
    }

    @Override
    public Game clone() {
        return new Chess(board, turnPlayer, moveStringBuilder, p1PieceValue, p2PieceValue);
    }

    @Override
    public String translateMoveIntToEnglish(int move) {
        if (move < 0) {
            throw new IllegalArgumentException("Trying to make a negative move in chess.");
        }
        if (move < 4096) {
            int point1 = move / 64;
            int point2 = move % 64;
            int x1 = point1 / 8;
            int y1 = point1 % 8;
            int x2 = point2 / 8;
            int y2 = point2 % 8;
            return translatePointToChessNotation(x1, y1) + "-" + translatePointToChessNotation(x2, y2);
        } else if (move < 4160) {
            int pawnPromotionMove = move - 4096;
            int topOrBottom = pawnPromotionMove / 32;
            int spotAndValue = pawnPromotionMove % 32;
            int promotionValue = spotAndValue / 8;
            int spot = spotAndValue % 4;
            return translatePointToChessNotation(spot, topOrBottom == 0 ? 0 : 7) + " = " + getPromotionValueChar(promotionValue);
        } else if (move < 4162) {
            if (move == 4160) {
                return ("O-O");
            } else {
                return ("O-O-O");
            }
        } else {
            throw new IllegalArgumentException("Illegal move trying to be translated.");
        }
    }

    private String getPromotionValueChar(int promotionValue) {
        if (promotionValue == 0) {
            return "Q";
        } else if (promotionValue == 1) {
            return "N";
        } else if (promotionValue == 2) {
            return "R";
        } else if (promotionValue == 3) {
            return "B";
        } else {
            throw new IllegalArgumentException("Trying to promote to something that doesn't exist!");
        }
    }

    private String translatePointToChessNotation(int x, int y) {
        return String.valueOf((char)('a' + x)) + (char)('1' + y);
    }

    @Override
    public JPanel getPanelRepresentingThisGame() {

        int offset = 50;
        int cellSize = 50;

        JPanel chessPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Helvetica", Font.PLAIN, 48));

                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        g.setColor((x + y) % 2 == 0 ? new Color(43, 171, 43) : new Color(50, 167, 255));
                        g.fillRect(offset + x * cellSize, offset + (7 - y) * cellSize, cellSize, cellSize);
                        if (board[x][y] != null){
                            g.setColor(board[x][y].getColor() == ChessColor.WHITE ? Color.WHITE : Color.BLACK);
                            g.drawString(board[x][y].toString(), offset + x * cellSize + cellSize / 4, offset + (7 - y + 1) * cellSize - cellSize / 4);
                        }
                    }
                }

                g.setColor(Color.black);
                g.setFont(new Font("Helvetica", Font.PLAIN, 24));
                for (int x = 0; x < 8; x++) {
                    g.drawString(String.valueOf((char)('a' + x)), offset + x * cellSize + cellSize / 4, offset + 8 * cellSize + cellSize / 2);
                }
                for (int y = 0; y < 8; y++) {
                    g.drawString(String.valueOf(1 + y), offset + 8 * cellSize + cellSize / 4, offset + (8 - y) * cellSize - cellSize / 4);
                }
            }
        };
        chessPanel.setPreferredSize(new Dimension(cellSize * 8 + offset * 2, cellSize * 8 + offset * 2));
        return chessPanel;
    }

    @Override
    public String toString() {
//        throw new UnsupportedOperationException("String version of Chess has not been created yet!");
        return "String version of Chess has not been created yet!";
    }
}
