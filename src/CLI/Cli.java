package CLI;

import CLI.CliConstants.TextStyles;
import CLI.CliConstants.TextColors;

import java.util.Scanner;
import TicTacToeRL.QAgent;
import TicTacToeRL.Trainer;

public class Cli {

    private static final Scanner scanner = new Scanner(System.in);

    private static QAgent.HyperParameters parameters = new QAgent.HyperParameters(0.5, 0.9, 1.0, 0.99999, 0.05);
    public static void main(String[] args) {
        showMainMenu();
    }

    private static void showMainMenu(){
        Menu mainMenu = new Menu("===== MAIN MENU =====");
        mainMenu.addOption("Train", () -> showTrainMenu());
        mainMenu.addOption("Play", () -> System.out.println("Not implemented yet"));
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
        

        Trainer trainer = new Trainer(epochs, parameters);
        trainer.trainLoop();

        scanner.nextLine();
        System.out.printf("%s%sTraining complete press enter to continue...%s", TextColors.BRIGHT_YELLOW, TextStyles.UNDERLINE, TextColors.RESET);
        scanner.nextLine();
    }

    private static QAgent.HyperParameters setParamters(){
        CliUtils.clearScreen();

        System.out.printf("%n%sBellman Equation:%s Q(s,a) = Q(s,a) + α(R + y max(Q'(s,a)) - Q(s,a))%n", TextStyles.BOLD, TextColors.RESET);
        
        System.out.printf("%nSet alpha (learning rate - how much new info affects old info | suggested [0.5 - 0.7]): ");
        double alpha = scanner.nextDouble();

        System.out.printf("%nSet gamma (discount factor - how much future reward affect current | suggested [0.9]): ");
        double gamma = scanner.nextDouble();

        System.out.printf("%nSet epsilon (random action chance | suggested [1.0]): ");
        double epsilon = scanner.nextDouble();

        System.out.printf("%nSet epsilon decay (how much epsilon is lowered each epoch | suggested [0.99999]): ");
        double epsilonDecay = scanner.nextDouble();

        System.out.printf("%nSet epsilon minimum ( | suggested [0.0.5 | 0.01]): ");
        double epsilonMin = scanner.nextDouble();
        
        if (scanner.hasNextLine()){
            scanner.nextLine();
        }

        QAgent.HyperParameters parameters = new QAgent.HyperParameters(alpha, gamma, epsilon, epsilonDecay, epsilonMin);
        return parameters;
    }
}
