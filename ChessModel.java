package Project3;

import java.util.ArrayList;

/**********************************************************************
 * CIS 163 Section 01
 * Project 3: Chess Game
 * ChessModel Class
 *
 * This class handles the model for the chess game.  Contains methods
 * for changing and updating the game state.  Implements the
 * IChessModel Class
 *
 * @author George Fayette
 * @version 3/23/2019
 *********************************************************************/
public class ChessModel implements IChessModel {
    /**
     * Private JButton array representing the chess board.
     */
    private IChessPiece[][] board;

    /**
     * Private JButton array representing the chess board.
     */
    private Player player;

    /**
     * Private JButton array representing the chess board.
     */
    private GUIcodes GUIcode;

    /**
     * Private JButton array representing the chess board.
     */
    private int numMoves;

    /**
     * Private JButton array representing the chess board.
     */
    private final int SIZE = 8;

    /**
     * Private JButton array representing the chess board.
     */
    private ArrayList<Move> moves;

    /**
     * Private JButton array representing the chess board.
     */
    private ArrayList<IChessPiece> capturedPieces, movedPieces;

    /**
     * Private JButton array representing the chess board.
     */
    private boolean upgradePawn, enPassant, castle;

    /**
     * Private JButton array representing the chess board.
     */
    private ArrayList<Boolean> EpHappened, castleHappened,
            firstPawnMoves, firstRookMoves, firstKingMoves;

    /******************************************************************
     * Public constructor sets game state to initial values.
     *****************************************************************/
    public ChessModel() {
        board = new IChessPiece[8][8];
        player = Player.WHITE;
        GUIcode = GUIcodes.NO_MESSAGE;
        numMoves = 0;

        moves = new ArrayList<>();
        capturedPieces = new ArrayList<>();
        movedPieces = new ArrayList<>();

        upgradePawn = false;
        enPassant = false;
        castle = false;

        EpHappened = new ArrayList<>();
        castleHappened = new ArrayList<>();
        firstPawnMoves = new ArrayList<>();
        firstRookMoves = new ArrayList<>();
        firstKingMoves = new ArrayList<>();


        for (int i = 0; i < SIZE; ++i) {
            board[6][i] = new Pawn(Player.WHITE);
        }
        board[7][0] = new Rook(Player.WHITE);
        board[7][1] = new Knight(Player.WHITE);
        board[7][2] = new Bishop(Player.WHITE);
        board[7][3] = new Queen(Player.WHITE);
        board[7][4] = new King(Player.WHITE);
        board[7][5] = new Bishop(Player.WHITE);
        board[7][6] = new Knight(Player.WHITE);
        board[7][7] = new Rook(Player.WHITE);


        for (int i = 0; i < SIZE; ++i) {
            board[1][i] = new Pawn(Player.BLACK);
        }
        board[0][0] = new Rook(Player.BLACK);
        board[0][1] = new Knight(Player.BLACK);
        board[0][2] = new Bishop(Player.BLACK);
        board[0][3] = new Queen(Player.BLACK);
        board[0][4] = new King(Player.BLACK);
        board[0][5] = new Bishop(Player.BLACK);
        board[0][6] = new Knight(Player.BLACK);
        board[0][7] = new Rook(Player.BLACK);

    }

    /******************************************************************
     * Public boolean, returns true if the move is valid.
     * @param move The move that is being checked.
     * @return True if the move is valid.
     *****************************************************************/
    public boolean isValidMove(Move move) {
        IChessPiece moveFrom = board[move.fromRow][move.fromColumn];

        if (board[move.fromRow][move.fromColumn] == null) {
            enPassant = false;
            castle = false;
            return false;
        }

        if (board[move.fromRow][move.fromColumn]
                .isValidMove(move, board)) {
            enPassant = false;
            castle = false;
            return true;
        }

        if (numMoves > 0 && moveFrom.type().equals("Pawn") &&
                isValidEnPassant(move)) {
            enPassant = true;
            castle = false;
            return true;
        }

        if (moveFrom.type().equals("King") && isValidCastle(move)) {
            enPassant = false;
            castle = true;
            return true;
        }

        enPassant = false;
        castle = false;
        return false;
    }

