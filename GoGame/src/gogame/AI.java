/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gogame;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author SORANTES
 */
public class AI {
    private Grid grid;
    private int col = 0, row =0;
    private int SIZE;
    private final GameBoard.State current_player = GameBoard.State.WHITE;
    public Grid play(Grid grid) {
        this.grid = grid;
        this.SIZE = grid.SIZE;
        do {
           col = ThreadLocalRandom.current().nextInt(0, SIZE);
           row = ThreadLocalRandom.current().nextInt(0, SIZE);
       } while (!tryAddStone());
        return this.grid;
    }

    public Point getLastMove() {
        return new Point(col, row);
    }

    
    
    public boolean tryAddStone(){
        if (row >=  SIZE|| col >= SIZE || row < 0 || col < 0) 
            return false;
        if (grid.isOccupied(row, col)) 
            return false;
        if(checkSuicide(row, col, current_player)){
            System.out.println("Suicide move AI");
            return false;
        }     
        grid.addStone(row, col, current_player);
        return true;
    }
    
/*public boolean checkSuicide(int row, int col, GameBoard.State player){
    if (row +1 < SIZE && grid.isOccupiedByEnemy(row + 1, col, player))  
        return true;
    if (row -1 > 0 && grid.isOccupiedByEnemy(row - 1, col, player))
        return true;
    if (col +1 < SIZE && grid.isOccupiedByEnemy(row, col + 1, player)) 
        return true;
    if (col -1 > 0 && grid.isOccupiedByEnemy(row, col - 1, player)) 
        return true;
    return false;
}*/
    public boolean checkSuicide(int row, int col, GameBoard.State player){
     boolean up = false, down = false, left = false, rigth = false;
    if (row +1 < SIZE){
        if( grid.isOccupiedByEnemy(row + 1, col, player)) 
            rigth = true;
    }    
    else
        rigth = true;
    if (row -1 > 0 ){
        if( grid.isOccupiedByEnemy(row - 1, col, player))
            left  = true;
    }
    else
        left = true;    
    if (col +1 < SIZE ){
        if( grid.isOccupiedByEnemy(row, col + 1, player)) 
            up = true;   
    }
    else
        up = true;   
    if (col -1 > 0 ){
        if( grid.isOccupiedByEnemy(row, col - 1, player)) 
            down = true;
    }
    else
        down = true;    
    
    return up&&left&&down&&rigth;
}
}
