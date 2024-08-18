public class Block {
    private BlockInfo blockInfo;
    private int x;
    private double y;
    public Block(BlockInfo blockInfo, int x, int y){
        this.blockInfo = blockInfo;
        this.x = x;
        this.y = y;
    }
    public BlockInfo getBlockInfo(){ return blockInfo;}
    public int getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void moveDown() {
        y+=0.05;
    }
    public void moveLeft() {
        x--;
    }
    public void moveRight() {
        x++;
    }
}
