package TicTacToeRL;

import java.util.Random;
import TicTacToeRL.GameVars.StepRecord;
import TicTacToeRL.GameVars.Mark;
import TicTacToeRL.GameVars.GameState;

public class Environment {
    private Board board;
    private Mark currentPlayer;
    private static Random randomGenerator = new Random();
    public int SIZE;


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
            return 1.0;
        }
        else if (board.getGameState() == GameState.DRAW){
            return 0.5;
        }
        return 0.0;
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

    public StepRecord step(int actionIndex){
        String prevState = board.getBoardState();
        int row = actionIndex / Board.SIZE;
        int col = actionIndex % Board.SIZE;
        boolean moveSuccessful = board.makeMove(row, col, currentPlayer);

        if(!moveSuccessful){
            return new StepRecord(prevState, actionIndex, -10.0, true);
        }

        double reward = getReward();
        boolean done = reward > 0.0;

        if (!done){
            swapPlayer();
        }

        return new StepRecord(prevState, actionIndex, reward, done);
    }

    public Board getBoard(){
        return board;
    }

    public Mark getCurrentPlayer(){
        return currentPlayer;
    }
}
