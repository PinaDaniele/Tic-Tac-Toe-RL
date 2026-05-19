package CLI;

public final class CliUtils {

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
