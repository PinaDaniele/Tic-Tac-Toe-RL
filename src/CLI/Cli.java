package CLI;

import CLI.CliConstants.TextStyles;
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
        trainMenu.addOption("Tune", () -> setParamters(parameters));
        trainMenu.ask();
    }

    public static void setParamters(QAgent.HyperParameters parameters){
        CliUtils.clearScreen();
        System.out.printf("%sBellman Equation:%s Q(s,a) = Q(s,a) + α(R + y max(Q'(s,a)) - Q(s,a))%n", TextStyles.BOLD, TextColors.RESET);
        
    }
}
