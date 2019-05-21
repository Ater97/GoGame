package gogame;

import gogame.GameBoard.State;
import java.util.ArrayList;

/**
 * Provides game logic.
 *  
 *
 */
public class Grid {

public final int SIZE;
public int ScoreBlack = 0, ScoreWhite = 0;
/**
 * [row][column]
 */
public Stone[][] stones;
public ArrayList<Chain> chains;

public Grid(int size) {
    SIZE = size;
    stones = new Stone[SIZE][SIZE];
    chains = new ArrayList<>();
}

/**
 * Adds Stone to Grid.
 * @param row
 * @param col
 * @param black
 */
public void addStone(int row, int col, State state) {
    String player = (state == State.BLACK) ? "Black" :"White";
    System.out.println(String.format("%s: y: %d, x: %d",player, row, col));
    Stone newStone = new Stone(row, col, state);
    stones[row][col] = newStone;
    // Check neighbors
    Stone[] neighbors = new Stone[4];
    // Don't check outside the board
    if (row > 0) 
        neighbors[0] = stones[row - 1][col];
    else
        stones[row][col].liberties--;
    if (row < SIZE - 1) 
        neighbors[1] = stones[row + 1][col];
    else
        stones[row][col].liberties--;
    if (col > 0) 
        neighbors[2] = stones[row][col - 1];
    else
        stones[row][col].liberties--;
    if (col < SIZE - 1) 
        neighbors[3] = stones[row][col + 1];
    else
        stones[row][col].liberties--;
    
    // Prepare Chain for this new Stone
    Chain newChain = new Chain(state);
    newChain.addStoneID(newStone.id);
    
    for (Stone neighbor : neighbors) {
        // Do nothing if no adjacent Stone
        if (neighbor == null) 
            continue;
        int neighborRow = neighbor.row, neighborCol = neighbor.col;
        stones[row][col].liberties--;
        stones[neighborRow][neighborCol].liberties--;

        // If it's different color than newStone check him
        if (neighbor.state != stones[row][col].state) {
            checkStone(stones[neighborRow][neighborCol]); 
            continue; 
        }
        //If are the same color add to the same chain
        Chain neighborChain = getChain(stones[neighborRow][neighborCol].id);
        if(neighborChain!=null)
            newChain.joinChains(neighborChain);
        else
            newChain.addStoneID(stones[neighborRow][neighborCol].id);
    }
    chains.add(newChain);
}

/**
 * Check liberties of Stone
 * 
 * @param stone
 */
public void checkStone(Stone stone) {
    // Every Stone is part of a Chain so we check total liberties
    Chain chain = getChain(stone.id);
    if (getLiberties(chain) == 0) {
        int tempRow=0, tempCol=0;
        for (int id : chain.stonesIDS) {
            tempCol = id/10;
            tempRow = id%10;
            increaseNeighborsLiberties(stones[tempRow][tempCol]);
            stones[tempRow][tempCol] = null;
            getScore(stone);
        }
        stones[stone.row][stone.col] =null;
        getScore(stone);
        System.out.println(String.format("Score Black: %d, White: %d",ScoreBlack, ScoreWhite));
    }
    else
        chains.add(chain);
}

public int getLiberties(Chain s){
    if(s!=null){
        int total =0, tempRow=0, tempCol=0;
         for(Integer id: s.stonesIDS){
            tempCol = id/10;
            tempRow = id%10;
            if(stones[tempRow][tempCol]!=null)
                total += stones[tempRow][tempCol].liberties;
        }
        return total;
    }
    return 0;
}

public Chain getChain(int neighborID){
    for(Chain c:chains){
        if(c.containsID(neighborID)){
            chains.remove(c);
            return c;
        }
    }
    return null;
}

public void increaseNeighborsLiberties(Stone s){
    if (s.row > 0) 
        if(stones[s.row - 1][s.col]!=null)
            stones[s.row - 1][s.col].liberties++;
    if (s.row < SIZE - 1) 
        if(stones[s.row + 1][s.col]!=null)
            stones[s.row + 1][s.col].liberties++;
    if (s.col > 0) 
        if(stones[s.row][s.col - 1]!=null)
            stones[s.row][s.col - 1].liberties++;
    if (s.col < SIZE - 1) 
        if(stones[s.row][s.col + 1]!=null)
            stones[s.row][s.col + 1].liberties++;
}

public void getScore(Stone stone){
    if(stone.state == State.WHITE)
        ScoreBlack++;
    else
        ScoreWhite++;
}

/**
 * Returns true if given position is occupied by any stone
 * 
 * @param row
 * @param col
 * @return true if given position is occupied
 */
public boolean isOccupied(int row, int col) {
    return stones[row][col] != null;
}
public boolean isOccupiedByEnemy(int row, int col, State player) {
    if(isOccupied(row, col))
       return stones[row][col].state != player;
    return false;
}

/**
 * Returns State (black/white) of given position or null if it's unoccupied.
 * Needs valid row and column.
 * 
 * @param row
 * @param col
 * @return
 */
public State getState(int row, int col) {
    Stone stone = stones[row][col];
    if (stone == null) {
        return null;
    } else {
        // System.out.println("getState != null");
        return stone.state;
    }
}
public int getLibertiesbySotone(Stone stone){
    Chain s = getChain(stone.id);
    if(s!=null){
        int total =0, tempRow=0, tempCol=0;
         for(Integer id: s.stonesIDS){
            tempCol = id/10;
            tempRow = id%10;
            if(stones[tempRow][tempCol]!=null)
                total += stones[tempRow][tempCol].liberties;
        }
         chains.add(s);
        return total;
    }
    return 0;
}

public boolean checkSuicide(int row, int col, State player){
    boolean up = false, down = false, left = false, rigth = false;
    if (row +1 < SIZE){
        if(stones[row+1][col]==null)
            return false;
        if(isOccupiedByEnemy(row + 1, col, player) || getLibertiesbySotone(stones[row+1][col])==1) 
            rigth = true;
    }    
    else
        rigth = true;
    if (row -1 > 0 ){
        if(stones[row-1][col]==null)
            return false;
        if(isOccupiedByEnemy(row - 1, col, player) || getLibertiesbySotone(stones[row-1][col])==1)
            left  = true;
    }
    else
        left = true;    
    if (col +1 < SIZE ){
        if(stones[row][col+1]==null)
            return false;
        if(isOccupiedByEnemy(row, col + 1, player) || getLibertiesbySotone(stones[row][col+1])==1) 
            up = true;   
    }
    else
        up = true;   
    if (col -1 > 0 ){
        if(stones[row][col-1]==null)
            return false;
        if(isOccupiedByEnemy(row, col - 1, player) || getLibertiesbySotone(stones[row][col-1])==1) 
            down = true;
    }
    else
        down = true;      
    return up&&left&&down&&rigth;
}
}