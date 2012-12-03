package mancala;

import java.util.ArrayList;
import javax.swing.event.*;

/**
 * the model of the Manacala program
 * holds all of the data and manages the game logic
 * @author Alex Souza
 */
public class Model {

    private static final int LENGTH = 7;
    private int[][] board;
    private int[][] boardSave;
    private int active;
    private int undos;
    private boolean canGo;
    private boolean canUndo;
    private boolean gameover;
    ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

    /**
     * creates a model and initializes the board;
     * @param startingPits the amount of pieces in each pit to start with
     */
    public Model(int startingPits) {
        board = new int[2][LENGTH];
        boardSave = new int[2][LENGTH];

        for (int i = 0; i < 2; i++)// fill the board with the desired amount
        {
            for (int j = 0; j < LENGTH; j++) {
                if (j == 6) {
                    board[i][j] = 0;
                    boardSave[i][j] = 0;
                } else {
                    board[i][j] = startingPits;
                    boardSave[i][j] = startingPits;
                }
            }
        }
        active = 0;//set active player to player A
        undos = 3;// set undos for first turn
        canGo = true;// set canGo for first turn
        canUndo = false;// no undos until after the first move
        gameover = false;// game just started

    }

    /**
     * attach listener to the model
     * @param change the changeListener being attached
     */
    public void attach(ChangeListener change) {
        listeners.add(change);
    }

    /**
     * notifies all listeners attached to this model
     */
    public void notifyListeners() {
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * takes in a pit and which side it is on. does the basic game logic and
     * checks all condition of the game updates the board
     *
     * @param pit the pit currently being used
     * @param side the side the pit is on
     */
    public void turn(int pit, int side) {
        if (gameover)// check if game is still going
        {
            return;
        }
        if (side != active)// check for the right side
        {
            return;
        }
        if (board[side][pit] == 0)// check for empty pit
        {
            return;
        }
        int held = board[side][pit];
        int index = pit;
        int sideIndex = side;
        if (canGo) {
            this.saveBoard();//save current board before moves are made
            board[side][pit] = 0;// set the picked pit to 0;
        }
        while (held > 0 && canGo) {// if player still has turn

            if (index >= LENGTH - 1)// reached end of board
            {
                index = 0;
                sideIndex = switchPlayer(sideIndex);
            } else if (index == LENGTH - 1 && side != active) // skip other players mancala
            {
                index = 0;
                sideIndex = switchPlayer(sideIndex);// next pit
            } else {
                index++;
            }
            // drop stones
            board[sideIndex][index]++;
            held--;
        }
        canUndo = true;// already moved can now undo
        canGo = false;// already moved 

        //steal from opposite players pit
        if (sideIndex == active && index < LENGTH - 1 && board[sideIndex][index] == 1)// check if last placed is in an empty pit
        {

            if (board[switchPlayer(sideIndex)][5 - index] != 0)// checks if opposite pit is empty or not
            {
                board[sideIndex][LENGTH - 1] += board[switchPlayer(sideIndex)][5 - index] + 1;
                board[switchPlayer(sideIndex)][5 - index] = 0;
                board[sideIndex][index] = 0;
            }
        }

        if (sideIndex == active && index == LENGTH - 1)// checks if last piece was in mancala
        {
            canGo = true;// free turn
        }
        this.notifyListeners();
    }

    /**
     * checks if the one side has no pieces left
     */
    private void gameover() {

        int sumA = 0;
        int sumB = 0;
        for (int i = 0; i < LENGTH - 1; i++) {
            sumA += board[0][i];
            sumB += board[1][i];
        }
        if (sumA == 0) {//if side A wins give points to A from side B
            board[1][LENGTH - 1] += sumB;
            clearRow(1);
            gameover = true;
        } else if (sumB == 0) {//if side B wins give points to B from side A
            board[0][LENGTH - 1] += sumA;
            clearRow(0);
            gameover = true;
        }
    }

    
    /**
     * clears the given side
     * @param side the side to be cleared
     */
    private void clearRow(int side) {
        for (int i = 0; i < LENGTH - 1; i++) {
            board[side][i] = 0;
        }
        this.notifyListeners();
    }

    /**
     * ends the current players turn and set to next player
     */
    public void done() {
        undos = 3;
        active = switchPlayer(active);
        canGo = true;
        gameover();
        this.notifyListeners();
    }

    /**
     * takes the current player as input and returns the opposite player
     *
     * @param activePlayer
     * @return returns the index of the opposite player
     */
    public int switchPlayer(int activePlayer) {
        if (activePlayer == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * saves the current board so it can be used by undo
     */
    public void saveBoard() {
        for (int i = 0; i < 2; i++) {
            System.arraycopy(board[i], 0, boardSave[i], 0, LENGTH);
        }

    }

    /**
     * resets the board to before the last move
     */
    public void undo() {
        if (undos > 0 && canUndo) { //sets board to savedBoard
            for (int i = 0; i < 2; i++) {
                System.arraycopy(boardSave[i], 0, board[i], 0, LENGTH);
            }
            undos--;// undo used
            canGo = true;// can redo turn
            canUndo = false;// can't undo twice in a row
        }
        this.notifyListeners();

    }

    /**
     * 
     * @return returns the board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * 
     * @return the current player
     */
    public int getActive() {
        return active;
    }

    /**
     * 
     * @return the number of undos
     */
    public int getUndos() {
        return undos;
    }

    /**
     * 
     * @return true if game is over false if it is not
     */
    public boolean isGameover() {
        return gameover;
    }
}