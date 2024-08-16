public class Block {
    private BlockInfo blockInfo;
    private int x;
    private int y;
    public Block(BlockInfo blockInfo, int x, int y){
        this.blockInfo = blockInfo;
        this.x = x;
        this.y = y;
    }
    public BlockInfo getBlockInfo(){ return blockInfo;}
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
