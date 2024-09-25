package model;

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
    public void setX(int x){this.x = x;}
    public void setY(double y){this.y = y;}
    public void setBlockInfo(BlockInfo blockInfo) {
        this.blockInfo = blockInfo;
    }
    public void moveDownNaturally(int level) {
        y+=(0.05 * level);
    } //Animation purpose
    public void moveDown() {
        y++;
    }
    public void moveLeft() {
        x--;
    }
    public void moveRight() {
        x++;
    }
    public void rotate() {
        blockInfo.setCurrentRotation(blockInfo.getCurrentRotation() + 1);
        if (blockInfo.getCurrentRotation() > 3) { blockInfo.setCurrentRotation(0); }
        blockInfo.setShape(blockInfo.getShapes()[blockInfo.getCurrentRotation()]);
    }

    public Block(Block block) {
        this.x = block.getX();
        this.y = block.getY();
        this.blockInfo = new BlockInfo(block.getBlockInfo().getShape(), block.getBlockInfo().getColour(), block.getBlockInfo().getNumber());
    }


}
