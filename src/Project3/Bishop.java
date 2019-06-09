package Project3;

/**********************************************************************
 * CIS 163 Section 01
 * Project 3: Chess Game
 * Bishop Class
 *
 * This class represents a ChessPiece that is a bishop.
 *
 * @author George Fayette
 * @version 3/23/2019
 *********************************************************************/
public class Bishop extends ChessPiece {

    /******************************************************************
     * Public constructor sets player to parameter value.
     * @param player The player type.
     *****************************************************************/
    public Bishop(Player player) {
        super(player);
    }

    /******************************************************************
     * Public String, returns the ChessPiece type.
     * @return A string representing the ChessPiece type.
     *****************************************************************/
    public String type() {
        return "Bishop";
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

            if (Math.abs(vDistance) - Math.abs(hDistance) != 0) {
                valid = false;
            } else {
                int absDistance = Math.abs(vDistance);
                if (vDistance > 0 && hDistance > 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow + i][move.fromColumn +
                                i] != null) {
                            valid = false;
                        }
                    }
                } else if (vDistance > 0 && hDistance < 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow + i][move.fromColumn -
                                i] != null) {
                            valid = false;
                        }
                    }
                } else if (vDistance < 0 && hDistance > 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow - i][move.fromColumn +
                                i] != null) {
                            valid = false;
                        }
                    }
                } else if (vDistance < 0 && hDistance < 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow - i][move.fromColumn -
                                i] != null) {
                            valid = false;
                        }
                    }
                }
            }
        }
        return valid;
    }
}