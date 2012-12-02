
package mancala;

import java.util.ArrayList;

import javax.swing.event.*;


public class Model {

    private static final int LENGTH = 7;
    private int[][] board;
    private int[][] boardSave;
    private int active;
    private int undoA;
    private int undoB;
    private boolean gameover;
    ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

    public Model(int startingPits) {
        board = new int[2][LENGTH];
        
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (j == 6) {
                    board[i][j] = 0;
                } else {
                    board[i][j] = startingPits;
                }
            }
        }
        active = 0;
        undoA = 3;
        undoB = 3;
        gameover = false;
    }

    public void attatch(ChangeListener change) {
        listeners.add(change);
    }

    public void notifyListeners() {
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public void turn(int pit, int side) {
        this.saveBoard();
        System.out.println("side: " + side + " pit: " + pit);
        undoA = 3;
        undoB = 3;
        if (side != active) {
            return;
        }
        int held = board[side][pit];
        board[side][pit] = 0;
        int index = pit;
        int sideIndex = side;
        while (held > 0 && !gameover) {
            if (index >= LENGTH - 1)// reached end of board
            {
                index = 0;
                sideIndex = switchPlayer(sideIndex);
                System.out.println("switched sides");
            } else if (index == LENGTH - 1 && side != active) // skip other players mancala
            {
                index = 0;
                sideIndex = switchPlayer(sideIndex);
            } else {
                index++;
            }

            // drop stones
            board[sideIndex][index]++;
            held--;
        }
        if (sideIndex == active && index < LENGTH - 1 && board[sideIndex][index] == 1) {
            
            if(board[switchPlayer(sideIndex)][5 - index] != 0)
            {
                board[sideIndex][LENGTH - 1] += board[switchPlayer(sideIndex)][5 - index] + 1;
                board[switchPlayer(sideIndex)][5 - index] = 0;
                board[sideIndex][index] = 0;
            }
            

        }
        if (sideIndex != active || index != LENGTH - 1) {
            active = switchPlayer(active);
        }
        gameover();
        if (gameover) {
            System.out.println("game over");
        }
        this.notifyListeners();
    }

    private void gameover() {
        
        int sum;
        for (int i = 0; i < 2; i++) {
            sum = 0;
            for (int j = 0; j < LENGTH - 1; j++) {
                sum += board[i][j];
            }
            if (sum == 0) {
                System.out.println("side: " + i + " has cleared");
                gameover = true;
            }
        }
    }

    private int next(int pit) {
        if (pit > LENGTH) {
            pit = 0;
        } else {
            pit++;
        }
        return pit;
    }

    public int switchPlayer(int activePlayer) {
        if (activePlayer == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    public void saveBoard() {
        boardSave = board.clone();

    }

    public void undo() {
        if (active == 0 && undoA > 0) {
            board = boardSave.clone();
            undoA--;
        } else if (active == 1 && undoB > 0) {
            board = boardSave.clone();
            undoB--;
        }
        this.notifyListeners();
    }

    public int[][] getBoard() {
        return board;
    }

    public int getActive() {
        return active;
    }
    
}