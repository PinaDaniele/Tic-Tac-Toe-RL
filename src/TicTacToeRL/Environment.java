package TicTacToeRL;

import java.util.Random;

public class Environment {

    public record StepResult(
        String state,
        double reward, 
        boolean done    
    ){}

    public static final double INVALID_MOVE_PENALTY = -10;
    public static final double WIN_REWARD = 1;
    public static final double DRAW_REWARD = 0.5;
    public static final double IN_PROGRESS_REWARD = 0;
    public static final double LOSE_REWARD = -1;

    private Board board;
    private Mark currentPlayer;
    private final Random randomGenerator;

    public Environment(){
        randomGenerator = new Random();
        resetGame();
    }

    public void resetGame(){
        board = new Board();

        int randomPlayer = randomGenerator.nextInt(2);
        currentPlayer = (randomPlayer == 0) ? Mark.X : Mark.O;
    }

    public StepResult step(int actionIndex){
        int row = actionIndex / Board.SIZE;
        int col = actionIndex % Board.SIZE;
        boolean moveSuccessful = board.makeMove(row, col, currentPlayer);

        if(!moveSuccessful){
            return new StepResult(board.getBoardStateString(), INVALID_MOVE_PENALTY, true);
        }

        Board.GameState currentState = board.getGameState();
        double reward = getReward(currentState);
        boolean done = (currentState != Board.GameState.IN_PROGRESS);

        if (!done){
            currentPlayer = (currentPlayer == Mark.X) ? Mark.O : Mark.X;
        }

        return new StepResult(board.getBoardStateString(), reward, done);
    }

    private double getReward(Board.GameState state){
        switch (state) {
            case WIN:
                return WIN_REWARD;
            case DRAW:
                return DRAW_REWARD;
            default:
                return IN_PROGRESS_REWARD;
        }
    }

    public Board getBoard(){
        return board;
    }

    public Mark getCurrentPlayer(){
        return currentPlayer;
    }
}
