package CLI;

import CLI.CliConstants.TextStyles;

import java.util.Scanner;

import CLI.CliConstants.TextColors;
import TicTacToeRL.QAgent;

public class Cli {
    public static void main(String[] args) {
        showMainMenu();
    }

    public static void showMainMenu(){
        Menu mainMenu = new Menu("===== MAIN MENU =====");
        mainMenu.addOption("Train", () -> showTrainMenu());
        mainMenu.addOption("Play", () -> System.out.println("Not implemented yet"));
        mainMenu.ask();
    }

    public static void showTrainMenu(){
        QAgent.HyperParameters parameters = new QAgent.HyperParameters(0.5, 0.9, 1.0, 0.99999, 0.05);
        Menu trainMenu = new Menu("===== TRAIN MENU =====");
        trainMenu.addOption("Tune",  () -> setParamters(parameters));
        trainMenu.ask();
    }


    public static void setParamters(QAgent.HyperParameters parameters){
        CliUtils.clearScreen();
        Scanner scanner = new Scanner(System.in);

        System.out.printf("%sBellman Equation:%s Q(s,a) = Q(s,a) + α(R + y max(Q'(s,a)) - Q(s,a))%n", TextStyles.BOLD, TextColors.RESET);
        
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

        parameters = new QAgent.HyperParameters(alpha, gamma, epsilon, epsilonDecay, epsilonMin);
        scanner.close();
        showTrainMenu();
        
    }
}
