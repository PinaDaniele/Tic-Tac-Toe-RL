package TicTacToeRL;

public final class GameVars {
    public enum Mark {
        O, X, EMPTY
    }

    public enum GameState {
        IN_PROGRESS, DRAW, WIN, INVALID;
    }

    public record HyperParameters(
    double alpha,
    double gamma,
    double epsilon,
    double epsilonDecay,
    double minEpsilon){}

    public record StepResult(
        String state,
        double reward, 
        boolean done    
    ){}

    public record PreviousMove(
        String state,
        int action,
        double reward
    ){}

}
