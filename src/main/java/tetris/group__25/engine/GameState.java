package tetris.group__25.engine;

import javafx.scene.input.KeyEvent;

public interface GameState {
    void handleInput(KeyEvent e);
    void update(long now);
    void render();
}
