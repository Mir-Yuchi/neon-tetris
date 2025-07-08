package tetris.group__25.engine;

import tetris.group__25.tetromino.Tetromino;
import tetris.group__25.tetromino.TetrominoFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Represents the Tetris game board and manages piece movement and state.
 */
public class Board {
    /*
     * The game board is a 20x10 grid represented as a 2D array.
     */
    private final int[][] grid = new int[20][10];
    private Tetromino currentPiece;
    private Tetromino holdPiece;
    private final Queue<Tetromino> nextPieces = new ArrayDeque<>();
    private final TetrominoFactory factory = new TetrominoFactory();
    private boolean canHold = true;
    private int lastLinesCleared;
    private List<Integer> lastClearedLines = new ArrayList<>();
    private boolean gameOver = false;
    private boolean pendingLineClear = false;

    public Board() {
        // Initialize the board with empty cells (0)
        spawnNewPiece();
        for (int i = 0; i < 3; i++) {
            nextPieces.add(factory.nextPiece());
        }
    }

    public void reset() {
        // Reset the board to its initial state
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++) {
                grid[y][x] = 0;
            }
        }
        nextPieces.clear();
        for (int i = 0; i < 3; i++) {
            nextPieces.add(factory.nextPiece());
        }
        holdPiece = null;
        canHold = true;
        gameOver = false;
        spawnNewPiece();
    }

    public void moveLeft() {
        /*
         * Move the current piece left if possible.
         */
        if (currentPiece != null && canMove(currentPiece.getX() - 1, currentPiece.getY(), currentPiece.getShape())) {
            currentPiece.setX(currentPiece.getX() - 1);
        }
    }

    public void moveRight() {
        /*
         * Move the current piece right if possible.
         */
        if (currentPiece != null && canMove(currentPiece.getX() + 1, currentPiece.getY(), currentPiece.getShape())) {
            currentPiece.setX(currentPiece.getX() + 1);
        }
    }

    public void moveDown() {
        /*
         * Move the current piece down if possible or lock it if it can't move down anymore.
         */
        if (pendingLineClear) return; // Don't move during line clear animation

        if (currentPiece != null && canMove(currentPiece.getX(), currentPiece.getY() + 1, currentPiece.getShape())) {
            currentPiece.setY(currentPiece.getY() + 1);
        } else if (currentPiece != null) {
            lockPiece();
            checkForLineClear();
            if (!pendingLineClear) {
                spawnNewPiece();
                canHold = true;
            }
        }
    }

    public void rotate() {
        /*
         * Rotate the current piece if possible, applying wall kicks if necessary.
         */
        if (currentPiece == null) return;
        int[][] rotated = currentPiece.rotate();
        if (canMove(currentPiece.getX(), currentPiece.getY(), rotated)) {
            currentPiece.setShape(rotated);
        } else {
            // Wall kick (simplified)
            if (canMove(currentPiece.getX() - 1, currentPiece.getY(), rotated)) {
                currentPiece.setX(currentPiece.getX() - 1);
                currentPiece.setShape(rotated);
            } else if (canMove(currentPiece.getX() + 1, currentPiece.getY(), rotated)) {
                currentPiece.setX(currentPiece.getX() + 1);
                currentPiece.setShape(rotated);
            }
        }
    }

    public void hardDrop() {
        /*
         * Move the current piece down to the lowest possible position and lock it.
         */
        if (pendingLineClear) return; // Don't drop during line clear animation

        if (currentPiece == null) return;
        int[] ghostPos = getGhostPosition();
        currentPiece.setY(ghostPos[1]);
        lockPiece();
        checkForLineClear();
        if (!pendingLineClear) {
            spawnNewPiece();
            canHold = true;
        }
    }

    public void hold() {
        /*
         * Hold the current piece, swapping it with the hold piece if possible.
         */
        if (currentPiece == null || !canHold) return;
        Tetromino temp = holdPiece;
        holdPiece = currentPiece.clone();
        holdPiece.setX(0);
        holdPiece.setY(0);
        if (temp == null) {
            spawnNewPiece();
        } else {
            currentPiece = temp;
            if (currentPiece != null) {
                currentPiece.setX(3);
                currentPiece.setY(0);
            }
        }
        canHold = false;
    }

    private void spawnNewPiece() {
        /*
         * Spawn a new piece from the queue of next pieces or generate new pieces if the queue is empty.
         */
        if (nextPieces.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                nextPieces.add(factory.nextPiece());
            }
        }
        currentPiece = nextPieces.poll();
        currentPiece.setX(3);
        currentPiece.setY(0);
        if (!canMove(currentPiece.getX(), currentPiece.getY(), currentPiece.getShape())) {
            currentPiece = null;
            gameOver = true;
        }
        nextPieces.add(factory.nextPiece());
    }

    private boolean canMove(int newX, int newY, int[][] shape) {
        /*
         * Check if the piece can move to the new position without colliding with the grid boundaries or other pieces.
         */
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    int gridX = newX + c;
                    int gridY = newY + r;
                    if (gridX < 0 || gridX >= 10 || gridY >= 20 || (gridY >= 0 && grid[gridY][gridX] != 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void lockPiece() {
        /*
         * Lock the current piece in place on the grid, marking its cells.
         */
        int[][] shape = currentPiece.getShape();
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    int gridX = currentPiece.getX() + c;
                    int gridY = currentPiece.getY() + r;
                    if (gridY >= 0 && gridY < 20 && gridX >= 0 && gridX < 10) {
                        grid[gridY][gridX] = shape[r][c];
                    }
                }
            }
        }
    }

    private void checkForLineClear() {
        List<Integer> fullRows = new ArrayList<>();
        for (int y = 0; y < 20; y++) {
            boolean full = true;
            for (int x = 0; x < 10; x++) {
                if (grid[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                fullRows.add(y);
            }
        }

        lastClearedLines = fullRows;
        lastLinesCleared = fullRows.size();

        if (!fullRows.isEmpty()) {
            pendingLineClear = true;
        }
    }

    public void completeLinesClearing() {
        if (!pendingLineClear) return;

        // Actually clear the lines
        for (int row : lastClearedLines) {
            for (int y = row; y > 0; y--) {
                System.arraycopy(grid[y - 1], 0, grid[y], 0, 10);
            }
            for (int x = 0; x < 10; x++) {
                grid[0][x] = 0;
            }
        }

        pendingLineClear = false;
        spawnNewPiece();
        canHold = true;
    }

    public List<Integer> getLastClearedLines() {
        return new ArrayList<>(lastClearedLines);
    }

    public boolean isPendingLineClear() {
        return pendingLineClear;
    }

    public int[][] getGrid() {
        /*
         * Get a deep copy of the grid to prevent external modifications.
         */
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        return copy;
    }

    public Tetromino getCurrentPiece() {
        /*
         * Get the current piece, returning a clone to prevent external modifications.
         */
        return currentPiece != null ? currentPiece.clone() : null;
    }

    public Tetromino getHoldPiece() {
        /*
         * Get the hold piece, returning a clone to prevent external modifications.
         */
        return holdPiece != null ? holdPiece.clone() : null;
    }

    public Tetromino getNextPiece() {
        /*
         * Get the next piece in the queue, returning a clone to prevent external modifications.
         */
        return nextPieces.peek() != null ? nextPieces.peek().clone() : null;
    }

    public List<Tetromino> getNextPieces(int count) {
        /*
         * Get a list of the next pieces in the queue, returning clones to prevent external modifications.
         */
        List<Tetromino> list = new ArrayList<>();
        Iterator<Tetromino> it = nextPieces.iterator();
        for (int i = 0; i < count && it.hasNext(); i++) {
            list.add(it.next().clone());
        }
        return list;
    }

    public boolean isGameOver() {
        /*
         * Check if the game is over, which happens when a new piece cannot be placed.
         */
        return gameOver;
    }

    public int getLastLinesCleared() {
        /*
         * Get the number of lines cleared by the last piece that was locked.
         */
        return lastLinesCleared;
    }

    public int[] getGhostPosition() {
        /*
         * Calculate the ghost position (where the piece would land if dropped).
         */
        if (currentPiece == null) return new int[]{0, 0};

        int ghostX = currentPiece.getX();
        int ghostY = currentPiece.getY();
        int[][] shape = currentPiece.getShape();

        // Find the lowest position where the piece can be placed
        while (canMove(ghostX, ghostY + 1, shape)) {
            ghostY++;
        }

        return new int[]{ghostX, ghostY};
    }
}
