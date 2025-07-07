package tetris.group__25.tetromino;

public class SPiece extends Tetromino {
// The SPiece class represents the S tetromino in Tetris.
    public SPiece() {
        super(new int[][] {
                {0, 5, 5},
                {5, 5, 0},
                {0, 0, 0}
        }, 5);
    }
}