package model;

import java.util.Arrays;

public class PureGame {
    private int width;
    private int height;
    private int[][] cells;
    private int[][] currentShape;
    private int[][] nextShape;

    public PureGame(int height, int width) {
        this.height = height;
        this.width = width;
        this.cells = new int[height][width];
    }

    public void setCells(int[][] cells) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++)
                this.cells[y][x] = cells[y][x];
        }
    }

    public void setCurrentShape(int[][] currentShape) {
        this.currentShape = currentShape;
    }

    public void setNextShape(int[][] nextShape) {
        this.nextShape = nextShape;
    }

    @Override
    public String toString() {
        return "model.PureGame{" +
                "width=" + width +
                ", height=" + height +
                ", cells=" + Arrays.deepToString(cells) +
                ", currentShape=" + Arrays.deepToString(currentShape) +
                ", nextShape=" + Arrays.deepToString(nextShape) +
                '}';
    }
}

