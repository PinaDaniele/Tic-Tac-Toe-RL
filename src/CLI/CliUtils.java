package CLI;

import CLI.CliConstants.TextColors;
import java.util.Scanner;
import java.io.File;

public final class CliUtils {

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void incorrectInput(Scanner scanner){
        scanner.nextLine();
        System.out.printf("%n%sIncorrect input, press enter to return...%s", TextColors.RED, TextColors.RESET);
        scanner.nextLine();
    }

    public static String askPath(Scanner scanner, String path){
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files.length == 0){
            return null;
        }

        for (int i=0; i<files.length; i++){
            System.out.printf("%n%d. %s", i, files[i].getName());
        }
        System.out.printf("%nSelect the index of the file you want to load (put non-existent index to load empty): ");
        int index = Integer.parseInt(scanner.nextLine());

        try{
            return files[index].getName();
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }

    }
}
