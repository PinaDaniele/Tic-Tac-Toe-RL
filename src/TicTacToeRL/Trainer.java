package TicTacToeRL;

import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.FileNotFoundException;

public class Trainer {
    private record PreviousMove(
        String state,
        int action,
        double reward
    ){}

    private final static int BATCH_PERCENTAGE = 10;

    private final Environment env;
    private final QAgent xAgent;
    private final QAgent oAgent;
    private final int epochs;

    public Trainer(int epochs, QAgent.HyperParameters parameters){
        this.epochs = epochs;
        this.env = new Environment();
        this.xAgent = new QAgent(Mark.X, parameters);
        this.oAgent = new QAgent(Mark.O, parameters);

    }

    public Trainer(int epochs,QAgent.HyperParameters parameters, String filePath) throws FileNotFoundException{
        this.epochs = epochs;
        this.env = new Environment();
        this.xAgent = new QAgent(Mark.X, filePath, parameters);
        this.oAgent = new QAgent(Mark.O, filePath, parameters);
    }

    public void train(){
        int batchDelimiter = epochs * BATCH_PERCENTAGE / 100;
        HashMap<String, Integer> batchStats = new HashMap<>(Map.of(
            "xWins", 0,
            "oWins", 0,
            "draws", 0,
            "modified", 0
        ));

        for (int epoch = 0; epoch < epochs; epoch++){
            env.resetGame();
            boolean done = false;

            PreviousMove lastMoveX = null;
            PreviousMove lastMoveO = null;

            while (!done){

                boolean isXTurn = (env.getCurrentPlayer() == Mark.X);
                QAgent activeAgent = (isXTurn) ? xAgent : oAgent;
                QAgent waitingAgent = (isXTurn) ? oAgent : xAgent;
                PreviousMove lastMove = (isXTurn) ? lastMoveO : lastMoveX;
                
                String currentState = env.getBoard().getBoardStateString();
                List<Integer> availableMoves = env.getBoard().getAvailableMoves();

                int actionIndex = activeAgent.makeAction(currentState, availableMoves);
                Environment.StepResult moveResult = env.step(actionIndex);

                if (moveResult.done()){
                    done = true;

                    activeAgent.learn(currentState, actionIndex, moveResult.reward(), moveResult.state(), moveResult.done());
                    incrementHashMapValue(batchStats, "modified");

                    if (lastMove != null){
                        double opponentReward = (moveResult.reward() == Environment.WIN_REWARD) ? Environment.LOSE_REWARD : Environment.DRAW_REWARD;
                        waitingAgent.learn(lastMove.state(), lastMove.action(), opponentReward, currentState, moveResult.done());
                        incrementHashMapValue(batchStats, "modified");
                    }

                    if (moveResult.reward() == Environment.WIN_REWARD){
                        String key = String.format("%sWins", activeAgent.getMark().toString().toLowerCase());
                        incrementHashMapValue(batchStats, key);
                    }
                    else{
                        incrementHashMapValue(batchStats, "draws");
                    }
                    
                }
                else{
                    
                    if (lastMove != null){
                        waitingAgent.learn(lastMove.state(), lastMove.action(), lastMove.reward(), currentState, moveResult.done());
                        incrementHashMapValue(batchStats, "modified");
                    }

                    PreviousMove currentMove = new PreviousMove(currentState, actionIndex, moveResult.reward());
                    if(isXTurn){
                        lastMoveX = currentMove;
                    }
                    else{
                        lastMoveO = currentMove;
                    }
                }
            }

            xAgent.decayEpsilon();
            oAgent.decayEpsilon();

            if ((epoch+1) % batchDelimiter == 0){
                printBatchStats(batchStats, batchDelimiter, epoch);
                resetBatch(batchStats);
            }

        }
        saveTrainedTable();
    }

    private void resetBatch(Map<String, Integer> m){
        for (String key: m.keySet()){
            m.put(key, 0);
        }
    }

    private void printBatchStats(Map<String, Integer> m, int batch, int epoch){
        double xWinPercentage = (double) m.get("xWins") / batch * 100;
        double oWinPercentage = (double) m.get("oWins") / batch * 100;
        double drawPercentage = (double) m.get("draws") / batch * 100;
        int totalMoves = m.get("modified");

        System.out.printf("Epoch [%d/%d]: x_wins %.2f | o_wins %.2f | draws %.2f | total moves updated %d%n", epoch+1, epochs, xWinPercentage, oWinPercentage, drawPercentage, totalMoves);
    }

    private void incrementHashMapValue(Map<String, Integer> m, String key){
        int val = m.get(key);
        m.put(key, ++val);
    }

    private void saveTrainedTable(){
        QTable trainedAgentTable = mergeTables();
        String filename = String.format("QTable_Size_%d_epochs_%d.csv", Board.SIZE, epochs);
        trainedAgentTable.saveToFile(filename);
    }

    private QTable mergeTables(){
        QTable mergedTable = new QTable();

        Set<String> allStates = new HashSet<>();
        allStates.addAll(xAgent.getQTable().getStates());
        allStates.addAll(oAgent.getQTable().getStates());
        int actionNum = Board.SIZE * Board.SIZE;

        for (String state: allStates){
            double[] xQValues = xAgent.getQTable().getQValues(state);
            double[] oQValues = oAgent.getQTable().getQValues(state);
            double[] mergedQValue = mergedTable.getQValues(state);

            for (int i = 0; i < actionNum; i++){
                if (xQValues[i] != 0.0 && oQValues[i] != 0.0){
                    mergedQValue[i] = (xQValues[i] + oQValues[i]) / 2.0;
                }
                else{
                    mergedQValue[i] = (xQValues[i] != 0) ? xQValues[i] : oQValues[i];
                }
                
            }
        }
        return mergedTable;
    }


}
