import java.util.Random;

public class Environment {
    private Board board;
    private Mark currentPlayer;
    private static Random randomGenerator = new Random();

    private record StepResult(String state, double reward, boolean done){}


    public Environment(){
        resetGame();
    }

    public void resetGame(){
        board = new Board();
        int randomPlayer = randomGenerator.nextInt(2);

        if (randomPlayer == 0){
            currentPlayer = Mark.X;
        }
        else{
            currentPlayer = Mark.O;
        }
    }

    private double getReward(){
        if (board.getGameState() == GameState.WIN){
            return 1.0;
        }
        else if (board.getGameState() == GameState.DRAW){
            return 0.5;
        }
        return 0.0;
    }

    private void swapPlayer(){
        if (currentPlayer == Mark.X){
            currentPlayer = Mark.O;
        }
        else{
            currentPlayer = Mark.X;
        }
    }

    public StepResult step(int row, int col){
        boolean moveSuccessful = board.makeMove(row, col, currentPlayer);

        if(!moveSuccessful){
            return new StepResult(board.getBoardState(), -10.0, true);
        }

        double reward = getReward();
        boolean done = reward > 0.0;

        if (!done){
            swapPlayer();
        }

        return new StepResult(board.getBoardState(), reward, done);
    }
}
