package Project3;

/**********************************************************************
 * CIS 163 Section 01
 * Project 3: Chess Game
 * Move Class
 *
 * This class represents a move for a chess game.
 *
 * @author George Fayette
 * @version 3/23/2019
 *********************************************************************/
public class Move {

    /**
     * Public Integers representing the to and from coordinates for a
     * move.
     */
    public int fromRow, fromColumn, toRow, toColumn;

    /******************************************************************
     * Default constructor does not initialize any fields.
     *****************************************************************/
    public Move() {
    }

    /******************************************************************
     * Public constructor sets fields to parameter values.
     * @param fromRow The row to be moved.
     * @param fromColumn The column to be moved.
     * @param toRow The row to be moved to.
     * @param toColumn The column to be moved to.
     *****************************************************************/
    public Move(int fromRow, int fromColumn, int toRow, int toColumn) {
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
    }

    /******************************************************************
     * Public toString returns a String representing the move.
     * @return A string representing the move.
     *****************************************************************/
    @Override
    public String toString() {
        return "Move [fromRow=" + fromRow + ", fromColumn=" +
                fromColumn + ", toRow=" + toRow + ", toColumn=" +
                toColumn + "]";
    }


}