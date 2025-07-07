package tetris.group__25.tetromino;

public class OPiece extends Tetromino {
// OPiece represents the O-shaped tetromino in Tetris
    public OPiece() {
        super(new int[][] {
                {4, 4},
                {4, 4}
        }, 4);
    }

    @Override
    public int[][] rotate() {
        // The O piece does not change shape when rotated, so we return the same shape
        return getShape();
    }
}