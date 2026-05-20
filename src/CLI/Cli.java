package CLI;

import CLI.CliConstants.TextStyles;
import CLI.CliConstants.TextColors;

import java.util.Random;
import java.util.Scanner;
import java.lang.Thread;

import TicTacToeRL.Board;
import TicTacToeRL.QAgent;
import TicTacToeRL.QTable;
import TicTacToeRL.Trainer;
import TicTacToeRL.Mark;

public class Cli {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static QAgent.HyperParameters parameters = new QAgent.HyperParameters(0.5, 0.9, 1.0, 0.99999, 0.05);
    public static void main(String[] args) {
        showMainMenu();
    }

    private static void showMainMenu(){
        Menu mainMenu = new Menu("===== MAIN MENU =====");
        mainMenu.addOption("Train", () -> showTrainMenu());
        mainMenu.addOption("Play", () -> play());
        mainMenu.ask();
    }

    private static void showTrainMenu(){
        Menu trainMenu = new Menu("===== TRAIN MENU =====");

        trainMenu.addOption("Tune",  () -> {
            parameters = setParamters();
        });

        trainMenu.addOption("Train", () -> {
            startTaining();
        });

        trainMenu.ask();
    }

    private static void startTaining(){
        System.out.printf("%nInsert the number of epochs: ");
        int epochs = Integer.parseInt(scanner.nextLine());
        
        boolean loadFromFile = CliUtils.askBoolean(scanner, "Do you want to load an existing QTable?");
        String fileName = null;
        if (loadFromFile){
            fileName = CliUtils.askPath(scanner, QTable.SAVE_FOLDER);
        }

        Trainer trainer = null;
        if(fileName == null){
            trainer = new Trainer(epochs, parameters);
        }
        else{
            trainer = new Trainer(epochs, parameters, fileName);
        }
        
        trainer.trainLoop();

        System.out.printf("%n%s%sTraining complete press enter to continue...%s", TextColors.BRIGHT_YELLOW, TextStyles.UNDERLINE, TextColors.RESET);
        scanner.nextLine();
    }

    private static QAgent.HyperParameters setParamters(){
        CliUtils.clearScreen();

        System.out.printf("%n%sBellman Equation:%s Q(s,a) = Q(s,a) + α(R + y max(Q'(s,a)) - Q(s,a))%n", TextStyles.BOLD, TextColors.RESET);
        
        System.out.printf("%nSet alpha (learning rate - how much new info affects old info | suggested [0.5 - 0.7]): ");
        double alpha = Double.parseDouble(scanner.nextLine());

        System.out.printf("%nSet gamma (discount factor - how much future reward affect current | suggested [0.9]): ");
        double gamma = Double.parseDouble(scanner.nextLine());

        System.out.printf("%nSet epsilon (random action chance | suggested [1.0]): ");
        double epsilon = Double.parseDouble(scanner.nextLine());

        System.out.printf("%nSet epsilon decay (how much epsilon is lowered each epoch | suggested [0.99999]): ");
        double epsilonDecay = Double.parseDouble(scanner.nextLine());

        System.out.printf("%nSet epsilon minimum ( | suggested [0.0.5 | 0.01]): ");
        double epsilonMin = Double.parseDouble(scanner.nextLine());

        QAgent.HyperParameters parameters = new QAgent.HyperParameters(alpha, gamma, epsilon, epsilonDecay, epsilonMin);
        return parameters;
    }


    private static void play(){
        CliUtils.clearScreen();

        System.out.printf("%n%sPlease load a model %s(or load empty to go back):", CliConstants.TextStyles.BOLD, CliConstants.TextColors.RESET);
        String fileName = CliUtils.askPath(scanner, QTable.SAVE_FOLDER);
        if (fileName == null){
            return;
        }

        System.out.printf("%n%sPress enter to start the game...%s", CliConstants.TextColors.BRIGHT_GREEN, CliConstants.TextColors.RESET);
        scanner.nextLine();

        CliUtils.clearScreen();
        Board gameBoard = new Board();

        Mark player = (random.nextInt(2) == 0) ? Mark.X : Mark.O;
        Mark aiMark = (player == Mark.X) ? Mark.O : Mark.X;
        QAgent aiOpponent = new QAgent(aiMark, fileName, parameters);

        boolean done = false;
        Mark currentPlayer = (random.nextInt(2) == 0) ? Mark.X : Mark.O;
        String cliBoard = cliFromatBoard(gameBoard.getMatrix());
        while(!done){
            CliUtils.clearScreen();
            cliBoard = cliFromatBoard(gameBoard.getMatrix());
            System.out.printf("%n%s%n", cliBoard);

            if(currentPlayer == player){
                playerTurn(gameBoard, player);
            }
            else{
                aiTurn(aiOpponent, gameBoard);
            }
            
            CliUtils.clearScreen();
            cliBoard = cliFromatBoard(gameBoard.getMatrix());
            System.out.printf("%n%s%n", cliBoard);

            if (gameBoard.getGameState() == Board.GameState.WIN){
                System.out.printf("%n%s%sPlayer %s Wins!%s%nPress Enter to continue", CliConstants.TextStyles.BOLD, CliConstants.TextColors.BRIGHT_YELLOW, currentPlayer, CliConstants.TextColors.RESET);
                done = true;
                scanner.nextLine();
            }
            else{
                currentPlayer = (currentPlayer==player) ? aiOpponent.getMark() : player;
            }
        }
        

    }

    private static void aiTurn(QAgent ai, Board gameBoard){
        int aiMove = ai.makeAction(gameBoard.getBoardStateString(), gameBoard.getAvailableMoves());
        int row = aiMove / Board.SIZE;
        int column = aiMove % Board.SIZE;

        System.out.printf("%nAi is thinking");
        for(int i=0; i<3; i++){
            try{
                Thread.sleep(random.nextInt(1000));
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            System.out.printf(".");
            System.out.flush();
            
        }

        gameBoard.makeMove(row, column, ai.getMark());
    }

    private static void playerTurn(Board gameBoard, Mark mark){
        while(true){
            try{
                System.out.printf("%nInsert row: ");
                int row = Integer.parseInt(scanner.nextLine());
                System.out.printf("Inert column: ");
                int column = Integer.parseInt(scanner.nextLine());

                try{
                    if (gameBoard.getMatrix()[row][column] == Mark.EMPTY){
                        gameBoard.makeMove(row, column, mark);
                        break;
                    }
                    else{
                        CliUtils.incorrectInput(scanner);
                    }
                }
                catch (IndexOutOfBoundsException e){
                    CliUtils.incorrectInput(scanner);
                }
            }
            catch (NumberFormatException e){
                System.err.printf("%n%sParse error - Please insert an integer%s", CliConstants.TextColors.RED, CliConstants.TextColors.RESET);
            }
        }
        
    }

    private static String cliFromatBoard(Mark[][] matrix){
        StringBuilder cliMatrix = new StringBuilder();

        for(int i=0; i<Board.SIZE; i++){
            cliMatrix.append("|");
            for(int j=0; j<Board.SIZE; j++){
                switch (matrix[i][j]) {
                    case Mark.X:
                        cliMatrix.append(CliConstants.TextColors.BLUE + Mark.X + CliConstants.TextColors.RESET);
                        break;
                    case Mark.O:
                        cliMatrix.append(CliConstants.TextColors.RED + Mark.O + CliConstants.TextColors.RESET);
                        break;
                    default:
                        cliMatrix.append(" ");
                        break;
                }
                cliMatrix.append("|");
            }
            cliMatrix.append("\n---\n");
        }
        return cliMatrix.toString();
    }
}
