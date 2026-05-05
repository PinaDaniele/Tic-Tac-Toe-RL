package TicTacToeRL;

import java.util.HashMap;
import java.io.*;

public class QTable implements FileWritable {
    private HashMap<String, double[]> table;
    private static int numActions;

    public QTable(int actions){
        numActions = actions;
        table = new HashMap<>();
    }

    public QTable(int actions, String fileName){
        numActions = actions;
        table = new HashMap<>();
        loadFromFile(fileName);
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

    @Override
    public void saveToFile(String fileName){
        File file = new File(fileName);

        try(PrintWriter writer = new PrintWriter(file)){
            for(String state: table.keySet()){
                double[] QValues = table.get(state);
                String line = state;

                for(double QValue: QValues){
                    line += "," + QValue;
                }
                writer.println(line);
            }
        }
        catch(IOException e){
            System.err.println("An error occured while saving the QTable: " + e.getMessage());
        }

    }

    @Override
    public void loadFromFile(String fileName){
        table.clear();
        FileReader file;
        try{
            file = new FileReader(fileName);
        }
        catch (FileNotFoundException e){
            System.err.println("File not found: " + e.getMessage());
            return;
        }
        

        try(BufferedReader reader = new BufferedReader(file)){
            String line = reader.readLine();

            while(line != null){
                String[] parts = line.split(",");
                String state = parts[0];
                double[] QValues = new double[numActions];

                for(int i=0; i<numActions; i++){
                    QValues[i] = Double.parseDouble(parts[i+1]);
                }

                table.put(state, QValues);
                line = reader.readLine();
            }

        }
        catch(IOException e){
            System.err.println("An error occured while loading the QTable: " + e.getMessage());
        }

    }

}
