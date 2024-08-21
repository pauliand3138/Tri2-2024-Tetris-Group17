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
    public void moveDownNaturally() {
        y+=0.05;
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

        if (blockInfo.getNumber() == 7) {
            if (blockInfo.getColumns() == 1) {
                this.setX(this.getX() + 1);
                this.setY((int)this.getY() - 1);
            } else {
                this.setX(this.getX() - 1);
                this.setY((int)this.getY() + 1);
            }
        }
    }
}
