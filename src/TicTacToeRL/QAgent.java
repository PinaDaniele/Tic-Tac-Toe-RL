package TicTacToeRL;

import java.util.Random;
import java.util.List;

public class QAgent {
    public record HyperParameters(
        double alpha,
        double gamma,
        double epsilon,
        double epsilonDecay,
        double minEpsilon){}

    private QTable agentQTable;
    private Mark agentMark;

    private final HyperParameters agentParameters;
    private double currentEpsilon;

    private final Random random;

    QAgent(Mark mark, HyperParameters parameters){
        random = new Random();
        agentMark = mark;
        agentQTable = new QTable();
        agentParameters = parameters;
        currentEpsilon = parameters.epsilon();
    }

    QAgent(Mark mark, String filePath, HyperParameters parameters){
        random = new Random();
        agentMark = mark;
        agentQTable = new QTable(filePath);
        agentParameters = parameters;
        currentEpsilon = parameters.epsilon();
    }
    
    public int makeAction(String state, List<Integer> availableActions){
        if (random.nextDouble() < currentEpsilon){
            int randomIndex = random.nextInt(availableActions.size());
            return availableActions.get(randomIndex);
        }

        int bestAction = -1;
        double maxQValue = Double.NEGATIVE_INFINITY;

        for(int action: availableActions){
            double QValue = agentQTable.getQValue(state, action);

            if (QValue > maxQValue || (QValue == maxQValue && random.nextBoolean())){
                maxQValue = QValue;
                bestAction = action;
            }
        }
        return bestAction;
    }

    public void learn(String state, int action, double reward, String nextState, boolean done){
        double currentQValue = agentQTable.getQValue(state, action);
        double maxFutureQValue = 0.0;

        if(!done){
            maxFutureQValue = agentQTable.getMaxQValue(nextState);
        }

        double newQValue = currentQValue + agentParameters.alpha() * (reward + agentParameters.gamma() * maxFutureQValue - currentQValue);
        agentQTable.updateQValue(state, action, newQValue);
    }

    public void decayEpsilon(){
        currentEpsilon *= agentParameters.epsilonDecay();
    }

    public Mark getMark(){
        return agentMark;
    }

    public QTable getQTable(){
        return agentQTable;
    }


}
