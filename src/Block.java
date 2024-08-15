import java.awt.*;

public class Block {
    private int number;
    private int [][] shape;
    private Color colour;

    public Block(int [][] shape, Color colour, int number){
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
}
