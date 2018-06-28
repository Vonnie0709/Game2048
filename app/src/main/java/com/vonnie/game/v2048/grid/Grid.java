package com.vonnie.game.v2048.grid;

import com.vonnie.game.v2048.cell.Cell;
import com.vonnie.game.v2048.cell.Tile;

import java.util.ArrayList;

/**
 * @author LongpingZou
 * @date 2018/6/19
 */
public class Grid {

    /**
     * normal game field
     */
    public Tile[][] field;
    /**
     * undo game field
     */
    public Tile[][] undoField;
    /**
     * field buffers
     */
    private Tile[][] bufferField;

    /**
     * Constructor of Grid
     *
     * @param sizeX
     * @param sizeY
     */
    public Grid(int sizeX, int sizeY) {
        field = new Tile[sizeX][sizeY];
        undoField = new Tile[sizeX][sizeY];
        bufferField = new Tile[sizeX][sizeY];
        clearGrid();
        clearUndoGrid();
    }

    /**
     * Random generate available cell
     *
     * @return
     */
    public Cell randomAvailableCell() {
        ArrayList<Cell> availableCells = getAvailableCells();
        if (availableCells.size() >= 1) {
            return availableCells.get((int) Math.floor(Math.random() * availableCells.size()));
        }
        return null;
    }

    /**
     * get all of available cells
     *
     * @return
     */
    public ArrayList<Cell> getAvailableCells() {
        ArrayList<Cell> availableCells = new ArrayList<Cell>();
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    availableCells.add(new Cell(xx, yy));
                }
            }
        }
        return availableCells;
    }


    /**
     * cell available or not
     *
     * @return
     */
    public boolean isCellsAvailable() {
        return (getAvailableCells().size() >= 1);
    }

    /**
     * cell available or not
     *
     * @param cell
     * @return
     */
    public boolean isCellAvailable(Cell cell) {
        return !isCellOccupied(cell);
    }


    public boolean isCellOccupied(Cell cell) {
        return (getCellContent(cell) != null);
    }

    /**
     * get content of cell
     *
     * @param cell
     * @return
     */
    public Tile getCellContent(Cell cell) {
        if (cell != null && isCellWithinBounds(cell)) {
            return field[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    }


    /**
     * get content of cell
     *
     * @param x
     * @param y
     * @return
     */
    public Tile getCellContent(int x, int y) {
        if (isCellWithinBounds(x, y)) {
            return field[x][y];
        } else {
            return null;
        }
    }

    /**
     * get cell bounds status
     *
     * @param cell
     * @return
     */
    public boolean isCellWithinBounds(Cell cell) {
        return 0 <= cell.getX() && cell.getX() < field.length
                && 0 <= cell.getY() && cell.getY() < field[0].length;
    }

    /**
     * get cell bounds status
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isCellWithinBounds(int x, int y) {
        return 0 <= x && x < field.length
                && 0 <= y && y < field[0].length;
    }


    /**
     * insert tile
     *
     * @param tile
     */
    public void insertTile(Tile tile) {

        field[tile.getX()][tile.getY()] = tile;

    }

    /**
     * remove tile
     *
     * @param tile
     */
    public void removeTile(Tile tile) {

        field[tile.getX()][tile.getY()] = null;
    }


    /**
     * save tiles
     */
    public void saveTiles() {
        for (int xx = 0; xx < bufferField.length; xx++) {
            for (int yy = 0; yy < bufferField[0].length; yy++) {
                if (bufferField[xx][yy] == null) {
                    undoField[xx][yy] = null;
                } else {
                    undoField[xx][yy] = new Tile(xx, yy, bufferField[xx][yy].getValue());
                }
            }
        }
    }


    /**
     * prepare to save tiles
     */
    public void prepareSaveTiles() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    bufferField[xx][yy] = null;
                } else {
                    bufferField[xx][yy] = new Tile(xx, yy, field[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * revert tiles
     */
    public void revertTiles() {
        for (int xx = 0; xx < undoField.length; xx++) {
            for (int yy = 0; yy < undoField[0].length; yy++) {
                if (undoField[xx][yy] == null) {
                    field[xx][yy] = null;
                } else {
                    field[xx][yy] = new Tile(xx, yy, undoField[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * clear grid
     */
    public void clearGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                field[xx][yy] = null;
            }
        }
    }


    /**
     * clear undo grid
     */
    public void clearUndoGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                undoField[xx][yy] = null;
            }
        }
    }
}
