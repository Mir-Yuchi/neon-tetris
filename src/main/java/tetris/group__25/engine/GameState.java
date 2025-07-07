package tetris.group__25.engine;

import javafx.scene.input.KeyEvent;

public interface GameState {
    // Interface for game states in the Tetris game engine
    void handleInput(KeyEvent e);
    void update(long now);
    void render();
}