public class App {
    public static void main(String[] args) throws Exception {
        Board testBoard = new Board();
        
        System.out.println(testBoard.makeMove(0, 0, Mark.X));
        System.out.println(testBoard.makeMove(0, 0, Mark.X));
        System.out.println(testBoard.toString());
    }
}
