package tetris.group__25.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tetris.group__25.render.Renderer;
import tetris.group__25.score.HighScoreManager;
import tetris.group__25.score.ScoreSystem;

/**
 * Manages the game logic and state transitions.
 */
public class GameEngine {
    /**
     * The main scene for the game.
     */
    private final Scene scene;
    private final Renderer renderer;
    private final Board board;
    private final ScoreSystem scoreSystem;
    private final HighScoreManager highScoreManager;
    private GameState state;
    private final AnimationTimer timer;
    private boolean isSoftDropping = false;
    private long lastSoftDrop = 0;

    public GameEngine(Scene scene, Renderer renderer) {
        /*
         * Initializes the game engine with the given scene and renderer.

         * @param scene The main scene for the game.
         * @param renderer The renderer responsible for drawing the game state.
         */
        this.scene = scene;
        this.renderer = renderer;
        this.board = new Board();
        this.scoreSystem = new ScoreSystem();
        this.highScoreManager = new HighScoreManager();
        this.state = new RunningState(this);

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
        /*
         * Starts the game engine, initializing the game state and starting the animation timer.
         */
        timer.start();
    }

    private void handleInput(KeyEvent e) {
        /*
         * Handles input events for the game.
         * @param e The KeyEvent containing the input data.
         */
        state.handleInput(e);
    }

    public void setState(GameState state) {
        /*
         * Sets the current game state.
         * @param state The new game state to set.
         */
        this.state = state;
    }

    public int getHighScore() {
        /*
         * Retrieves the current high score from the HighScoreManager.
         * @return The current high score.
         */
        return highScoreManager.getHighScore();
    }

    private class RunningState implements GameState {
        /*
         * Represents the running state of the game where the player can control the tetrominoes.
         */
        private final GameEngine engine;
        private long lastUpdate = 0;

        RunningState(GameEngine engine) {
            /*
             * Initializes the running state with the game engine.
             * @param engine The game engine managing the game state.
             */
            this.engine = engine;
        }

        @Override
        public void handleInput(KeyEvent e) {
            /*
             * Handles input events for the running state.
             * @param e The KeyEvent containing the input data.
             */
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
            }
        }

        @Override
        public void update(long now) {
            /*
             * Updates the game state based on the current time.
             * @param now The current time in nanoseconds.
             */
            if (lastUpdate == 0) lastUpdate = now;
            if (isSoftDropping) {
                double softDropInterval = 0.05;
                double elapsedSoftDrop = (now - lastSoftDrop) / 1_000_000_000.0;
                if (elapsedSoftDrop >= softDropInterval) {
                    board.moveDown();
                    checkBoardState();
                    lastSoftDrop = now;
                }
            } else {
                double elapsed = (now - lastUpdate) / 1_000_000_000.0;
                double fallSpeed = Math.max(0.1, 1.0 - (scoreSystem.getLevel() * 0.1));
                if (elapsed >= fallSpeed) {
                    board.moveDown();
                    checkBoardState();
                    lastUpdate = now;
                }
            }
        }

        @Override
        public void render() {
            /*
             * Renders the current game state.
             */
            renderer.render(board, scoreSystem.getScore(), scoreSystem.getLevel(), scoreSystem.getTotalLines(), engine.getHighScore());
        }

        private void checkBoardState() {
            /*
             * Checks the state of the board after a move.
             * If the game is over, transitions to the GameOverState.
             * If lines were cleared, updates the score system.
             */
            if (board.isGameOver()) {
                highScoreManager.setHighScore(scoreSystem.getScore());
                engine.setState(new GameOverState(engine));
            } else {
                int linesCleared = board.getLastLinesCleared();
                if (linesCleared > 0) {
                    scoreSystem.addClear(linesCleared);
                }
            }
        }
    }

    private class GameOverState implements GameState {
        /*
         * Represents the game over state where the player can restart the game.
         */
        private final GameEngine engine;

        GameOverState(GameEngine engine) {
            this.engine = engine;
        }

        @Override
        public void handleInput(KeyEvent e) {
            /*
             * Handles input events for the game over state.
             * Allows the player to restart the game by pressing 'R'.
             * @param e The KeyEvent containing the input data.
             */
            if (e.getCode() == KeyCode.R) {
                engine.board.reset();
                engine.scoreSystem.reset();
                engine.setState(new RunningState(engine));
            }
        }

        @Override
        public void update(long now) {
            // No updates during game over
        }

        @Override
        public void render() {
            /*
             * Renders the game over screen.
             * Displays the final score, level, total lines cleared, and high score.
             */
            renderer.render(board, scoreSystem.getScore(), scoreSystem.getLevel(), scoreSystem.getTotalLines(), engine.getHighScore());
        }
    }
}