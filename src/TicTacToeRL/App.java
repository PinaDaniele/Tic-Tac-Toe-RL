package TicTacToeRL;

import TicTacToeRL.GameVars.HyperParameters;


public class App {
    public static void main(String[] args) throws Exception {
        
        HyperParameters parameters = new HyperParameters(0.1, 0.9, 0.1, 0.9995, 0.01);
        Trainer testTrainer = new Trainer(100, parameters);
        testTrainer.train();
    }
}
