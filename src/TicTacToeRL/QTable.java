package TicTacToeRL;

import java.util.HashMap;
import java.util.Set;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class QTable implements FileWritable {
    private HashMap<String, double[]> table;
    private final int numActions = Board.SIZE * Board.SIZE;

    public static final String SAVE_FOLDER = "./qtables/";
    private final Path folderPath = Path.of(SAVE_FOLDER);

    public QTable(){
        table = new HashMap<>();
    }

    public QTable(String fileName){
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

    public Set<String> getStates(){
        return table.keySet();
    }

    @Override
    public void saveToFile(String fileName){
        try{
            Files.createDirectories(folderPath);
        }
        catch(IOException e){
            System.err.println("An error occured while creating the save QTable folder: " + e.getMessage());
        }

        Path filePath = folderPath.resolve(fileName);

        try(PrintWriter writer = new PrintWriter(filePath.toFile())){
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
            Path filePath = folderPath.resolve(fileName);
            file = new FileReader(filePath.toFile());
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
