package TicTacToeRL;

import java.io.FileNotFoundException;

public interface FileWritable {

    public abstract void saveToFile(String fileName);

    public abstract void loadFromFile(String fileName) throws FileNotFoundException;
}
