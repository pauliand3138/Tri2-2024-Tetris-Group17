public class TetrisThread extends Thread{
    private Board board;

    public TetrisThread(Board board){
        this.board = board;
    }
    public void run() {
    // No usage yet, replaced Thread usage to Timer according to lecture

//        while(true){
//            try {
//                board.dropBlock();
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }

    }
}
