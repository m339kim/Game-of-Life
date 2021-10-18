/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *

/* Implementation of Game of GameOfLife. Contains methods that plays the game according to the rules below. */

/**
 * < Rules of John Conway's Game of Life >
 *  The Life world is a two-dimensional plane of cells.
 *  Each cell may be empty or contain a single creature.
 *  Each day, creatures are born or die in each cell according to the neighboring creatures on the previous day.
 *  A neighbor is a cell that adjoins the cell either horizontally, vertically, or diagonally. 
 *  If the player reaches 10 rounds, game ends.
 */

/**
 *
 * @author leannekim. Dec 5, 2019.
 */
public class GameOfLife {

    int columns, rows;
    int[][] board;

    /**
     * constructor
     * pre: none
     * post: columns, row have been initialized. board object has been created.
     */
    public GameOfLife() {
        // Initialize rows, columns and set-up arrays
        columns = 20;
        rows = 20;
        board = new int[columns][rows];
        init();
    }

    /**
     * Set all cells dead
     * pre: none
     * post: All cells have been initialized to 0.
     */
    public void init() {
        for (int i =1;i < columns-1;i++) {
          for (int j =1;j < rows-1;j++) {
            board[i][j] = 0;
          }
        }
    }

    /**
     * Enliven cells.
     * pre: none
     * post: Value of cells with coordinates passed through parameters are changed to 1.
     * @param x
     * @param y 
     */
    public void enliven(int x, int y){
        board[x][y] = 1;
    }
    
    /**
     * Kill cells.
     * pre: none
     * post: Value of cells with coordinates passed through parameters are changed to 0.
     */
    public void kill(int x, int y){
        board[x][y] = 0;
    }
    
    /**
     * Check neighbors, create new generation
     * pre: none
     * post: board has been assigned as nextGen.
     */
    public void nextGen() {
            // array for next generation
            int[][] nextGen = new int[columns][rows];

            // Loop through every spot in the array and check spots neighbors
            for (int x = 1; x < columns-1; x++) {
                for (int y = 1; y < rows-1; y++) {

                    // Add up all the states in a 3x3 surrounding grid
                    int numNeighbors = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            numNeighbors += board[x+i][y+j];
                        }
                    }

                  // Subtract current cell's state (already added it in the loop above)
                  numNeighbors -= board[x][y];
                  
                  if ((board[x][y] == 1) && (numNeighbors <  2)){         // Underpopulation. Dies.  (less than 2 neighbors)
                        nextGen[x][y] = 0;      
                  } else if ((board[x][y] == 1) && (numNeighbors >  3)){  // Overpopulation. Dies.   (more than 3 neighbors)
                        nextGen[x][y] = 0;
                  } else if ((board[x][y] == 0) && (numNeighbors == 3)) { // Reproduction. Lives.    (dead cell back to life)
                        nextGen[x][y] = 1;
                  } else { 
                        nextGen[x][y] = board[x][y];  // Cells with no changes
                  }
                }
            }
            
            board = nextGen;
    }
    
    /**
     * Checks if the cell with parameter coordinates is alive or dead
     * pre: none
     * post: If cell is alive, 1 has been returned. If cell is dead, 0 has been returned.
     * @param x
     * @param y
     * @return 
     */
    public int checkAliveOrDead(int x, int y){
        if (board[x][y] == 1){  // if alive
            return 1;
        } else {
            return 0;
        }
    }
}