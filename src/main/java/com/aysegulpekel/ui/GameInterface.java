package com.aysegulpekel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import org.apache.log4j.Logger;

import com.aysegulpekel.core.Board;
import com.aysegulpekel.core.Cell;

/**
 * Game Interface represents the life on the board
 */
public class GameInterface extends JFrame implements ActionListener {
    private static Dimension BOARD_SIZE = new Dimension(500, 500);
    private static final int CELL_SIZE = 10;
    private JButton start, reset;
    private GameBoard gameBoard;
    private Thread game;

    private static final Logger LOGGER = Logger.getLogger(GameInterface.class);

    /**
     * Creates the menu to choose start or reset the board
     */
    public GameInterface() {
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);

        start = new JButton("start");
        start.addActionListener(this);

        reset = new JButton("reset");
        reset.addActionListener(this);

        menu.add(start);
        menu.add(reset);

        gameBoard = new GameBoard();
        add(gameBoard);
    }

    /**
     * @param isBeingPlayed keeps the board running
     */
    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            start.setEnabled(false);
            game = new Thread(gameBoard);
            game.start();
        } else {
            start.setEnabled(true);
            game.interrupt();
            gameBoard.resetBoard();
        }
    }

    /**
     * Listens reset button to clean the board or start button to play
     *
     * @param e user action event performed on board
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(reset)) {
            gameBoard.resetBoard();
            gameBoard.repaint();
            setGameBeingPlayed(false);
        } else if (e.getSource().equals(start)) {
            setGameBeingPlayed(true);
        }
    }

    /**
     * Game board is visual representation class of th board with its listeners and runnable application
     */
    private class GameBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {
        // Array list to keep clicked points on board
        private ArrayList<Point> point = new ArrayList<Point>(0);

        /**
         * Adds action listeners to the board
         */
        public GameBoard() {
            // Add resizing listener
            addComponentListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        /**
         * Overrides JComponent method
         * <p>
         * Points represents the clicked cells which are indicated with blue
         * and grid is created by drawLine and drawRect methods
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                for (Point newPoint : point) {
                    int cellX = newPoint.x * CELL_SIZE;
                    int cellY = newPoint.y * CELL_SIZE;
                    g.setColor(Color.blue);
                    g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                }
            } catch (ConcurrentModificationException cme) {
                LOGGER.info(cme.getMessage(), cme);
            }

            g.setColor(Color.BLACK);
            g.drawRect(0, 0, BOARD_SIZE.width, BOARD_SIZE.height);

            // Draws vertical lines
            for (int i = 0; i <= BOARD_SIZE.width; i += CELL_SIZE) {
                g.drawLine(i, 0, i, BOARD_SIZE.height);
            }

            // Draws horizontal lines
            for (int i = 0; i <= BOARD_SIZE.height; i += CELL_SIZE) {
                g.drawLine(0, i, BOARD_SIZE.width, i);
            }
        }

        // Updates the board when the frame is resized
        private void updateArraySize() {
            ArrayList<Point> removeList = new ArrayList<Point>(0);
            for (Point current : point) {
                if ((current.x >= BOARD_SIZE.width - CELL_SIZE) || (current.y >= BOARD_SIZE.height - CELL_SIZE)) {
                    removeList.add(current);
                }
            }
            point.removeAll(removeList);
            repaint();
        }

        // Adds a new clicked point to point array and repaints
        public void addPoint(int x, int y) {
            if (!point.contains(new Point(x, y))) {
                point.add(new Point(x, y));
            }
            repaint();
        }

        // Calls add point with action's positions
        public void addPoint(MouseEvent me) {
            int x = me.getPoint().x / CELL_SIZE;
            int y = me.getPoint().y / CELL_SIZE;

            if ((x >= 0) && (x < BOARD_SIZE.width) && (y >= 0) && (y < BOARD_SIZE.height)) {
                addPoint(x, y);
            }
        }

        // Clears the board
        public void resetBoard() {
            point.clear();
        }

        // Updatas the boards dimension and cells when the frame is resized
        public void componentResized(ComponentEvent e) {
            BOARD_SIZE = new Dimension(getWidth() + CELL_SIZE, getHeight() + CELL_SIZE);
            updateArraySize();
        }

        public void componentMoved(ComponentEvent e) {

        }

        public void componentShown(ComponentEvent e) {

        }

        public void componentHidden(ComponentEvent e) {

        }

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {

        }

        // Allows to get clicked when mouse released
        public void mouseReleased(MouseEvent e) {
            addPoint(e);
        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

        // Allows to get clicked when mouse dragged
        public void mouseDragged(MouseEvent e) {
            addPoint(e);
        }

        public void mouseMoved(MouseEvent e) {

        }

        /**
         * Run is the method to call recursively to make life continue as soon as the board is not reset
         * <p>
         * Creates the board, initializes the cells status as set alive by the clicks.
         */
        public void run() {
            Cell[][] matrix = new Cell[BOARD_SIZE.width][BOARD_SIZE.height];
            Board board = new Board(matrix, BOARD_SIZE.width, BOARD_SIZE.height);

            // For each click matrix's corresponding cell's status is 1 as alive.
            for (Point current : point) {
                matrix[current.x][current.y].setStatus(1);
            }

            // Board lives and generates new life conditions
            board.live();

            // Each cell's status is checked for keeping track of surviving cells
            ArrayList<Point> survivingCells = new ArrayList<Point>(0);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j].getStatus() == 1) {
                        survivingCells.add(new Point(i, j));
                    }
                }
            }

            // Clear the board first
            resetBoard();

            // Represent the surviving cells
            point.addAll(survivingCells);
            repaint();
            try {
                // Sleep between lives to see the change easily
                Thread.sleep(200);
                // Run recursively
                run();
            } catch (InterruptedException ex) {
                LOGGER.info(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Setup the interface specifics
     *
     * @param args main arguments
     */
    public static void main(String[] args) {
        JFrame game = new GameInterface();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setTitle("Conway's Game of Life");
        game.setSize(500, 500);
        game.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - game.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - game.getHeight()) / 2);
        game.setVisible(true);
    }
}