package Project3;

/**********************************************************************
 * CIS 163 Section 01
 * Project 3: Chess Game
 * Rook Class
 *
 * This class represents a ChessPiece that is a rook.
 *
 * @author George Fayette
 * @version 3/23/2019
 *********************************************************************/
public class Rook extends ChessPiece {

    /**
     * Public boolean representing whether or not the rook has moved.
     */
    public boolean firstMove;

    /******************************************************************
     * Public constructor sets player to parameter value.
     * @param player The player type.
     *****************************************************************/
    public Rook(Player player) {
        super(player);
        firstMove = true;
    }

    /******************************************************************
     * Public String, returns the ChessPiece type.
     * @return A string representing the ChessPiece type.
     *****************************************************************/
    public String type() {
        return "Rook";
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

            if (vDistance != 0 && hDistance != 0) {
                valid = false;
            } else {
                int absDistance = Math.abs(vDistance + hDistance);
                if (vDistance > 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow + i][move.fromColumn] !=
                                null) {
                            valid = false;
                        }
                    }
                } else if (vDistance < 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow - i][move.fromColumn] !=
                                null) {
                            valid = false;
                        }
                    }
                } else if (hDistance > 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow][move.fromColumn + i] !=
                                null) {
                            valid = false;
                        }
                    }
                } else if (hDistance < 0) {
                    for (int i = 1; i < absDistance; ++i) {
                        if (board[move.fromRow][move.fromColumn - i] !=
                                null) {
                            valid = false;
                        }
                    }
                }
            }
        }
        return valid;
    }
}