    /******************************************************************
     * Public boolean, returns true if the move has been made and
     * does not put the player into check.
     * @param move The move that is being tried.
     * @return True if the move has been made and does not put the
     * player into check.
     *****************************************************************/
    public boolean tryMove(Move move) {
        if (isValidMove(move)) {
            move(move);
            if (!inCheck(board[move.toRow][move.toColumn].player())) {
                return true;
            }
            undo();
        }
        return false;
    }

    // Checks to see if a move is a valid En Passant.  Returns true
    // if valid.
    private boolean isValidEnPassant(Move move) {
        Move lastMove = moves.get(numMoves - 1);
        if (board[lastMove.toRow][lastMove.toColumn].type()
                .equals("Pawn")) {

            if (lastMove.fromRow - lastMove.toRow == 2) {

                board[lastMove.fromRow - 1][lastMove.toColumn] =
                        new Pawn(player.next());

                if (board[move.fromRow][move.fromColumn]
                        .isValidMove(move, board)) {
                    board[lastMove.fromRow - 1][lastMove.toColumn] =
                            null;
                    return true;
                }
                board[lastMove.fromRow - 1][lastMove.toColumn] = null;
            }

            if (lastMove.fromRow - lastMove.toRow == -2) {

                board[lastMove.fromRow + 1][lastMove.toColumn] =
                        new Pawn(player.next());

                if (board[move.fromRow][move.fromColumn]
                        .isValidMove(move, board)) {
                    board[lastMove.fromRow + 1][lastMove.toColumn] =
                            null;
                    return true;
                }
                board[lastMove.fromRow + 1][lastMove.toColumn] = null;
            }
        }
        return false;
    }

