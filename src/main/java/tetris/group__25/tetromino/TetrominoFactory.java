package tetris.group__25.tetromino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TetrominoFactory {
// TetrominoFactory is responsible for creating and managing tetromino pieces in Tetris.
    private final List<Class<? extends Tetromino>> tetrominoes;
    private final List<Tetromino> bag;

    public TetrominoFactory() {
        // Initialize the list of tetromino classes
        tetrominoes = new ArrayList<>();
        tetrominoes.add(IPiece.class);
        tetrominoes.add(JPiece.class);
        tetrominoes.add(LPiece.class);
        tetrominoes.add(OPiece.class);
        tetrominoes.add(SPiece.class);
        tetrominoes.add(TPiece.class);
        tetrominoes.add(ZPiece.class);
        bag = new ArrayList<>();
        refillBag();
    }

    public Tetromino nextPiece() {
        // Returns the next tetromino piece from the bag, refilling it if necessary.
        if (bag.isEmpty()) {
            refillBag();
        }
        return bag.remove(0);
    }

    private void refillBag() {
        // Refill the bag with all tetromino pieces, shuffling them for randomness.
        bag.clear();
        for (Class<? extends Tetromino> tetrominoClass : tetrominoes) {
            try {
                bag.add(tetrominoClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Failed to create tetromino", e);
            }
        }
        Collections.shuffle(bag);
    }
}