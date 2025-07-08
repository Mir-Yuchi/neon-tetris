package tetris.group__25.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tetris.group__25.render.Renderer;
import tetris.group__25.score.HighScoreManager;
import tetris.group__25.score.ScoreSystem;

import java.util.List;

public class GameEngine {
    private final Scene scene;
    private final Renderer renderer;
    private final Board board;
    private final ScoreSystem scoreSystem;
    private final HighScoreManager highScoreManager;
    private GameState state;
    private final AnimationTimer timer;
    private boolean isSoftDropping = false;
    private long lastSoftDrop = 0;
    private Runnable onBackToMenu;

    public GameEngine(Scene scene, Renderer renderer) {
        this.scene = scene;
        this.renderer = renderer;
        this.board = new Board();
        this.scoreSystem = new ScoreSystem();
        this.highScoreManager = new HighScoreManager();
        this.state = new RunningState(this);

        // Set up renderer callbacks
        renderer.setOnRestartGame(this::restartGame);
        renderer.setOnBackToMenu(this::backToMenu);

        scene.setOnKeyPressed(this::handleInput);
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.DOWN) {
                isSoftDropping = false;
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                state.update(now);
                state.render();
            }
        };
    }

    public void start() {
        board.reset();
        scoreSystem.reset();
        renderer.hideGameOverOverlay();
        renderer.hidePauseOverlay();
        setState(new RunningState(this));
        timer.start();
    }

    public void restartGame() {
        board.reset();
        scoreSystem.reset();
        renderer.hideGameOverOverlay();
        setState(new RunningState(this));
    }

    public void setOnBackToMenu(Runnable callback) {
        this.onBackToMenu = callback;
    }

    public void backToMenu() {
        timer.stop();
        renderer.hideGameOverOverlay();
        renderer.hidePauseOverlay();
        if (onBackToMenu != null) {
            onBackToMenu.run();
        }
    }

    private void handleInput(KeyEvent e) {
        state.handleInput(e);
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getHighScore() {
        return highScoreManager.getHighScore();
    }

    private class RunningState implements GameState {
        private final GameEngine engine;
        private long lastUpdate = 0;

        RunningState(GameEngine engine) {
            this.engine = engine;
        }

        @Override
        public void handleInput(KeyEvent e) {
            if (renderer.isAnimating()) return; // Block input during animation

            switch (e.getCode()) {
                case LEFT -> board.moveLeft();
                case RIGHT -> board.moveRight();
                case DOWN -> {
                    isSoftDropping = true;
                    board.moveDown();
                    checkBoardState();
                }
                case UP -> board.rotate();
                case SPACE -> {
                    board.hardDrop();
                    checkBoardState();
                }
                case C -> board.hold();
                case P -> engine.setState(new PausedState(engine));
            }
        }

        @Override
        public void update(long now) {
            if (renderer.isAnimating()) return; // Don't update during animation

            if (lastUpdate == 0) lastUpdate = now;
            if (isSoftDropping) {
                double interval = 0.05;
                if ((now - lastSoftDrop) / 1e9 >= interval) {
                    board.moveDown();
                    checkBoardState();
                    lastSoftDrop = now;
                }
            } else {
                double elapsed = (now - lastUpdate) / 1e9;
                double speed = Math.max(0.1, 1.0 - scoreSystem.getLevel() * 0.1);
                if (elapsed >= speed) {
                    board.moveDown();
                    checkBoardState();
                    lastUpdate = now;
                }
            }
        }

        @Override
        public void render() {
            renderer.render(board, scoreSystem.getScore(), scoreSystem.getLevel(),
                    scoreSystem.getTotalLines(), engine.getHighScore());
        }

        private void checkBoardState() {
            if (board.isGameOver()) {
                highScoreManager.setHighScore(scoreSystem.getScore());
                engine.setState(new GameOverState(engine));
            } else if (board.isPendingLineClear()) {
                List<Integer> clearedLines = board.getLastClearedLines();
                renderer.animateLineClearing(clearedLines, () -> {
                    board.completeLinesClearing();
                    int cleared = board.getLastLinesCleared();
                    if (cleared > 0) scoreSystem.addClear(cleared);
                });
            }
        }
    }

    private class PausedState implements GameState {
        private final GameEngine engine;

        PausedState(GameEngine engine) {
            this.engine = engine;
            renderer.showPauseOverlay();
        }

        @Override
        public void handleInput(KeyEvent e) {
            if (e.getCode() == KeyCode.P) {
                renderer.hidePauseOverlay();
                engine.setState(new RunningState(engine));
            } else if (e.getCode() == KeyCode.M) {
                renderer.hidePauseOverlay();
                engine.backToMenu();
            }
        }

        @Override
        public void update(long now) { /* paused */ }

        @Override
        public void render() {
            renderer.render(board, scoreSystem.getScore(), scoreSystem.getLevel(),
                    scoreSystem.getTotalLines(), engine.getHighScore());
        }
    }

    private class GameOverState implements GameState {
        private final GameEngine engine;

        GameOverState(GameEngine engine) {
            this.engine = engine;
            renderer.showGameOverOverlay();
        }

        @Override
        public void handleInput(KeyEvent e) {
            if (e.getCode() == KeyCode.R) {
                engine.restartGame();
            } else if (e.getCode() == KeyCode.M) {
                engine.backToMenu();
            }
        }

        @Override
        public void update(long now) { /* game over */ }

        @Override
        public void render() {
            renderer.render(board, scoreSystem.getScore(), scoreSystem.getLevel(),
                    scoreSystem.getTotalLines(), engine.getHighScore());
        }
    }
}
