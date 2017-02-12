package com.aysegulpekel.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Board presents the life with its cells
 * There are rules to keep alive the board
 * <p>
 * Cells within neighbours
 * 1. less than 2 cannot survive because of loneliness
 * 2. more than 3 cannot survive either because of overpopulation
 * 3. exactly 3, reproduce, for no reason
 * 4. exactly 2, keeps their status stable
 */
public class Board {
    int width, height;
    public Cell[][] matrix = new Cell[width][height];

    /**
     * @param matrix given initial matrix of board
     * @param width  width of board
     * @param height height of board
     */
    public Board(Cell[][] matrix, int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = matrix;
        exist();
    }

    /**
     * Life continues with the cells explore themselves and evolve accordingly
     */
    public void live() {
        explore();
        change();
    }

    /**
     * Cells start being existing once the board initialized
     * Every location of the board implies the cells location
     * and cells are not aware of their neighbours yet
     * since they are created simultaneously
     */
    private void exist() {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                Cell cell = new Cell(0, 0);
                matrix[x][y] = cell;
            }
        }
    }

    /**
     * Cells start to explore around, figure out how many neighbours do they have
     */
    public void explore() {
        int neighborCount;
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                neighborCount = findNeighbourCount(matrix, x, y);
                matrix[x][y].setNeighbourCount(neighborCount);
            }
        }
    }

    /**
     * Life of the cells evolve by their neighbours count
     * They decide to continue or not according to their neighbourhood
     */
    public void change() {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                matrix[x][y].setStatus(evolve(matrix[x][y].getNeighbourCount(), matrix[x][y].getStatus()));
            }
        }
    }

    /**
     * Life is not full of surprises.
     * A cell can have 8 neighbours at most and there are rules for each possibility
     * 2 neighbour makes the status stable
     * 3 neighbour makes a new regenerated cell
     * Less or more makes the cell dead
     *
     * @param neighbourCount neighbour count of the cell
     * @param status         status of the cell
     * @return new status of the cell which is evolved by the neighbour count
     */
    public static int evolve(int neighbourCount, int status) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 0);
        map.put(1, 0);
        map.put(2, status); // cell keeps the status stable
        map.put(3, 1);      // cell is regenerated if it was dead else nothing changes
        map.put(4, 0);
        map.put(5, 0);
        map.put(6, 0);
        map.put(7, 0);
        map.put(8, 0);
        return map.get(neighbourCount);
    }

    /**
     * Finds the neighbour count for a given position of a cell
     * There are 8 neighbour position for every cell, horizontal, vertical and diagonal respectively
     * If statements consider these positions and boundaries of the board
     *
     * @param matrix matrix of the board
     * @param x      x position on board
     * @param y      y position on board
     * @return the neighbours count of the cell
     */
    public static int findNeighbourCount(Cell[][] matrix, int x, int y) {
        int count = 0;

        if (x > 0 && y > 0 && matrix[x - 1][y - 1].getStatus() == 1) {
            count += 1;
        }

        if (x > 0 && matrix[x - 1][y].getStatus() == 1) {
            count += 1;
        }

        if (x > 0 && y < matrix[x].length - 1 && matrix[x - 1][y + 1].getStatus() == 1) {
            count += 1;
        }

        if (y > 0 && matrix[x][y - 1].getStatus() == 1) {
            count += 1;

        }
        if (y < matrix[x].length - 1 && matrix[x][y + 1].getStatus() == 1) {
            count += 1;
        }

        if (y > 0 && x < matrix.length - 1 && matrix[x + 1][y - 1].getStatus() == 1) {
            count += 1;
        }
        if (x < matrix.length - 1 && matrix[x + 1][y].getStatus() == 1) {
            count += 1;
        }

        if (x < matrix.length - 1 && y < matrix[x].length - 1 && matrix[x + 1][y + 1].getStatus() == 1) {
            count += 1;
        }
        return count;
    }
}