    // Checks to see if a move is a valid Castle.  Returns true if
    // valid.
    private boolean isValidCastle(Move move) {
        if (move.fromRow == move.toRow &&
                ((King) board[move.fromRow][move.fromColumn]).firstMove) {
            if (move.fromColumn - move.toColumn == 2) {
                for (int i = move.fromColumn - 1; i >= 1; --i) {
                    if (board[move.fromRow][i] != null) {
                        return false;
                    }
                }
                if (board[move.fromRow][0] != null &&
                        board[move.fromRow][0].type().equals("Rook")) {
                    if (((Rook) board[move.fromRow][0]).firstMove) {
                        return true;
                    }
                }
            }

            if (move.fromColumn - move.toColumn == -2) {
                for (int i = move.fromColumn + 1; i <= 6; ++i) {
                    if (board[move.fromRow][i] != null) {
                        return false;
                    }
                }
                if (board[move.fromRow][7] != null &&
                        board[move.fromRow][7].type().equals("Rook")) {
                    if (((Rook) board[move.fromRow][7]).firstMove) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /******************************************************************
     * This method executes a given move and stores values for
     * undoing the move.
     * @param move The move that is executed.
     *****************************************************************/
    public void move(Move move) {
        IChessPiece moveFrom = board[move.fromRow][move.fromColumn];
        IChessPiece moveTo = board[move.toRow][move.toColumn];
        isValidMove(move);

        if (moveFrom.type().equals("Pawn")) {

            if (((Pawn) moveFrom).firstMove) {
                firstPawnMoves.add(true);
                ((Pawn) moveFrom).firstMove = false;
            } else {
                firstPawnMoves.add(false);
            }


            if (move.toRow == 0 || move.toRow == 7) {
                upgradePawn = true;
            } else {
                upgradePawn = false;
            }


        } else {
            firstPawnMoves.add(false);
            upgradePawn = false;
        }

        if (moveFrom.type().equals("Rook")) {
            if (((Rook) moveFrom).firstMove) {
                firstRookMoves.add(true);
                ((Rook) moveFrom).firstMove = false;
            } else {
                firstRookMoves.add(false);
            }
        } else {
            firstRookMoves.add(false);
        }

        if (moveFrom.type().equals("King")) {
            if (((King) moveFrom).firstMove) {
                firstKingMoves.add(true);
                ((King) moveFrom).firstMove = false;
            } else {
                firstKingMoves.add(false);
            }
        } else {
            firstKingMoves.add(false);
        }

        if (enPassant) {
            board[move.fromRow][move.toColumn] = null;
            EpHappened.add(true);
        } else {
            EpHappened.add(false);
        }

        if (castle) {
            if (move.fromColumn > move.toColumn) {
                board[move.toRow][move.toColumn + 1] =
                        board[move.toRow][0];
                board[move.toRow][0] = null;
            } else {
                board[move.toRow][move.toColumn - 1] =
                        board[move.toRow][7];
                board[move.toRow][7] = null;
            }
            castleHappened.add(true);
        } else {
            castleHappened.add(false);
        }

        moves.add(numMoves, move);
        capturedPieces.add(numMoves, moveTo);
        movedPieces.add(numMoves, moveFrom);

        ++numMoves;
        board[move.toRow][move.toColumn] =
                board[move.fromRow][move.fromColumn];
        board[move.fromRow][move.fromColumn] = null;
        setNextPlayer();
    }

    /******************************************************************
     * This method updates GUIcode to reflect the current game status.
     *****************************************************************/
    public void updateStatus() {
        if (upgradePawn) {
            GUIcode = GUIcodes.UPGRADE;
        } else if (isCheckmate()) {
            GUIcode = GUIcodes.CHECKMATE;
        } else if (isDraw()) {
            GUIcode = GUIcodes.DRAW;
        } else if (inCheck(player)) {
            GUIcode = GUIcodes.IN_CHECK;
        } else {
            GUIcode = GUIcodes.NO_MESSAGE;
        }
    }

    /******************************************************************
     * This method promotes a pawn to the piece type in the String
     * parameter.
     * @param piece The type of piece that the pawn should be
     *              promoted to.
     *****************************************************************/
    public void upgradePawn(String piece) {
        int r = moves.get(numMoves - 1).toRow;
        int c = moves.get(numMoves - 1).toColumn;
        if (piece.equals("Rook")) {
            board[r][c] = new Rook(player.next());
            ((Rook) board[r][c]).firstMove = false;
        } else if (piece.equals("Knight")) {
            board[r][c] = new Knight(player.next());
        } else if (piece.equals("Bishop")) {
            board[r][c] = new Bishop(player.next());
        } else if (piece.equals("Queen")) {
            board[r][c] = new Queen(player.next());
        }
        upgradePawn = false;
    }

    /******************************************************************
     * This method undoes the previous move.
     *****************************************************************/
    public void undo() {
        if (numMoves > 0) {
            Move lastMove = moves.get(numMoves - 1);
            board[lastMove.fromRow][lastMove.fromColumn] =
                    movedPieces.get(numMoves - 1);
            board[lastMove.toRow][lastMove.toColumn] =
                    capturedPieces.get(numMoves - 1);
            IChessPiece moveFrom =
                    board[lastMove.fromRow][lastMove.fromColumn];

            if (moveFrom.type().equals("Pawn") &&
                    firstPawnMoves.get(numMoves - 1)) {
                ((Pawn) moveFrom).firstMove = true;
            }

            if (moveFrom.type().equals("Rook") &&
                    firstRookMoves.get(numMoves - 1)) {
                ((Rook) moveFrom).firstMove = true;
            }

            if (moveFrom.type().equals("King") &&
                    firstKingMoves.get(numMoves - 1)) {
                ((King) moveFrom).firstMove = true;
            }

            if (EpHappened.get(numMoves - 1)) {
                board[lastMove.fromRow][lastMove.toColumn] =
                        new Pawn(player);
                ((Pawn) board[lastMove.fromRow][lastMove.toColumn]).firstMove =
                        false;
            }

            if (castleHappened.get(numMoves - 1)) {
                if (lastMove.fromColumn > lastMove.toColumn) {
                    board[lastMove.toRow][0] =
                            board[lastMove.toRow][lastMove.toColumn +
                                    1];
                    board[lastMove.toRow][lastMove.toColumn + 1] = null;
                } else {
                    board[lastMove.toRow][7] =
                            board[lastMove.toRow][lastMove.toColumn -
                                    1];
                    board[lastMove.toRow][lastMove.toColumn - 1] = null;
                }
            }

            --numMoves;
            moves.remove(numMoves);
            capturedPieces.remove(numMoves);
            movedPieces.remove(numMoves);
            firstPawnMoves.remove(numMoves);
            firstRookMoves.remove(numMoves);
            firstKingMoves.remove(numMoves);
            EpHappened.remove(numMoves);
            castleHappened.remove(numMoves);
            numMoves = moves.size();
            setNextPlayer();
        }
    }

    /******************************************************************
     * This method checks to see if there is a checkmate.
     * @return True if game status is checkmate.
     *****************************************************************/
    public boolean isCheckmate() {
        if (inCheck(player) && isComplete()) {
            return true;
        }
        return false;
    }

    /******************************************************************
     * This method checks to see if there is a draw.
     * @return True if game status is draw.
     *****************************************************************/
    public boolean isDraw() {
        if (!inCheck(player) && isComplete()) {
            return true;
        }
        return false;
    }

    /******************************************************************
     * This method checks to see if the game is over.
     * @return True if the game is over.
     *****************************************************************/
    public boolean isComplete() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    Move m = new Move(r, c, 0, 0);
                    if (outOfCheckMove(m) != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //tries all possible moves for a given piece to see if that move
    // will take the player out of check.
    private Move outOfCheckMove(Move m) {
        for (int r = 0; r < SIZE; ++r) {
            m.toRow = r;
            for (int c = 0; c < SIZE; ++c) {
                m.toColumn = c;
                if (tryMove(m)) {
                    undo();
                    return m;
                }
            }
        }
        return null;
    }

    /******************************************************************
     * This method checks to see if a player is in check.
     * @param p The player that is possibly in check.
     * @return True if the player is in check.
     *****************************************************************/
    public boolean inCheck(Player p) {
        boolean inCheck = false;
        int rKing = 0;
        int cKing = 0;
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null && board[r][c].player() == p &&
                        board[r][c].type().equals("King")) {
                    rKing = r;
                    cKing = c;
                }
            }
        }

        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == p.next()) {
                    Move capMove = new Move(r, c, rKing, cKing);
                    if (isValidMove(capMove)) {
                        inCheck = true;
                    }

                }
            }
        }
        return inCheck;
    }

    /******************************************************************
     * This method returns the number of moves for the current game.
     * @return The number of moves for the game.
     *****************************************************************/
    public int numMoves() {
        return numMoves;
    }

    /******************************************************************
     * This method returns the GUIcode for the game.
     * @return The GUIcode for the game.
     *****************************************************************/
    public GUIcodes GUIcode() {
        return GUIcode;
    }

    /******************************************************************
     * This method returns the current player in the game.
     * @return The current player in the game.
     *****************************************************************/
    public Player currentPlayer() {
        return player;
    }

    /******************************************************************
     * This method returns the number of rows on the board.
     * @return The number of rows on the board.
     *****************************************************************/
    public int numRows() {
        return SIZE;
    }

    /******************************************************************
     * This method returns the number of columns on the board.
     * @return The number of columns on the board.
     *****************************************************************/
    public int numColumns() {
        return SIZE;
    }

    /******************************************************************
     * This method returns the IChessPiece at the requested location.
     * @param row The row of the IChessPiece
     * @param column The column of the IChessPiece
     * @return The number of moves for the game.
     *****************************************************************/
    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    /******************************************************************
     * This method sets the next player for the game.
     *****************************************************************/
    public void setNextPlayer() {
        player = player.next();
    }

    /******************************************************************
     * This method places a piece at a given location on the board.
     * @param row The row that the piece is being placed.
     * @param column The Column that the piece is being placed.
     * @param piece The IChessPiece that is being placed.
     *****************************************************************/
    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }

    /******************************************************************
     * This method clears the game board by setting all tiles to null.
     *****************************************************************/
    public void clearBoard() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                board[r][c] = null;
            }
        }
    }

    // This method attempts to find a checkmate move.
    private boolean moveCheckmate() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    for (int r2 = 0; r2 < SIZE; ++r2) {
                        for (int c2 = 0; c2 < SIZE; ++c2) {
                            Move m = new Move(r, c, r2, c2);
                            if (tryMove(m)) {
                                if (isCheckmate()) {
                                    return true;
                                } else {
                                    undo();
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // This method attempts to put the opponent into check.
    private boolean moveCheck() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    for (int r2 = 0; r2 < SIZE; ++r2) {
                        for (int c2 = 0; c2 < SIZE; ++c2) {
                            Move m = new Move(r, c, r2, c2);
                            if (tryMove(m)) {
                                if (inCheck(player) &&
                                        !pieceInDanger(r2, c2,
                                                player.next())) {
                                    return true;
                                } else {
                                    undo();
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // This method checks to see if a piece is in danger of being
    // captured.
    private boolean pieceInDanger(int row, int col, Player p) {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == p.next()) {
                    Move m = new Move(r, c, row, col);
                    if (tryMove(m)) {
                        undo();
                        return true;
                    }

                }
            }
        }
        return false;
    }


    // This method looks for a move that will take the piece out of
    // danger.
    private boolean moveOutOfDanger(int row, int col) {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                Move m = new Move(row, col, r, c);
                if (tryMove(m)) {
                    if (!pieceInDanger(r, c, player.next())) {
                        return true;
                    } else {
                        undo();
                    }
                }
            }
        }
        return false;
    }

    // This method looks for a capture move that will take the piece out
    // of danger.
    private boolean moveOutOfDangerCapture(int row, int col) {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player.next()) {
                    Move m = new Move(row, col, r, c);
                    if (tryMove(m)) {
                        if (!pieceInDanger(r, c, player.next())) {
                            return true;
                        } else {
                            undo();
                        }
                    }
                }
            }
        }
        return false;
    }

    // This method attempts to protect a piece that is in danger of
    // being captured.
    private boolean protectPiece() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    if (pieceInDanger(r, c, player)) {
                        if (moveOutOfDanger(r, c)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // This method attempts to protect a piece that is in danger of
    // being captured by capturing an opponent's piece.
    private boolean protectPieceCapture() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    if (pieceInDanger(r, c, player)) {
                        if (moveOutOfDangerCapture(r, c)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // This method looks for a move that will not place the piece in
    // danger.
    private boolean safeMove(int r, int c) {
        for (int r2 = 0; r2 < SIZE; ++r2) {
            for (int c2 = 0; c2 < SIZE; ++c2) {
                Move m = new Move(r, c, r2, c2);
                if (tryMove(m)) {
                    if (!pieceInDanger(r2, c2, player.next())) {
                        return true;
                    } else {
                        undo();
                    }
                }
            }
        }
        return false;
    }

    // This method looks for a capture move that will not place the
    // piece in danger.
    private boolean safeCaptureMove(int r, int c) {
        for (int r2 = 0; r2 < SIZE; ++r2) {
            for (int c2 = 0; c2 < SIZE; ++c2) {
                if (board[r2][c2] != null &&
                        board[r2][c2].player() == player.next()) {
                    Move m = new Move(r, c, r2, c2);
                    if (tryMove(m)) {
                        if (!pieceInDanger(r2, c2, player.next())) {
                            return true;
                        } else {
                            undo();
                        }
                    }
                }
            }
        }
        return false;
    }

    // This method attempts to make a safe move.
    private boolean movePieceSafe() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    if (safeMove(r, c)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // This method attempts to make a safe capture move.
    private boolean movePieceSafeCapture() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    if (safeCaptureMove(r, c)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // This method attempts to make a safe pawn move.
    private boolean movePawnSafe() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player &&
                        board[r][c].type().equals("Pawn")) {
                    if (safeMove(r, c)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // This method attempts to make a safe capture move for a pawn.
    private boolean movePawnSafeCapture() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player &&
                        board[r][c].type().equals("Pawn")) {
                    if (safeCaptureMove(r, c)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // This method attempts find a valid move.  May place the moved
    // piece in danger.
    private boolean movePieceUnsafe() {
        for (int r = 0; r < SIZE; ++r) {
            for (int c = 0; c < SIZE; ++c) {
                if (board[r][c] != null &&
                        board[r][c].player() == player) {
                    for (int r2 = 0; r2 < SIZE; ++r2) {
                        for (int c2 = 0; c2 < SIZE; ++c2) {
                            Move m = new Move(r, c, r2, c2);
                            if (tryMove(m)) {
                                return true;
                            }
                        }
                    }
                }

            }
        }
        return false;
    }


    /******************************************************************
     * This method preforms an AI move for the game.
     *****************************************************************/
    public void AI() {
        if (GUIcode != GUIcodes.CHECKMATE && GUIcode != GUIcodes.DRAW) {
            if (moveCheckmate()) {
                System.out.println("move checkmate");
            } else if (moveCheck()) {
                System.out.println("move check");
            } else if (protectPieceCapture()) {
                System.out.println("protect piece capture");
            } else if (protectPiece()) {
                System.out.println("protect piece");
            } else if (movePawnSafeCapture()) {
                System.out.println("move pawn safe capture");
            } else if (movePieceSafeCapture()) {
                System.out.println("move piece safe capture");
            } else if (movePawnSafe()) {
                System.out.println("move pawn safe");
            } else if (movePieceSafe()) {
                System.out.println("move piece safe");
            } else if (movePieceUnsafe()) {
                System.out.println("move piece unsafe");
            }
        }
    }
}