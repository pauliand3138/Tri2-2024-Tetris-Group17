import java.awt.Color;

public class BlockInfo {
    private int number;
    private int [][] shape;
    private Color colour;

    public BlockInfo(int [][] shape, Color colour, int number){
        this.shape = shape;
        this.colour = colour;
        this.number = number;
    }
    public int[][] getShape(){
        return shape;
    }
    public Color getColour(){
        return colour;
    }
    public int getNumber(){
        return number;
    }
    public int getRows(){
        return shape.length;
    }
    public int getColumns(){
        return shape[0].length;
    }
}
