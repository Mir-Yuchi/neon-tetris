package tetris.group__25.tetromino;

public class OPiece extends Tetromino {
    public OPiece() {
        super(new int[][] {
                {4,4},
                {4,4}
        }, 4);
    }

    @Override
    public int[][] rotate() {
        return getShape();
    }
}
