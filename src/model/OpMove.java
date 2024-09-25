package model;

public class OpMove{
    private int opX;
    private int opRotate;

    public OpMove(int opX, int opRotate){
        this.opX = opX;
        this.opRotate = opRotate;
    }

    public int getOpX() {
        return this.opX;
    }

    public int getOpRotate() {
        return this.opRotate;
    }
}
