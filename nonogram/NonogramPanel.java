package nonogram;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A GUI user interface to a Nonogram puzzle.
 * 
 * @author Daniel Dimuna
 * @version September 2022
 */

@SuppressWarnings({ "deprecation", "serial" })
public class NonogramPanel extends JPanel implements Observer {

    private static boolean traceOn = false;

    public NonogramPanel() {
        scnr = new Scanner(System.in);
        Scanner fs = null;
        try {
            fs = new Scanner(new File(NGFILE));
        } catch (FileNotFoundException e) {
            System.out.println(NGFILE + "not found");
        }
        game = new Nonogram(fs);
        Moves = new Stack<Assign>();

        // Nonogram nonogram = new Nonogram(fs);
        int row, col;

        JPanel grid = new JPanel(new GridLayout(Nonogram.MIN_SIZE, Nonogram.MIN_SIZE)); // create the gridlayout for the
                                                                                        // cells
        JPanel rightNums = new JPanel(new GridLayout(Nonogram.MIN_SIZE, 1)); // create the layout for the row
                                                                             // constraints
        JPanel topNums = new JPanel(new GridLayout(1, Nonogram.MIN_SIZE)); // create the layout for the colum constraint
        cells = new PanelCell[game.getNumRows()][game.getNumCols()];

        // Add the row constraints
        for (row = 0; row < Nonogram.MIN_SIZE; row++) {
            int[] rowNum = game.getRowNums(row);
            JLabel rowlabel = new JLabel(Arrays.toString(rowNum));
            JPanel rowPanel = new JPanel();
            rowPanel.add(rowlabel);
            rightNums.add(rowPanel);
        }

        // Add topNums
        for (col = 0; col < Nonogram.MIN_SIZE; col++) {
            int[] colNum = game.getColNums(col);
            JLabel colLabel = new JLabel(Arrays.toString(colNum));
            JPanel colPanel = new JPanel();
            colPanel.add(colLabel);
            topNums.add(colPanel);
        }

        // Add cells to grid
        for (row = 0; row < game.getNumRows(); row++) {
            for (col = 0; col < game.getNumCols(); col++) {
                cells[row][col] = new PanelCell(this, row, col, Nonogram.UNKNOWN);
                grid.add(cells[row][col]);
            }
        }

        // set up buttons & status bar
        clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clear();
            }
        });

        undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                undo();
            }
        });

        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                save();
            }
        });

        load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                load();
            }
        });
        // Add grid, rightNums, and topNums to container panel
        JPanel container = new JPanel(new BorderLayout());
        container.add(grid, BorderLayout.CENTER);
        container.add(rightNums, BorderLayout.WEST);
        container.add(topNums, BorderLayout.NORTH);

        setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);

        JPanel center = new JPanel(new GridLayout(1, 4));
        center.add(clear);
        center.add(undo);
        center.add(save);
        center.add(load);
        center.setPreferredSize(new Dimension(30, 30));

        add(center, BorderLayout.NORTH);
        status = new JTextArea();

        add(new JScrollPane(status), BorderLayout.SOUTH);
        // game = new Nonogram(fs);
        game.addObserver(this);
    }

    void makeMove(int row, int col, int state) {
        if ((row < 0) || (row > game.getNumRows()))
            throw new NonogramException("invalid row (" + row + ")");
        if ((col < 0) || (col > game.getNumCols()))
            throw new NonogramException("invalid col (" + col + ")");
        if ((state != Nonogram.UNKNOWN) && (state != Nonogram.EMPTY) && (state != Nonogram.FULL))
            throw new NonogramException("invalid state (" + state + ")");
        Assign userMove = new Assign(row, col, state);
        Moves.add(userMove);
        game.setState(row, col, state);
    }

    public void checkWin() {
        if (game.isSolved()) {
            setStatus("Puzzle sucessfully completed ");
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Game cleared");
        }
    }

    /**
     * Updates the PanelCells when the underlying model cells are assigned
     * 
     * @param o   the observable
     * @param arg the cell that was assigned
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg == null)
            throw new IllegalArgumentException("arg (Cell) is null");
        Cell c = (Cell) arg;
        cells[c.getRow()][c.getCol()].updateBackground(c.getState());
    }

    public void undo() {
        Moves.peek(); // select the last item in the stack
        if (Moves.empty()) { // check if the stack is empty
            throw new EmptyStackException();
        }
        // System.out.println(Moves.peek());
        Moves.pop(); // remove the last item in the stack
        game.clear();
        for (Assign moves : Moves) { // loop over the stack and pass the new stack into the game.
            game.setState(moves);
        }

    }

    public void clear() {

        // create a new game instance
        game.clear();
        game.addObserver(this);
            setStatus(" ");

    }

    public void save() {
        try {
            PrintStream ps = new PrintStream(new File(FILENAME));
            for (Assign moves : Moves)
                ps.println(moves.toStringForFile());
            ps.close();
            setStatus("game saved sucessfully");
        } catch (IOException e) {
            setStatus("Sorry, there is an error, please try again");
        }
    }

    public void load() {
        try {
            Scanner fscnr = new Scanner(new File(FILENAME));
            clear();
            // System.out.println("game cleared ahhhhhhhhh!!");
            // System.out.println(fscnr.nextLine().split(" "));
            while (fscnr.hasNextLine()) {
                String[] values = fscnr.nextLine().split(" ");
                Assign moves = new Assign(Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                        Integer.parseInt(values[2]));
                game.setState(moves);
                Moves.push(moves);

            }
            fscnr.close();

            setStatus("game loaded from file");
        } catch (IOException e) {
            setStatus("an input output error occurred");
        }
    }

    /**
     * Sets the status bar to a given string
     * 
     * @param s the new status
     */
    void setStatus(String s) {
        status.setText(s);
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Nonogram");
        frame.setPreferredSize(new Dimension(700, 500));
        frame.setVisible(true);
        // JPanel leftPanel = new JPanel();
        // JPanel topPanel = new JPanel();
        // topPanel.setPreferredSize(new Dimension(900, 80));

        NonogramPanel panel = new NonogramPanel();

        // leftPanel.setPreferredSize(new Dimension(80, 400));
        // leftPanel.setBackground(Color.RED);
        // panel.setLayout(new BorderLayout());

        frame.setLayout(new BorderLayout());
        // // test
        frame.add(panel);
        // frame.getContentPane().add(panelTop, BorderLayout.NORTH);

        // // test end

        // frame.add(topPanel, BorderLayout.NORTH);
        // frame.add(leftPanel, BorderLayout.WEST);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private PanelCell[][] cells = null;
    private JPanel grid = null;
    private JPanel rightNums = null;// test
    private JPanel topNums = null;// test
    private JPanel rnums = null;

    private JButton clear = null;
    private JButton undo = null;
    private JButton save = null;
    private JButton load = null;
    private JTextArea status = null;
    private Nonogram game = null;
    private Stack<Assign> Moves = null;
    private static final String FILENAME = "nons/save.non";
    private static final String NGFILE = "nons/tiny.non";

    private Scanner scnr;
}
