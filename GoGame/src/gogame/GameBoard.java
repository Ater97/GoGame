package gogame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/** Provides I/O.
   */
 public class GameBoard extends JPanel {

private static final long serialVersionUID = -494530433694385328L;

/**
 * Number of rows/columns.
 */
private static int SIZE = 9;
/**
 * Number of tiles in row/column. (Size - 1)
 */
public static int N_OF_TILES = SIZE - 1;
public static final int TILE_SIZE = 40;
public static final int BORDER_SIZE = TILE_SIZE;

/**
 * Black/white player/stone
 * 
 *
 */
public enum State {
    BLACK, WHITE
}
private State current_player;
private Grid grid;
private Point lastMove;
private AI AIplayer;

public GameBoard(Integer size) {
    SIZE = (size==null && size==0) ? 9:size;
    N_OF_TILES = SIZE - 1;
    grid = new Grid(SIZE);
    // Black always starts
    current_player = State.BLACK;
    AIplayer = new AI();

    this.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            repaint();
            // Converts to float for float division and then rounds to
            // provide nearest intersection.
            int row = Math.round((float) (e.getY() - BORDER_SIZE)
                    / TILE_SIZE);
            int col = Math.round((float) (e.getX() - BORDER_SIZE)
                    / TILE_SIZE);
            // Check wherever it's valid
            if (row >= SIZE || col >= SIZE || row < 0 || col < 0) 
                return;
            if (grid.isOccupied(row, col)) 
                return;
            if(checkSuicide(row, col, current_player)){
                System.out.println("Suicide move of Black");
                return;
            }
            grid.addStone(row, col, current_player);
            lastMove = new Point(col, row);
            //repaint(); 
            grid = AIplayer.play(grid, lastMove);
            lastMove = AIplayer.getLastMove();
            // Switch current player
            /* if (current_player == State.BLACK) 
                current_player = State.WHITE;
            else 
                current_player = State.BLACK;
            */
            //repaint();
        }
    });
}
public boolean checkSuicide(int row, int col, State player){
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

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(Color.BLACK);
    // Draw rows.
    for (int i = 0; i < SIZE; i++) {
        g2.drawLine(BORDER_SIZE, i * TILE_SIZE + BORDER_SIZE, TILE_SIZE
                * N_OF_TILES + BORDER_SIZE, i * TILE_SIZE + BORDER_SIZE);
    }
    // Draw columns.
    for (int i = 0; i < SIZE; i++) {
        g2.drawLine(i * TILE_SIZE + BORDER_SIZE, BORDER_SIZE, i * TILE_SIZE
                + BORDER_SIZE, TILE_SIZE * N_OF_TILES + BORDER_SIZE);
    }
    // Iterate over intersections
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            State state = grid.getState(row, col);
            if (state != null) {
                if (state == State.BLACK) {
                    g2.setColor(Color.BLACK);
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillOval(col * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                        row * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                        TILE_SIZE, TILE_SIZE);
            }
        }
    }
    // Highlight last move
    if (lastMove != null) {
        g2.setColor(Color.RED);
        g2.drawOval(lastMove.x * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                lastMove.y * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                TILE_SIZE, TILE_SIZE);
    }
}

@Override
public Dimension getPreferredSize() {
    return new Dimension(N_OF_TILES * TILE_SIZE + BORDER_SIZE * 2,
            N_OF_TILES * TILE_SIZE + BORDER_SIZE * 2);
}

}