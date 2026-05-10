package TicTacToeRL;

import TicTacToeRL.GameVars.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.io.FileNotFoundException;

public class Trainer {

    private Environment env;
    private QAgent Xagent;
    private QAgent Oagent;

    private final int EPOCHS;
    public static int trainerCounter = 0;
    private final int trainerID;

    public Trainer(int epochs, HyperParameters parameters){
        EPOCHS = epochs;
        env = new Environment();
        Xagent = new QAgent(Mark.X, parameters);
        Oagent = new QAgent(Mark.O, parameters);
        trainerID = trainerCounter;
        trainerCounter++;
    }

    public Trainer(int epochs,HyperParameters parameters, String filePath) throws FileNotFoundException{
        EPOCHS = epochs;
        env = new Environment();
        Xagent = new QAgent(Mark.X, filePath, parameters);
        Oagent = new QAgent(Mark.O, filePath, parameters);
        trainerID = trainerCounter;
        trainerCounter++;
    }

    private QAgent getCurrentAgent(){
        if(env.getCurrentPlayer() == Mark.X){
            return Xagent;
        }
        else{
            return Oagent;
        }
    }

    private QTable mergeTables(){
        QTable mergedTable = new QTable();

        Set<String> allStates = new HashSet<>();
        allStates.addAll(Xagent.getQTable().getStates());
        allStates.addAll(Oagent.getQTable().getStates());

        for (String state: allStates){
            double[] XQValues = Xagent.getQTable().getQValues(state);
            double[] OQValues = Oagent.getQTable().getQValues(state);
            double[] mergedQValue = mergedTable.getQValues(state);

            for (int i = 0; i < (Board.SIZE * Board.SIZE); i++){
                if (XQValues[i] != 0.0 && OQValues[i] != 0.0){
                    mergedQValue[i] = (XQValues[i] + OQValues[i]) / 2.0;
                }
                else if (XQValues[i] != 0.0) {
                    mergedQValue[i] = XQValues[i];
                }
                else{
                    mergedQValue[i] = OQValues[i]; 
                }
                
            }
        }
        
        return mergedTable;
    }

    public void train(){
        for (int epoch = 0; epoch < EPOCHS; epoch++){
            env.resetGame();
            boolean done = false;

            PreviousMove previousMoveX = null;
            PreviousMove previousMoveO = null;

            int modifiedCounter = 0;
            Mark winner = Mark.EMPTY;


            while (!done){
                QAgent currentAgent = getCurrentAgent();
                String currentState = env.getBoard().getBoardStateString();
                List<Integer> availableMoves = env.getBoard().getAvailableMoves();

                int actionIndex = currentAgent.makeAction(currentState, availableMoves);

                Environment.StepResult moveResult = env.step(actionIndex);

                if (moveResult.done()){
                    done = true;
                    currentAgent.learn(currentState, actionIndex, moveResult.reward(), moveResult.state(), true);
                    modifiedCounter++;

                    double opponentReward = (moveResult.reward() == Environment.WIN_REWARD) ? Environment.LOSE_REWARD : Environment.DRAW_REWARD;

                    if (currentAgent == Xagent){
                        Oagent.learn(previousMoveO.state(), previousMoveO.action(), opponentReward, moveResult.state(), true);
                        modifiedCounter++;
                    }
                    else{
                        Xagent.learn(previousMoveX.state(), previousMoveX.action(), opponentReward, moveResult.state(), true);
                        modifiedCounter++;
                    }

                    if (moveResult.reward() == Environment.WIN_REWARD){
                        winner = currentAgent.getMark();
                    }
                    else if (moveResult.reward() == Environment.DRAW_REWARD){
                        winner = Mark.EMPTY;
                    }
                }
                else{
                    if (currentAgent == Xagent){

                        if (previousMoveO != null){
                            Oagent.learn(previousMoveO.state(), previousMoveO.action(), previousMoveO.reward(), moveResult.state(), moveResult.done());
                            modifiedCounter++;
                        }

                        previousMoveX = new PreviousMove(currentState, actionIndex, moveResult.reward());

                    }
                    else{
                        if (previousMoveX != null){
                            Xagent.learn(previousMoveX.state(), previousMoveX.action(), previousMoveX.reward(), moveResult.state(), moveResult.done());
                            modifiedCounter++;
                        }
                        previousMoveO = new PreviousMove(currentState, actionIndex, moveResult.reward());
                    }
                }
            }

            Xagent.decayEpsilon();
            Oagent.decayEpsilon();

            System.out.println("Epoch " + (epoch + 1) + "/" + EPOCHS + " completed. Modified actions: " + modifiedCounter + "| Winner: " + winner.toString());

        }
        QTable trainedAgentTable = mergeTables();
        trainedAgentTable.saveToFile("QTable_tid_" + trainerID + "_epochs_" + EPOCHS + ".csv");

    }


}
