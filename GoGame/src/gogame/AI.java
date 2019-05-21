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
            attempts++;
       } while (!tryAddStone());
        System.out.println("Attempts " + attempts);
        return this.grid;
    }
    public void getcoordinates(){
        if(isSurrounded())
            return;   
        col = lasPoint.x;
        row = lasPoint.y;
        if(lasPoint.y+1+attempts<SIZE && !grid.isOccupied(lasPoint.x, lasPoint.y+1+attempts))
            row = lasPoint.y+1+attempts;
        else if(lasPoint.x+1+attempts<SIZE && !grid.isOccupied(lasPoint.x+1+attempts, lasPoint.y))
            col = lasPoint.x+1+attempts;
        else if(lasPoint.y-1-attempts>=0 && !grid.isOccupied(lasPoint.x, lasPoint.y-1-attempts))
            row = lasPoint.y-1-attempts;
        else if(lasPoint.x-1-attempts>=0 && !grid.isOccupied(lasPoint.x-1-attempts, lasPoint.y))
            col = lasPoint.x-1-attempts;
    }
     
    public boolean isSurrounded(){
        for (int i = 0; i < SIZE; i++) { //col
            for (int j = 0; j < SIZE; j++) { //row
                if(grid.stones[i][j] !=null){
                    Stone stone =grid.stones[i][j];
                    if(stone.state ==current_player){
                        if(stone.liberties<=2 && stone.liberties > 0) { 
                            if(i+1<SIZE && !grid.isOccupied(i+1, j)){
                                row = i+1;
                                col = j;
                            }                             
                            else if(j+1<SIZE && !grid.isOccupied(i, j+1)){
                                row = i;
                                col = j+1; 
                            }  
                            else if(j-1>=0 && !grid.isOccupied(i, j-1)){
                                row = i;
                                col = j-1;
                            }     
                            else if(i-1>=0 && !grid.isOccupied(i-1, j)){
                                row = i-1;
                                col = j;
                            }       
                            System.out.println("is surrounded");
                            return true;
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
        if(grid.checkSuicide(row, col, current_player)){
            System.out.println("Suicide move of AI");
            return false;
        }     
        grid.addStone(row, col, current_player);
        return true;
    }
}
