package tetris.group__25.tetromino;

public class ZPiece extends Tetromino {
    public ZPiece() {
        // ZPiece represents the Z-shaped tetromino in Tetris
        super(new int[][] {
                {7, 7, 0},
                {0, 7, 7},
                {0, 0, 0}
        }, 7);
    }
}