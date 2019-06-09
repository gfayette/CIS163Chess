package Project3;

import java.awt.Dimension;
import javax.swing.JFrame;

/**********************************************************************
 * CIS 163 Section 01
 * Project 3: Chess Game
 * ChessGUI Class
 *
 * This class starts the game by creating a ChessPanel class.
 *
 * @author George Fayette
 * @version 3/23/2019
 *********************************************************************/
public class ChessGUI {

    /******************************************************************
     * Main method for starting the game.
     * @param args The String arguments that are passed to the programs.
     *****************************************************************/
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChessPanel panel = new ChessPanel();
        frame.getContentPane().add(panel);

        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(650, 730));
        frame.pack();
        frame.setVisible(true);
    }
}
