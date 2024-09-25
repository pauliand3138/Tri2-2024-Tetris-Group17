package model;

public class Move {
    private int col;
    private int rotation;

    public Move(int col, int rotation) {
        this.col = col;
        this.rotation = rotation;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}

