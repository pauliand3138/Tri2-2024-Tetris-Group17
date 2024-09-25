package model;

import java.awt.Color;

public class BlockInfo {
    private int number;
    private int [][] shape;
    private Color colour;
    private int[][][] shapes; // To store possible shapes for rotation
    private int currentRotation;

    public BlockInfo(int [][] shape, Color colour, int number){
        this.shape = shape;
        this.colour = colour;
        this.number = number;
        this.currentRotation = 1;
        createShapes();
    }
    public int[][] getShape(){
        return shape;
    }
    public void setShape(int[][] shape){
        this.shape = shape;
    }
    public Color getColour(){
        return colour;
    }
    public int getNumber(){
        return number;
    }
    public int getRows(){
        return shape.length;
    } // block height
    public int getColumns(){
        return shape[0].length;
    } // block width
    public int getCurrentRotation() {
        return currentRotation;
    }
    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }
    public int[][][] getShapes() {
        return shapes;
    }
    public void setShapes(int[][][] shapes) {
        this.shapes = shapes;
    }

    public void createShapes() {
        shapes = new int[4][][];

        for(int i = 0; i < 4; i++) {
            int row = shape[0].length;
            int col = shape.length;

            shapes[i] = new int[row][col];

            for(int j = 0; j < row; j++) {
                for(int k = 0; k < col; k++) {
                    shapes[i][j][k] = shape[col - k - 1][j];
                }
            }

            shape = shapes[i];
        }
        shape = shapes[1]; // Added this line to prevent rotate not working during first press
    }

}
