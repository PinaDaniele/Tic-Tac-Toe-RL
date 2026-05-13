package TicTacToeRL;

import java.util.HashMap;
import java.util.Set;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class QTable implements FileWritable {
    private static final String SAVE_FOLDER = "./qtables/";
    private static final Path folderPath = Path.of(SAVE_FOLDER);

    private HashMap<String, double[]> table;

    public QTable(){
        table = new HashMap<>();
    }

    public QTable(String fileName) throws FileNotFoundException{
        table = new HashMap<>();
        loadFromFile(fileName);
    }

    public Set<String> getStates(){
        return table.keySet();
    }

    public double getQValue(String state, int action){
        return getQValues(state)[action];
    }

    public double[] getQValues(String state){
        return table.computeIfAbsent(state, k -> new double[Board.SIZE * Board.SIZE]);
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
        try{
            Files.createDirectories(folderPath);
        }
        catch(IOException e){
            System.err.printf("An error occured while creating the save QTable folder: %s%n", e.getMessage());
        }

        Path filePath = folderPath.resolve(fileName);

        try(PrintWriter writer = new PrintWriter(filePath.toFile())){
            for(String state: table.keySet()){
                double[] QValues = table.get(state);
                StringBuilder line = new StringBuilder(state);

                for(double QValue: QValues){
                    line.append(",").append(QValue);
                }
                writer.println(line.toString());
            }
        }
        catch(IOException e){
            System.err.printf("An error occured while saving the QTable: %s%n", e.getMessage());
        }

    }

    @Override
    public void loadFromFile(String fileName) throws FileNotFoundException{
        table.clear();
        FileReader file;
        try{
            Path filePath = folderPath.resolve(fileName);
            file = new FileReader(filePath.toFile());
        }
        catch (FileNotFoundException e){
            throw new FileNotFoundException("File not found when loading QTable:\n" + e.getMessage());
        }
        

        try(BufferedReader reader = new BufferedReader(file)){
            String line = reader.readLine();

            while(line != null){
                String[] parts = line.split(",");
                String state = parts[0];
                double[] QValues = new double[Board.SIZE * Board.SIZE];

                for(int i=0; i<Board.SIZE * Board.SIZE; i++){
                    QValues[i] = Double.parseDouble(parts[i+1]);
                }

                table.put(state, QValues);
                line = reader.readLine();
            }

        }
        catch(IOException e){
            System.err.printf("An error occured while loading the QTable (loading an empty one): %s%n", e.getMessage());
            table = new HashMap<>();
        }

    }

}
