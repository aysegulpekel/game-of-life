package com.aysegulpekel.core;

/**
 * Cells are the objects of the life on board.
 */
public class Cell {

    private int status;
    private int neighbourCount;

    /**
     * @param status         0 if the cell is dead or 1 if the cell is alive
     * @param neighbourCount number of neighbours
     */
    public Cell(int status, int neighbourCount) {
        this.status = status;
        this.neighbourCount = neighbourCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNeighbourCount() {
        return neighbourCount;
    }

    public void setNeighbourCount(int neighbourCount) {
        this.neighbourCount = neighbourCount;
    }
}