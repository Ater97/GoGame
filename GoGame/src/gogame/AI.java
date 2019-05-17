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
    private int col = 0, row =0, SIZE, attempts;
    private final GameBoard.State current_player = GameBoard.State.WHITE;
    Point lasPoint;
    public Grid play(Grid grid, Point lastPoint) {
        this.grid = grid;
        this.SIZE = grid.SIZE;
        this.lasPoint = lastPoint;
        attempts = 0;
        do {
            //Temp random 
            col = ThreadLocalRandom.current().nextInt(0, SIZE);
            row = ThreadLocalRandom.current().nextInt(0, SIZE);
            if(attempts<3)
                getcoordinates();
       } while (!tryAddStone());
        System.out.println("Attempts " + attempts);
        return this.grid;
    }
    public void getcoordinates(){
        attempts++;
        if(isSurrounded())
            return;   
        col = lasPoint.x;
        row = lasPoint.y;
        if(!grid.isOccupied(lasPoint.x, lasPoint.y+1))
            row = lasPoint.y+1;
        else if(!grid.isOccupied(lasPoint.x+1, lasPoint.y))
            col = lasPoint.x+1;
        else if(!grid.isOccupied(lasPoint.x, lasPoint.y-1))
            row = lasPoint.y-1;
        else if(!grid.isOccupied(lasPoint.x-1, lasPoint.y))
            col = lasPoint.x-1;
    }
    public boolean isSurrounded(){
        for (int i = 0; i < SIZE-1; i++) {
            for (int j = 0; j < SIZE-1; j++) {
                if(grid.stones[i][j] !=null){
                    Chain tempChain =grid.stones[i][j].chain;
                    for(Stone stone:tempChain.stones){
                        if(stone.state ==current_player){
                            if(stone.liberties<=1) { 
                                if(!grid.isOccupied(i+1, j)){
                                    row = i+1;
                                    col = j;
                                }                             
                                else if(!grid.isOccupied(i, j+1)){
                                    col = j+1;
                                    row = i;
                                }  
                                else if(!grid.isOccupied(i, j-1)){
                                    col = j-1;
                                    row =i;
                                }     
                                else if(!grid.isOccupied(i-1, j)){
                                    row = i-1;
                                    col =j;
                                }       
                                System.out.println("is surrounded");
                                return true;
                            }
                        }
                    }
                }    
            }
        }
        return false;
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
            System.out.println("Suicide move of AI");
            return false;
        }     
        grid.addStone(row, col, current_player);
        return true;
    }
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

        return up && left && down && rigth;
    }
    
}
