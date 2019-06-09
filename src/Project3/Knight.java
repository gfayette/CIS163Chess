package Project3;

/**********************************************************************
 * CIS 163 Section 01
 * Project 3: Chess Game
 * Knight Class
 *
 * This class represents a ChessPiece that is a knight.
 *
 * @author George Fayette
 * @version 3/23/2019
 *********************************************************************/
public class Knight extends ChessPiece {

    /******************************************************************
     * Public constructor sets player to parameter value.
     * @param player The player type.
     *****************************************************************/
    public Knight(Player player) {
        super(player);
    }

    /******************************************************************
     * Public String, returns the ChessPiece type.
     * @return A string representing the ChessPiece type.
     *****************************************************************/
    public String type() {
        return "Knight";
    }

    /******************************************************************
     * Public boolean, returns true if the move is valid.
     * @param move The move that is being checked.
     * @param board The array of IChessPieces that is being checked.
     * @return True if the move is valid.
     *****************************************************************/
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        boolean valid = true;

        if (!super.isValidMove(move, board)) {
            valid = false;
        } else {
            int vDistance = move.toRow - move.fromRow;
            int hDistance = move.toColumn - move.fromColumn;

            if (vDistance < 0) {
                vDistance *= -1;
            }
            if (hDistance < 0) {
                hDistance *= -1;
            }

            if (!(vDistance == 1 && hDistance == 2) &&
                    !(vDistance == 2 && hDistance == 1)) {
                valid = false;
            }
        }
        return valid;
    }
}