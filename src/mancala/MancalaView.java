
package mancala;
//test Ryan

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author kai dao
 * @author Ryan Eager
 */
public class MancalaView implements ChangeListener{
    
    private static final int LENGTH = 7;
    private int [][] board;
    private JButton [][] pits;
    private JButton undo;
    private Model gameModel;
    private JFrame frame; 
    
    public MancalaView(int startingPits, int color){
        gameModel = new Model(startingPits);
        pits = new JButton[2][LENGTH];
        board = gameModel.getBoard();
        
       
        //settin up frame
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        boardsetup();
        
        //mancal's
        //disable the mancals from being cliced
        pits[0][6].setEnabled(false);
        pits[1][6].setEnabled(false);
        frame.add(pits[0][6], BorderLayout.WEST);
        frame.add(pits[1][6], BorderLayout.EAST);
        
         //set color of board
        if(color == 0){
            for(int i=0; i < 2; i++){
               for(int j=0; j < 7; j++){
                   pits[i][j].setBackground(Color.RED);
               }
         }
        }
        else{
            for(int i=0; i < 2; i++){
               for(int j=0; j < 7; j++){
                   pits[i][j].setBackground(Color.BLUE);
               }
         }
        }
        
        
        //lables for pits
        JLabel PlayerA = new JLabel("Player 0");
        PlayerA.setFont(new Font("Serif", Font.PLAIN, 26));
        JLabel PlayerB = new JLabel("Player 1");
        PlayerB.setFont(new Font("Serif", Font.PLAIN, 26));
   
        //Player A's pits
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2,8)); 
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));
        topPanel.add(PlayerA);
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));
        topPanel.add(pits[0][5]);
        topPanel.add(pits[0][4]);
        topPanel.add(pits[0][3]);
        topPanel.add(pits[0][2]);
        topPanel.add(pits[0][1]);
        topPanel.add(pits[0][0]);
        frame.add(topPanel, BorderLayout.NORTH);
        
        //Player B's pits
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2,8));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(pits[1][0]);
        bottomPanel.add(pits[1][1]);
        bottomPanel.add(pits[1][2]);
        bottomPanel.add(pits[1][3]);
        bottomPanel.add(pits[1][4]);
        bottomPanel.add(pits[1][5]);
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(PlayerB);
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(new JLabel(""));
        frame.add(bottomPanel, BorderLayout.SOUTH);
       
        //setup undo button
        undo = new JButton("Undo player " + gameModel.getActive());
        undo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                gameModel.undo();
                gameModel.notifyListeners(); 
            }
        });
        
        frame.add(undo,BorderLayout.CENTER);
        updatePits();
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void boardsetup(){
        for(int i = 0; i <= 1; i++){
            for( int j = 0 ; j < LENGTH; j++){
                pits[i][j] = new JButton();
                final int pit = j;
                final int side = i;
                pits[i][j].addActionListener(new ActionListener(){
                    
                    public void actionPerformed(ActionEvent e) {
                        gameModel.turn(pit, side);
                        gameModel.notifyListeners();
                    }
                });
            }
        }
        gameModel.attatch(this);
    }
    
    private void updatePits(){
        for(int i = 0; i < 2; i++ ){
            for (int j = 0; j < LENGTH; j++){
                pits[i][j].setText(board[i][j] + "");
            }
        }
        undo.setText("Undo player " + gameModel.getActive());
        frame.repaint();
    }
    
    public void stateChanged(ChangeEvent e) {
        board = gameModel.getBoard();
        updatePits();
        frame.repaint();
    }
}
