public class Move {
    private int col;
    private int[][] shape;

    public Move(int col, int[][] shape) {
        this.col = col;
        this.shape = shape;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int[][] getShape() {
        return shape;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }
}

