 

import javax.swing.JButton;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Daniel Dimuna
 * @version September 2022
 */

public class PanelCell extends JButton {

    private NonogramPanel np;
    private int row;
    private int col;
    private int state;
    private Nonogram game;

    public PanelCell(NonogramPanel np, int r, int c, int state) {

        this.np = np;
        this.row = r;
        this.col = c;
        this.state = state;
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(20, 30));

        addMouseListener(new MouseAdapter() { // implememnt a mouse listener
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); // call the constructor from the mouse event

                updateGame();
            }

        });

    }

    public void updateBackground(int state) {
        if (state == Nonogram.EMPTY) { // check the value of the cell then change the color accordingly
            setBackground(Color.WHITE);
            setText("x");
            setForeground(Color.RED);
        }

        else if (state == Nonogram.FULL) {
            setBackground(Color.BLACK);
            setText("");

        } else if (state == Nonogram.UNKNOWN) {
            setText("");
            setBackground(Color.WHITE);
        }
    }

    private void updateGame() { 
        if (state == 0) {
            state = 1;
            np.makeMove(row, col, 1);
            np.checkWin();

        } else if (state == 1) {
            state = 2;
            np.makeMove(row, col, 2);
            np.checkWin();

        } else if (state == 2) {
            state = 0;
            np.makeMove(row, col, 0);
            np.checkWin();

        }
    }

}
