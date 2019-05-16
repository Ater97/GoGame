/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gogame;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author SORANTES
 */
public class GoGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new GoGame().init();
    }
    public static final String TITLE = "Go Game";
    public static final int BORDER_SIZE = 25;
    
    private void init() {
    JFrame f = new JFrame();
    f.setTitle(TITLE);
    
    
    JPanel container = new JPanel();
    container.setBackground(Color.orange);
    container.setLayout(new BorderLayout());
    f.add(container);
    container.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

    GameBoard board = new GameBoard(9);
    container.add(board);

    f.pack();
    f.setResizable(false);
    f.setLocationByPlatform(true);
    f.setVisible(true);
}}

