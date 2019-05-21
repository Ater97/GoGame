/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gogame;

import gogame.GameBoard.State;
import java.util.ArrayList;

/**
 *
 * @author sorantes
 */
public class Chain {
    
public ArrayList<Integer> stonesIDS = new ArrayList<>();
public State state ;
    
public Chain(State state) {
    this.state = state;
}

public void addStoneID(int ID){
    int index = stonesIDS.indexOf(ID);
    if(index < 0)
        stonesIDS.add(ID);
}

public void joinChains(Chain neighbor){
    ArrayList<Integer> two = neighbor.stonesIDS;
    two.stream()
            .filter((x) -> (!stonesIDS.contains(x)))
            .forEachOrdered((x) -> {
                stonesIDS.add(x);
    });
}

public boolean containsID(int id){
    if(stonesIDS.contains(id))
        return true;
    return false;
}
}
