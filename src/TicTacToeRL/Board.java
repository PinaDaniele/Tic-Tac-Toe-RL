package TicTacToeRL;

import java.util.ArrayList;
import TicTacToeRL.GameVars.Mark;
import TicTacToeRL.GameVars.GameState;

public class Board{
    private Mark[][] boardMatrix;
    public static int SIZE = 3;

    Board(){
        setEmptyBoard();
    }

    //Resets the board
    private void setEmptyBoard(){
        boardMatrix = new Mark[SIZE][SIZE];
        for(int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                boardMatrix[i][j] = Mark.EMPTY;
            }
        }
    }

    //returns the board current state as a string
    public String getBoardState(){
        String state = "";
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                state += boardMatrix[i][j].toString();
            }
        }
        return state;
    }

    //Place a mark, returns false if the move is illegal
    public boolean makeMove(int row, int col, Mark mark){
        if(boardMatrix[row][col] == Mark.EMPTY){
            boardMatrix[row][col] = mark;
            return true;
        }
        else{
            return false;
        }
    }

    //Some functions to check if the player has won
    private boolean checkRow(int row){
        if (boardMatrix[row][0] != Mark.EMPTY){
            for (int i=1; i<SIZE; i++){
                if(boardMatrix[row][i]!= boardMatrix[row][0]){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkCol(int col){
        if (boardMatrix[0][col] != Mark.EMPTY){
            for (int i=1; i<SIZE; i++){
                if(boardMatrix[i][col] != boardMatrix[0][col]){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkDiag(){
        boolean LRDiag = true;
        boolean RLDiag = true;

        if (boardMatrix[0][0] != Mark.EMPTY){
            for (int i=1; i<SIZE; i++){
                if(boardMatrix[i][i] != boardMatrix[0][0]){
                    LRDiag = false;
                    break;
                }
            }
        }
        else{
            LRDiag = false;
        }

        if(boardMatrix[0][SIZE -1] != Mark.EMPTY){
            for(int i=1; i<SIZE; i++){
                if(boardMatrix[i][SIZE -1 -i] != boardMatrix[0][SIZE - 1]){
                    RLDiag = false;
                    break;
                }
            }
        }
        else{
            RLDiag = false;
        }

        return LRDiag || RLDiag;
    }

    private boolean isFull(){
        for(int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                if(boardMatrix[i][j] == Mark.EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    public GameState getGameState(){
        for (int i=0; i<SIZE; i++){
            if (checkRow(i)){
                return GameState.WIN;
            }
        }

        for (int i=0; i<SIZE; i++){
            if (checkCol(i)){
                return GameState.WIN;
            }
        }

        if (checkDiag()){
            return GameState.WIN;
        }

        if (isFull()){
            return GameState.DRAW;
        }

        return GameState.IN_PROGRESS;
    }

    //Returns a list of indexes, representing empty cells
    public ArrayList<Integer> getAvailableMoves(){
        ArrayList<Integer> availableMoves = new ArrayList<>();
        for (int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){
                if(boardMatrix[i][j] == Mark.EMPTY){
                    availableMoves.add(i * SIZE + j);
                }
            }
        }
        return availableMoves;
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