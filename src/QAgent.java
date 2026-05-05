import java.util.Random;
import java.util.ArrayList;

public class QAgent {
    private QTable agentQTable;
    private Mark agentMark;

    private HyperParameters agentParameters;
    private double currentEpsilon;

    private static Random random = new Random();

    QAgent(Mark mark, int actions, HyperParameters parameters){
        agentMark = mark;
        agentQTable = new QTable(actions);
        agentParameters = parameters;
        currentEpsilon = parameters.epsilon();
    }

    QAgent(Mark mark, int actions, String filePath, HyperParameters parameters){
        agentMark = mark;
        agentQTable = new QTable(actions, filePath);
        agentParameters = parameters;
        currentEpsilon = parameters.epsilon();
    }

    public int makeAction(String state, ArrayList<Integer> availableActions){
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
