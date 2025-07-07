package tetris.group__25.tetromino;

public class LPiece extends Tetromino {
    public LPiece() {
        // LPiece represents the L-shaped tetromino in Tetris
        super(new int[][] {
                {0, 0, 3},
                {3, 3, 3},
                {0, 0, 0}
        }, 3);
    }
}