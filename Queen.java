package Project3;

/**********************************************************************
 * CIS 163 Section 01
 * Project 3: Chess Game
 * Queen Class
 *
 * This class represents a ChessPiece that is a queen.
 *
 * @author George Fayette
 * @version 3/23/2019
 *********************************************************************/
public class Queen extends ChessPiece {

    /******************************************************************
     * Public constructor sets player to parameter value.
     * @param player The player type.
     *****************************************************************/
    public Queen(Player player) {
        super(player);
    }

    /******************************************************************
     * Public String, returns the ChessPiece type.
     * @return A string representing the ChessPiece type.
     *****************************************************************/
    public String type() {
        return "Queen";
    }

    /******************************************************************
     * Public boolean, returns true if the move is valid.
     * @param move The move that is being checked.
     * @param board The array of IChessPieces that is being checked.
     * @return True if the move is valid.
     *****************************************************************/
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        Bishop move1 = new Bishop(board[move.fromRow][move.fromColumn].player());
        Rook move2 = new Rook(board[move.fromRow][move.fromColumn].player());
        return (move1.isValidMove(move, board) || move2.isValidMove(move, board));
    }
}
