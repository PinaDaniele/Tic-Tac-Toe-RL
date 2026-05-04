import java.util.HashMap;

public class QTable {
    private HashMap<String, double[]> table;
    private static int numActions;

    public QTable(int actions){
        numActions = actions;
        table = new HashMap<>();
    }

    public double[] getQValues(String state){
        return table.computeIfAbsent(state, k -> new double[numActions]);
    }

    public double getQValue(String state, int action){
        return getQValues(state)[action];
    }

    public void updateQValue(String state, int action, double newValue){
        getQValues(state)[action] = newValue;
    }

    public double getMaxQValue(String state){
        double[] QValues = getQValues(state);
        double maxQValue = Double.NEGATIVE_INFINITY;

        for (double QValue: QValues){
            if(QValue > maxQValue){
                maxQValue = QValue;
            }
        }
        return maxQValue;
    }

}
