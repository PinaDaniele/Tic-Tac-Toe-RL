package TicTacToeRL;

public class App {
    public static void main(String[] args) throws Exception {
        
        QAgent.HyperParameters parameters = new QAgent.HyperParameters(0.1, 0.9, 0.1, 0.9995, 0.01);
        Trainer testTrainer = new Trainer(1000000, parameters);
        testTrainer.trainLoop();
    }
}
