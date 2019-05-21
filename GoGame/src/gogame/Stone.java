package gogame;

import gogame.GameBoard.State;
import java.util.ArrayList;

/**
 * Basic game element.
 *
 */
public class Stone {

public int chainID;
public int id;
public State state;
public int liberties = 4;
// Row and col are need to remove (set to null) this Stone from Grid
public int row;
public int col;

public Stone(int row, int col, State state) {
    this.state = state;
    this.row = row;
    this.col = col;
    id = row + 10*col;
}
public void setChainID(int ID){
    this.chainID = ID;
}
}