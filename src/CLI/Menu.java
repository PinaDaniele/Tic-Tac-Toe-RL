package CLI;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Runnable;

import CLI.CliConstants.TextColors;
import CLI.CliConstants.TextStyles;


public class Menu {
    
    private String header;
    private String headerColor = TextColors.WHITE;
    private String optionsColor = TextColors.WHITE;
    private String inputColor = TextColors.WHITE;
    private ArrayList<MenuOption> optionsList;

    private final Scanner scanner = new Scanner(System.in);
    private boolean running;
    private boolean addedExit;

    public Menu(String header){
        this.header = header;
        optionsList = new ArrayList<>();
    }

    public Menu(String header, String headerColor, String optionsColor, String inputColor){
        this.header = header;
        this.headerColor = headerColor;
        this.optionsColor = optionsColor;
        this.inputColor = inputColor;
        optionsList = new ArrayList<>();
    }

    public void addOption(String key, Runnable action){
        optionsList.add(new MenuOption(key, action));
    }

    private void addExitOption(){
        optionsList.add(new MenuOption("Exit", () -> running=false));
        addedExit = true;
    }

    private void printOptions(){
        for (MenuOption option : optionsList){
            System.out.printf("%s  -%s%s%n", optionsColor ,option.getKey(), TextColors.RESET);
        }
    }

    private Runnable checkInput(String key){
        for(MenuOption option: optionsList){
            if (key.toLowerCase().equals(option.getKey().toLowerCase())){
                return option.getAction();
            }
        }
        return null;
    }

    public void ask(){
        running = true;
        if(!addedExit){
            addExitOption();
        }
        

        while(running){
            CliUtils.clearScreen();
            System.out.printf("%s%s%s%s%n", headerColor, TextStyles.BOLD, header, TextColors.RESET);
            printOptions();
            
            System.out.printf("%sInsert one of the command above: ", inputColor);
            String key = scanner.nextLine();
            Runnable selectedAction = checkInput(key);

            if (selectedAction == null){
                CliUtils.incorrectInput(scanner);
            }
            else{
                selectedAction.run();
            }
        }
    }


}
