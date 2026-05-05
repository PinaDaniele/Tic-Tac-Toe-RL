package TicTacToeRL;
public final class GameVars {
    public enum Mark {
        O, X, EMPTY
    }

    public enum GameState {
        IN_PROGRESS, DRAW, WIN;
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
        boolean done){}

}
