
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
    private boolean gameover;
    private int undos;
    private boolean done;
    private boolean canGo;
    ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    

    public Model(int startingPits) {
        board = new int[2][LENGTH];
        boardSave = new int[2][LENGTH];
        
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
        undos = 3;
        gameover = false;
        canGo = true;
        done = false;
                
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
    * 
    * @param pit
    * @param side 
    */
    public void turn(int pit, int side) {
        
        if(side != active)
        {
            return;
        }
        if(board[side][pit] == 0){
            return;
        }
        this.saveBoard();
        int held = board[side][pit];
        board[side][pit] = 0;
        int index = pit;
        int sideIndex = side;
        while (held > 0 && canGo) {
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
        canGo = false;
        //steal from opposite players pit
        if (sideIndex == active && index < LENGTH - 1 && board[sideIndex][index] == 1) {
            
            if(board[switchPlayer(sideIndex)][5 - index] != 0)
            {
                board[sideIndex][LENGTH - 1] += board[switchPlayer(sideIndex)][5 - index] + 1;
                board[switchPlayer(sideIndex)][5 - index] = 0;
                board[sideIndex][index] = 0;
            }
        }
        
        if (sideIndex == active && index == LENGTH - 1) {
            canGo = true;
        }
        else if (done)
        {
            active = switchPlayer(active);
        }
        gameover();
        if (gameover) {
            System.out.println("game over");
        }
        this.notifyListeners();
    }

    /**
     * 
     */
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

    /**
     *
     **/
    private void Done() {
        done = true;
        
    }

    /**
     * 
     * @param activePlayer
     * @return 
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
            for (int j = 0; j < LENGTH - 1; j++)
            {
                boardSave[i][j] = board[i][j];
            }
        }
            
    }

    /**/
    public void undo()
    {
        if(undos > 0);
        {
            for (int i = 0; i < 2; i++) 
            {
                for (int j = 0; j < LENGTH; j++)
                {
                    board[i][j] = boardSave[i][j];
                }
            }
            undos--;
        }            
            this.notifyListeners();

    }

    /**
     * 
     * @return 
     */
    public int[][] getBoard()
    {
        return board;
    }

    /**
     * 
     * @return 
     */
    public int getActive() {
        return active;
    }
    
}