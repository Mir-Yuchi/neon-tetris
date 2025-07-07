package tetris.group__25.tetromino;

public class TPiece extends Tetromino {
// TPiece represents the T-shaped tetromino in Tetris
    public TPiece() {
        super(new int[][] {
                {0, 6, 0},
                {6, 6, 6},
                {0, 0, 0}
        }, 6);
    }
}