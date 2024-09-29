//package facade;
//
//import model.GameInfo;
//import view.panel.GameInfoPanel;
//
//public class GameFacade {
//    private GameInfoPanel gameInfoPanel;
//    private GameInfo gameInfo;
//
//    public GameFacade(int playerNum, int playerType) {
//        this.gameInfo = new GameInfo(playerNum, playerType);
//        this.gameInfoPanel = new GameInfoPanel(gameInfo);
//    }
//
//    public void clearLines() {
//        gameInfoPanel.clearLines();
//    }
//
//    public void updateGameInfo() {
//        gameInfoPanel.updateGameInfo();
//    }
//
//    public GameInfoPanel getGameInfoPanel() {
//        return gameInfoPanel;
//    }
//
//    public GameInfo getGameInfo() {
//        return gameInfo;
//    }
//}
//
