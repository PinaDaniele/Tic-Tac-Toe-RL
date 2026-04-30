public class Board{
    private Mark[][] boardMatrix;
    public static final int SIZE = 3;

    Board(){
        setEmptyBoard();
    }

    private void setEmptyBoard(){
        boardMatrix = new Mark[SIZE][SIZE];
        for(int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                boardMatrix[i][j] = Mark.EMPTY;
            }
        }
    }

    public String getBoardState(){
        String state = "";
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                state += boardMatrix[i][j].toString();
            }
        }
        return state;
    }

    public boolean makeMove(int row, int col, Mark mark){
        if(boardMatrix[row][col] == Mark.EMPTY){
            boardMatrix[row][col] = mark;
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String toString(){
        String stringBoard = "";
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                stringBoard += boardMatrix[i][j].toString() + " ";
            }
            stringBoard += "\n";
        }
        return stringBoard;
    }

}