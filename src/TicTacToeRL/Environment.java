package TicTacToeRL;

import java.util.Random;
import TicTacToeRL.GameVars.StepResult;
import TicTacToeRL.GameVars.Mark;
import TicTacToeRL.GameVars.GameState;

public class Environment {
    private Board board;
    private Mark currentPlayer;
    private static Random randomGenerator = new Random();

    public static final double INVALID_MOVE_PENALTY = -10;
    public static final double WIN_REWARD = 1;
    public static final double DRAW_REWARD = 0.5;
    public static final double IN_PROGRESS_REWARD = 0;
    public static final double LOSE_REWARD = -1;

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

    //gives out a reward depending on the game state
    private double getReward(){
        if (board.getGameState() == GameState.WIN){
            return WIN_REWARD;
        }
        else if (board.getGameState() == GameState.DRAW){
            return DRAW_REWARD;
        }
        return IN_PROGRESS_REWARD;
    }

    //changes turn
    private void swapPlayer(){
        if (currentPlayer == Mark.X){
            currentPlayer = Mark.O;
        }
        else{
            currentPlayer = Mark.X;
        }
    }

    public StepResult step(int actionIndex){
        int row = actionIndex / Board.SIZE;
        int col = actionIndex % Board.SIZE;
        boolean moveSuccessful = board.makeMove(row, col, currentPlayer);

        if(!moveSuccessful){
            return new StepResult(board.getBoardStateString(), INVALID_MOVE_PENALTY, true);
        }

        double reward = getReward();
        boolean done = reward > 0.0;

        if (!done){
            swapPlayer();
        }

        return new StepResult(board.getBoardStateString(), reward, done);
    }

    public Board getBoard(){
        return board;
    }

    public Mark getCurrentPlayer(){
        return currentPlayer;
    }
}
