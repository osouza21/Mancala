package mancala;

import java.util.ArrayList;
import javax.swing.event.*;

/**
 *
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
     *
     * @param change
     */
    public void attatch(ChangeListener change) {
        listeners.add(change);
    }

    /**
     *
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
        if (sumA == 0) {
            board[0][LENGTH - 1] += sumB;
            clearRow(1);
            gameover = true;
        } else if (sumB == 0) {
            board[1][LENGTH - 1] += sumA;
            clearRow(0);
            gameover = true;
        }
    }

    private void clearRow(int side) {
        for (int i = 0; i < LENGTH - 1; i++) {
            board[side][i] = 0;
        }
        this.notifyListeners();
    }

    /**
     * ends the current players turn and set to next player
     *
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
     * @return returns the opposite player
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
            for (int j = 0; j < LENGTH; j++) {
                boardSave[i][j] = board[i][j];
            }
        }

    }

    /**/
    public void undo() {
        if (undos > 0 && canUndo) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < LENGTH; j++) {
                    board[i][j] = boardSave[i][j];
                }
            }
            undos--;
            canGo = true;
            canUndo = false;
        }
        this.notifyListeners();

    }

    /**
     *
     * @return
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     *
     * @return
     */
    public int getActive() {
        return active;
    }

    public int getUndos() {
        return undos;
    }

    public boolean isGameover() {
        return gameover;
    }
}