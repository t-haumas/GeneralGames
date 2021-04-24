import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Chess extends Game {

    private static final int DEATH_PENALTY = 100;

    private static final int ROOK = 0;
    private static final int BISHOP = 1;
    private static final int KNIGHT = 2;
    private static final int QUEEN = 3;
    private static final int PAWN = 4;

    private ChessPiece[][] board;
    private int turnPlayer;
    private King whiteKing;
    private King blackKing;
    private int p1PieceValue;
    private int p2PieceValue;
    private boolean threeFoldRepetition;
    private HashMap<String, Integer> positionsReached;
    private int movesSincePawnMoveOrCapture;
    private LinkedList<ChessPiece> whitePieces;
    private LinkedList<ChessPiece> blackPieces;

    public Chess() {
        createNewGame();
    }

    public Chess(int turnPlayer, StringBuilder moveStringBuilder, int p1PieceValue, int p2PieceValue,
                 HashMap<String, Integer> positionsReached, int movesSincePawnMoveOrCapture, LinkedList<ChessPiece> oldWhitePieces, LinkedList<ChessPiece> oldBlackPieces) {

        super(moveStringBuilder);
        this.positionsReached = new HashMap<>(positionsReached);
        this.turnPlayer = turnPlayer;
        this.p1PieceValue = p1PieceValue;
        this.p2PieceValue = p2PieceValue;
        this.movesSincePawnMoveOrCapture = movesSincePawnMoveOrCapture;
        threeFoldRepetition = false;
        whitePieces = new LinkedList<>();
        blackPieces = new LinkedList<>();
        board = new ChessPiece[8][8];

        for (ChessPiece piece : oldWhitePieces) {
            board[piece.getX()][piece.getY()] = piece.clone(board);
            whitePieces.add(board[piece.getX()][piece.getY()]);
            if (board[piece.getX()][piece.getY()] instanceof King) {
                if (board[piece.getX()][piece.getY()].getColor() == ChessColor.WHITE) {
                    whiteKing = (King) board[piece.getX()][piece.getY()];
                } else {
                    blackKing = (King) board[piece.getX()][piece.getY()];
                }
            }
        }

        for (ChessPiece piece : oldBlackPieces) {
            board[piece.getX()][piece.getY()] = piece.clone(board);
            blackPieces.add(board[piece.getX()][piece.getY()]);
            if (board[piece.getX()][piece.getY()] instanceof King) {
                if (board[piece.getX()][piece.getY()].getColor() == ChessColor.WHITE) {
                    whiteKing = (King) board[piece.getX()][piece.getY()];
                } else {
                    blackKing = (King) board[piece.getX()][piece.getY()];
                }
            }
        }

//        for (int x = 0; x < 8; x++) {
//            for (int y = 0; y < 8; y++) {
//                if (oldBoard[x][y] != null) {
//                    board[x][y] = oldBoard[x][y].clone(board);
//                    if (board[x][y] instanceof King) {
//                        if (board[x][y].getColor() == ChessColor.WHITE) {
//                            whiteKing = (King) board[x][y];
//                        } else {
//                            blackKing = (King) board[x][y];
//                        }
//                    }
//                }
//            }
//        }

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

    public static int convertToMove(int x1, int y1, int x2, int y2) {
        return 512 * x1 + 64 * y1 + 8 * x2 + y2;
    }

    @Override
    public String getName() {
        return "Chess";
    }

    @Override
    protected void createNewGame() {
        whitePieces = new LinkedList<>();
        blackPieces = new LinkedList<>();
        positionsReached = new HashMap<>();
        threeFoldRepetition = false;
        movesSincePawnMoveOrCapture = 0;
        turnPlayer = 1;
        board = new ChessPiece[8][8];

        //  Add pawns to both sides
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(ChessColor.WHITE, board, i, 1);
            whitePieces.add(board[i][1]);
            board[i][6] = new Pawn(ChessColor.BLACK, board, i, 6);
            blackPieces.add(board[i][6]);
            p1PieceValue += getPieceValue(PAWN);
            p2PieceValue += getPieceValue(PAWN);
        }

        //  Add rooks
        board[0][0] = new Rook(ChessColor.WHITE, board, 0, 0);
        board[7][0] = new Rook(ChessColor.WHITE, board, 7, 0);
        whitePieces.add(board[0][0]);
        whitePieces.add(board[7][0]);

        board[0][7] = new Rook(ChessColor.BLACK, board, 0, 7);
        board[7][7] = new Rook(ChessColor.BLACK, board, 7, 7);
        blackPieces.add(board[0][7]);
        blackPieces.add(board[7][7]);

        p1PieceValue += getPieceValue(ROOK) * 2;
        p2PieceValue += getPieceValue(ROOK) * 2;

        //  Add knights
        board[1][0] = new Knight(ChessColor.WHITE, board, 1, 0);
        board[6][0] = new Knight(ChessColor.WHITE, board, 6, 0);
        whitePieces.add(board[1][0]);
        whitePieces.add(board[6][0]);

        board[1][7] = new Knight(ChessColor.BLACK, board, 1, 7);
        board[6][7] = new Knight(ChessColor.BLACK, board, 6, 7);
        blackPieces.add(board[1][7]);
        blackPieces.add(board[6][7]);

        p1PieceValue += getPieceValue(KNIGHT) * 2;
        p2PieceValue += getPieceValue(KNIGHT) * 2;

        //  Add bishops
        board[2][0] = new Bishop(ChessColor.WHITE, board, 2, 0);
        board[5][0] = new Bishop(ChessColor.WHITE, board, 5, 0);
        whitePieces.add(board[2][0]);
        whitePieces.add(board[5][0]);

        board[2][7] = new Bishop(ChessColor.BLACK, board, 2, 7);
        board[5][7] = new Bishop(ChessColor.BLACK, board, 5, 7);
        blackPieces.add(board[2][7]);
        blackPieces.add(board[5][7]);

        p1PieceValue += getPieceValue(BISHOP) * 2;
        p2PieceValue += getPieceValue(BISHOP) * 2;

        //  Add royalty
        board[3][0] = new Queen(ChessColor.WHITE, board, 3, 0);
        whitePieces.add(board[3][0]);
        whiteKing = new King(ChessColor.WHITE, board, new Point(4, 0));
        board[4][0] = whiteKing;
        whitePieces.add(whiteKing);
        /*  Don't need to swap their spots! Cuz this is just a reflection.    */
        board[3][7] = new Queen(ChessColor.BLACK, board, 3, 7);
        blackPieces.add(board[3][7]);
        blackKing = new King(ChessColor.BLACK, board, new Point(4, 7));
        board[4][7] = blackKing;
        blackPieces.add(blackKing);

        p1PieceValue += getPieceValue(QUEEN);
        p2PieceValue += getPieceValue(QUEEN);

        whiteKing.setOtherKing(blackKing);
        blackKing.setOtherKing(whiteKing);

    }

    private int getPieceValue(int type) {
        if (type == ROOK) {
            return new Rook(ChessColor.WHITE, null, 0, 0).getValue();
        } else if (type == BISHOP) {
            return new Bishop(ChessColor.WHITE, null, 0, 0).getValue();
        } else if (type == KNIGHT) {
            return new Knight(ChessColor.WHITE, null, 0, 0).getValue();
        } else if (type == QUEEN) {
            return new Queen(ChessColor.WHITE, null, 0, 0).getValue();
        } else if (type == PAWN) {
            return new Pawn(ChessColor.WHITE, null, 0, 0).getValue();
        } else {
            throw new RuntimeException("Trying to get value of an invalid piece.");
        }
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
//        for (int x1 = 0; x1 < 8; x1++) {
//            for (int y1 = 0; y1 < 8; y1++) {
//                if (board[x1][y1] != null) {
//                    if (isTurnPlayerColor(board[x1][y1])) {
//                        for (int x2 = 0; x2 < 8; x2++) {
//                            for (int y2 = 0; y2 < 8; y2++) {
//                                int move = 512 * x1 + 64 * y1 + 8 * x2 + y2;
//                                if (isLegal(move)) legalMoves.add(move);
//                            }
//                        }
//                    }
//                }
//            }
//        }

        for (ChessPiece piece : turnPlayer == 1 ? whitePieces : blackPieces) {
            for (int move : piece.getMovesThisPieceCanMake()) {
                if (isLegal(move)) {
                    legalMoves.add(move);
                }
            }
        }

        for (int move = 4096; move <= getMaxMove(); move++)
        {
            if (isLegal(move))
            {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    private boolean isTurnPlayerColor(ChessPiece piece) {
        return piece.getColor() == ChessColor.WHITE && turnPlayer == 1 || piece.getColor() == ChessColor.BLACK && turnPlayer == 2;
    }

    @Override
    protected boolean move(int move) {
        if (isLegal(move)) {
            movesSincePawnMoveOrCapture++;
            if (4160 <= move && move < 4162) {
                // Castling move
                if (turnPlayer == 1) {
                    if (move == 4160) {
                        //  Kingside
                        whiteKing.moveTo(6, 0);
                        board[6][0] = whiteKing;
                        board[5][0] = board[7][0];
                        board[5][0].moveTo(5, 0);
                        board[4][0] = null;
                        board[7][0] = null;
                    } else {
                        //  Queenside
                        whiteKing.moveTo(2, 0);
                        board[2][0] = whiteKing;
                        board[3][0] = board[0][0];
                        board[3][0].moveTo(3, 0);
                        board[4][0] = null;
                        board[0][0] = null;
                    }
                } else {
                    if (move == 4160) {
                        //  Kingside
                        blackKing.moveTo(6, 7);
                        board[6][7] = blackKing;
                        board[5][7] = board[7][7];
                        board[5][7].moveTo(5, 7);
                        board[4][7] = null;
                        board[7][7] = null;
                    } else {
                        //  Queenside
                        blackKing.moveTo(2, 7);
                        board[2][7] = blackKing;
                        board[3][7] = board[0][7];
                        board[3][7].moveTo(3,7);
                        board[4][7] = null;
                        board[0][7] = null;
                    }
                }
            } else if (4096 <= move && move < 4160){
                //  Pawn promotion
                movesSincePawnMoveOrCapture = 0;

                //  Pawn promotion forward.
                int pawnPromotionMove = move - 4096;
                int topOrBottom = pawnPromotionMove / 32;
                ChessColor movingPawnColor = topOrBottom == 0 ? ChessColor.BLACK : ChessColor.WHITE;
                int xAndValue = pawnPromotionMove % 32;
                int promotionValue = xAndValue / 8;
                int x = xAndValue % 8;
                int y1 = topOrBottom == 0 ? 1 : 6;
                int y2 = topOrBottom == 0 ? 0 : 7;
                board[x][y2] = getNewPieceFromPromoValue(promotionValue, movingPawnColor, x, y2);
                if (turnPlayer == 1) {
                    whitePieces.add(board[x][y2]);
                    whitePieces.remove(board[x][y1]);
                } else {
                    blackPieces.add(board[x][y2]);
                    blackPieces.remove(board[x][y1]);
                }
                board[x][y1] = null;
                if (movingPawnColor == ChessColor.BLACK) {
                    p2PieceValue -= getPieceValue(PAWN);
                    p2PieceValue += board[x][y2].getValue();
                } else {
                    p1PieceValue -= getPieceValue(PAWN);
                    p1PieceValue += board[x][y2].getValue();
                }
                turnPlayer = getNextPlayer();
                increaseNumTimesThisPositionHasBeenReached();
                return true;
            } else {
                int x1 = move / 64 / 8;
                int y1 = move / 64 % 8;
                int x2 = move % 64 / 8;
                int y2 = move % 64 % 8;
                if (board[x1][y1] instanceof Pawn) {
                    movesSincePawnMoveOrCapture = 0;
                }
                if (board[x2][y2] != null) {
                    movesSincePawnMoveOrCapture = 0;
                    if (turnPlayer == 1) {
                        p2PieceValue -= board[x2][y2].getValue();
                        blackPieces.remove(board[x2][y2]);
                    } else {
                        p1PieceValue -= board[x2][y2].getValue();
                        whitePieces.remove(board[x2][y2]);
                    }
                }
                if (board[x2][y2] instanceof King) {
                    System.out.println(board[x1][y1]);
                    System.out.println(turnPlayer);
                    JFrame errorShower = new JFrame();
                    errorShower.add(getPanelRepresentingThisGame());
                    errorShower.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
                    System.out.println(this.moveStringBuilder.toString());
                    System.out.println(translateMoveIntToEnglish(move));
                    errorShower.setVisible(true);
                    JOptionPane.showMessageDialog(null, "King die!");
                    whiteKing.inCheck();
                }
                board[x2][y2] = board[x1][y1];
                board[x2][y2].moveTo(x2, y2);
                board[x1][y1] = null;
            }
            turnPlayer = getNextPlayer();
            increaseNumTimesThisPositionHasBeenReached();
            return true;
        } else {
            turnPlayer = getNextPlayer();
            return false;
        }
    }

    private ChessPiece getNewPieceFromPromoValue(int promotionValue, ChessColor movingPawnColor, int startX, int startY) {
        if (promotionValue == 0) {
            return new Queen(movingPawnColor, board, startX, startY);
        } else if (promotionValue == 1) {
            return new Knight(movingPawnColor, board, startX, startY);
        } else if (promotionValue == 2) {
            return new Rook(movingPawnColor, board, startX, startY);
        } else {
            return new Bishop(movingPawnColor, board, startX, startY);
        }
    }

    private void increaseNumTimesThisPositionHasBeenReached() {
        StringBuilder thisPosition = new StringBuilder();
        /* TurnPlayer, White kingMoved, blackKingMoved, EverySpace'sPiece*/
        thisPosition.append(turnPlayer);
        thisPosition.append(whiteKing.moved);
        thisPosition.append(blackKing.moved);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                thisPosition.append(board[x][y]);
            }
        }

        String positionString = thisPosition.toString();
        if (positionsReached.containsKey(positionString)) {
            positionsReached.put(positionString, positionsReached.get(positionString) + 1);
        } else {
            positionsReached.put(positionString, 1);
        }

        threeFoldRepetition = positionsReached.get(positionString) >= 3;
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
                    if (isTurnPlayerColor(board[x1][y1])) {
                        if (board[x1][y1].canMakeMove(x1, y1, x2, y2)) {
                            return moveDoesNotResultInCheck(x1, y1, x2, y2);
                        }
                    }
                }
            }
        } else if (move < 4160){
            //  Pawn promotion forward.
            int pawnPromotionMove = move - 4096;
            int topOrBottom = pawnPromotionMove / 32;
            int xAndValue = pawnPromotionMove % 32;
//            int promotionValue = xAndValue / 8;
            ChessColor movingPawnColor = topOrBottom == 0 ? ChessColor.BLACK : ChessColor.WHITE;
            int x = xAndValue % 8;
            int y1 = topOrBottom == 0 ? 1 : 6;
            int y2 = topOrBottom == 0 ? 0 : 7;
            if (board[x][y2] == null) {
                if (board[x][y1] instanceof Pawn) {
                    if (board[x][y1].getColor() == movingPawnColor && isTurnPlayerColor(board[x][y1])) {
                        return moveDoesNotResultInCheck(x, y1, x, y2);
                    }
                }
            }
            return false;
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
        return getLegalMoves().size() == 0 || threeFoldRepetition || movesSincePawnMoveOrCapture >= 50;
    }

    @Override
    public boolean theOutcomeCannotChangeAndWeKnowIt() {
        return false;
    }

    @Override
    public Game clone() {
        return new Chess(turnPlayer, moveStringBuilder, p1PieceValue, p2PieceValue, positionsReached, movesSincePawnMoveOrCapture, whitePieces, blackPieces);
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
            int xAndValue = pawnPromotionMove % 32;
            int promotionValue = xAndValue / 8;
            int x = xAndValue % 8;
            int y1 = topOrBottom == 0 ? 1 : 6;
            int y2 = topOrBottom == 0 ? 0 : 7;
            return translatePointToChessNotation(x, y1) + "-" + translatePointToChessNotation(x, y2) + " = " + getPromotionValueChar(promotionValue);
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
