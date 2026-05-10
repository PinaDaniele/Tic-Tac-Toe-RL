package TicTacToeRL;

import java.util.ArrayList;
import java.util.List;
import TicTacToeRL.GameVars.Mark;

public class Board{
        

    public enum GameState {
        IN_PROGRESS, DRAW, WIN;
    }

    private Mark[][] boardMatrix;
    public static final int SIZE = 3;
    private int moveCount;

    public Board(){
        moveCount = 0;
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

    @Override
    public String toString(){
        StringBuilder stringBoard = new StringBuilder();
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                stringBoard.append(boardMatrix[i][j].toString() + "");
            }
            stringBoard.append("\n");
        }
        return stringBoard.toString();
    }

    public String getBoardStateString(){
        StringBuilder state = new StringBuilder();
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                state.append(boardMatrix[i][j].toString());
            }
        }
        return state.toString();
    }

    public List<Integer> getAvailableMoves(){
        List<Integer> availableMoves = new ArrayList<>();
        for (int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){
                if(boardMatrix[i][j] == Mark.EMPTY){
                    availableMoves.add(i * SIZE + j);
                }
            }
        }
        return availableMoves;
    }

    public boolean makeMove(int row, int col, Mark mark){
        if(boardMatrix[row][col] == Mark.EMPTY){
            boardMatrix[row][col] = mark;
            moveCount++;
            return true;
        }
        else{
            return false;
        }
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

        if (moveCount == SIZE * SIZE){
            return GameState.DRAW;
        }

        return GameState.IN_PROGRESS;
    }

    // --- Helper methods fo checking winning conditions ---
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

}