
package mancala;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Main {

    /**
     * @author Ryan Eager
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(4,1));
        JButton threeRed = new JButton("3 Stones & Red Pits");
        JButton fourRed = new JButton("4 Stones & Red Pits");
        JButton threeBlue = new JButton("3 Stones & Blue Pits");
        JButton fourBlue = new JButton("4 Stones & Blue Pits");
        frame.add(threeRed);
        frame.add(fourRed);
        frame.add(threeBlue);
        frame.add(fourBlue);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        //action listenrs for gametype buttons
        threeRed.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                MancalaView view = new MancalaView(3,0);
            }
        });
        
        fourRed.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                MancalaView view = new MancalaView(4,0);
            }
        });
        
        threeBlue.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                MancalaView view = new MancalaView(3,1);
            }
        });
        
        fourBlue.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                MancalaView view = new MancalaView(4,1);
            }
        });
        
    }

}
