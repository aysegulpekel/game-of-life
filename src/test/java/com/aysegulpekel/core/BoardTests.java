package com.aysegulpekel.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTests {
    // Initialize a matrix
    private Cell[][] matrix = new Cell[5][5];

    private Board board = new Board(matrix, 5, 5);

    @Test
    public void exist() {
        // If matrix has initialized correctly, then it should be existed already!
        // All the cells have to be dead and lonely
        matrix = board.matrix;
        assertEquals(0, matrix[0][0].getStatus());
        assertEquals(0, matrix[0][1].getStatus());
        assertEquals(0, matrix[0][2].getStatus());

        assertEquals(0, matrix[0][0].getNeighbourCount());
        assertEquals(0, matrix[0][1].getNeighbourCount());
        assertEquals(0, matrix[0][2].getNeighbourCount());
    }

    @Test
    public void explore() {
        // Blinker pattern is initialized
        matrix = board.matrix;
        matrix[1][2].setStatus(1);
        matrix[2][2].setStatus(1);
        matrix[3][2].setStatus(1);

        board.explore();

        // Matrix[2][1] is dead and has 3 neighbours
        assertEquals(0, matrix[2][1].getStatus());
        assertEquals(3, matrix[2][1].getNeighbourCount());

        // Matrix[1][2] is alive and has only 1 neighbour
        assertEquals(1, matrix[1][2].getStatus());
        assertEquals(1, matrix[1][2].getNeighbourCount());
    }

    @Test
    public void change() {
        // Blinker pattern is initialized
        matrix = board.matrix;
        matrix[1][2].setStatus(1);
        matrix[2][2].setStatus(1);
        matrix[3][2].setStatus(1);

        // Blinker pattern neighbourhood is also initialized
        matrix[1][2].setNeighbourCount(1);
        matrix[2][2].setNeighbourCount(2);
        matrix[3][2].setNeighbourCount(1);
        matrix[2][1].setNeighbourCount(3);
        matrix[2][3].setNeighbourCount(1);

        board.change();

        // Expectations are the changes within the cells which have 3 neighbours
        assertEquals(1, matrix[2][1].getStatus());
        assertEquals(0, matrix[1][2].getStatus());
    }

    @Test
    public void evolve() {
        // If the cell is dead but has 3 neighbours then it regenerates otherwise keeps being alive
        assertEquals(1, Board.evolve(3, 0));
        assertEquals(1, Board.evolve(3, 1));

        // If the cell has 2 neighbours then it stays the same
        assertEquals(0, Board.evolve(2, 0));
        assertEquals(1, Board.evolve(2, 1));

        // If the cell has more than 3 or less than 2 neighbours then it dies
        assertEquals(0, Board.evolve(4, 1));
        assertEquals(0, Board.evolve(1, 1));
    }

    @Test
    public void findNeighbourCount() {
        // Blinker pattern is initialized
        matrix = board.matrix;
        matrix[1][2].setStatus(1);
        matrix[2][2].setStatus(1);
        matrix[3][2].setStatus(1);

        // Neighbour counts of living cells are asked by the Blinker pattern
        assertEquals(1, Board.findNeighbourCount(matrix, 1, 2));
        assertEquals(2, Board.findNeighbourCount(matrix, 2, 2));
        assertEquals(1, Board.findNeighbourCount(matrix, 3, 2));
        assertEquals(3, Board.findNeighbourCount(matrix, 2, 1));
        assertEquals(3, Board.findNeighbourCount(matrix, 2, 3));
    }
}