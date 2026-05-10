package TicTacToeRL;

public final class GameVars {
    public enum Mark {
        O, X, EMPTY
    }

    public record HyperParameters(
    double alpha,
    double gamma,
    double epsilon,
    double epsilonDecay,
    double minEpsilon){}

    

    public record PreviousMove(
        String state,
        int action,
        double reward
    ){}

}
