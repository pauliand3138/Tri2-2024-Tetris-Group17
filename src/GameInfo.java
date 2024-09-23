public class GameInfo {
    private int score;
    private int initLevel;
    private int currLevel;
    private int lineErased;
    private int playerNum;
    private int playerType;
    private Block nextBlock;

    public GameInfo(int playerNum, int playerType) {
        this.playerNum = playerNum;
        this.playerType = playerType;
        this.score = 0;
        this.initLevel = Common.gameConfig.getGameLevel();
        this.currLevel = initLevel;
        this.lineErased = 0;
        this.nextBlock = null;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getInitLevel() {
        return initLevel;
    }

    public void setInitLevel(int initLevel) {
        this.initLevel = initLevel;
    }

    public int getCurrLevel() {
        return currLevel;
    }

    public void setCurrLevel(int currLevel) {
        this.currLevel = currLevel;
    }

    public int getLineErased() {
        return lineErased;
    }

    public void setLineErased(int lineErased) {
        this.lineErased = lineErased;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }
}
