package tetris.group__25.render;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import tetris.group__25.engine.Board;
import tetris.group__25.tetromino.Tetromino;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles rendering of the Tetris game UI components, including the game board,
 * hold piece, next pieces, stats, and game over message.
 */
public class Renderer {
    private static final int ROWS = 20;
    private static final int COLS = 10;
    private final Pane boardPane;
    private final Pane holdPane;
    private final Pane nextPane;
    private final Rectangle background;
    private final StackPane rootStack;
    private final Label scoreLabel;
    private final Label levelLabel;
    private final Label linesLabel;
    private final Label highScoreLabel;
    private Label gameOverLabel;
    private final Rectangle[][] gridRectangles;
    private final List<Rectangle> temporaryPieceRectangles;
    private double cellSize;
    private double offsetX;
    private double offsetY;

    /**
     * Constructs a Renderer with the specified JavaFX components.
     *
     * @param boardPane      Pane for the main game board
     * @param holdPane       Pane for the hold piece
     * @param nextPane       Pane for the next pieces
     * @param background     Rectangle for the background gradient
     * @param rootStack      Root StackPane for overlaying game over label
     * @param scoreLabel     Label for displaying the score
     * @param levelLabel     Label for displaying the level
     * @param linesLabel     Label for displaying the number of lines cleared
     * @param highScoreLabel Label for displaying the high score
     */
    public Renderer(Pane boardPane, Pane holdPane, Pane nextPane, Rectangle background, StackPane rootStack,
                    Label scoreLabel, Label levelLabel, Label linesLabel, Label highScoreLabel) {
        /*
            * Initializes the Renderer with the provided JavaFX components.
         */
        this.boardPane = boardPane;
        this.holdPane = holdPane;
        this.nextPane = nextPane;
        this.background = background;
        this.rootStack = rootStack;
        this.scoreLabel = scoreLabel;
        this.levelLabel = levelLabel;
        this.linesLabel = linesLabel;
        this.highScoreLabel = highScoreLabel;
        this.gridRectangles = new Rectangle[ROWS][COLS];
        this.temporaryPieceRectangles = new ArrayList<>();
        initializeGrid();
        initializeGameOverLabel();
        boardPane.widthProperty().addListener((observable, oldValue, newValue) -> updateGridLayout());
        boardPane.heightProperty().addListener((observable, oldValue, newValue) -> updateGridLayout());
    }

    /**
     * Initializes the game board grid with rectangles for each cell.
     */
    private void initializeGrid() {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                Rectangle rect = new Rectangle();
                rect.setFill(Color.BLACK);
                rect.setStroke(Color.gray(0.2));
                rect.setStrokeWidth(0.5);
                rect.setEffect(new DropShadow(3, Color.gray(0.8)));
                boardPane.getChildren().add(rect);
                gridRectangles[y][x] = rect;
            }
        }
        updateGridLayout();
    }

    /**
     * Initializes the game over label, centered on the game area.
     */
    private void initializeGameOverLabel() {
        gameOverLabel = new Label("Game Over!\nPress R to Restart");
        gameOverLabel.setStyle(
                "-fx-font-size: 28;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: rgba(0,0,0,0.7);" +
                        "-fx-padding: 20;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;"
        );
        gameOverLabel.setAlignment(Pos.CENTER);
        gameOverLabel.setVisible(false);
        rootStack.getChildren().add(gameOverLabel);
        StackPane.setAlignment(gameOverLabel, Pos.CENTER);
    }

    /**
     * Updates the grid layout based on the current pane size, ensuring dynamic resizing.
     */
    private void updateGridLayout() {
        double boardWidth = boardPane.getWidth();
        double boardHeight = boardPane.getHeight();
        if (boardWidth <= 0 || boardHeight <= 0) return;
        double cellWidth = boardWidth / COLS;
        double cellHeight = boardHeight / ROWS;
        cellSize = Math.min(cellWidth, cellHeight);
        offsetX = (boardWidth - cellSize * COLS) / 2;
        offsetY = (boardHeight - cellSize * ROWS) / 2;
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                Rectangle rect = gridRectangles[y][x];
                rect.setX(offsetX + x * cellSize);
                rect.setY(offsetY + y * cellSize);
                rect.setWidth(cellSize - 1);
                rect.setHeight(cellSize - 1);
            }
        }
        double piecePaneSize = 4 * cellSize;
        holdPane.setPrefSize(piecePaneSize, piecePaneSize);
        double nextPaneHeight = (piecePaneSize + 10) * 3; // For 3 pieces
        nextPane.setPrefHeight(nextPaneHeight);
    }

    /**
     * Renders the game state, including the board, pieces, and stats.
     *
     * @param board     The game board
     * @param score     Current score
     * @param level     Current level
     * @param lines     Total lines cleared
     * @param highScore High score
     */
    public void render(Board board, int score, int level, int lines, int highScore) {
        if (board == null) return;
        gameOverLabel.setVisible(board.isGameOver());
        updateBackground(level);
        updateGrid(board.getGrid());
        clearTemporaryPieces();
        Tetromino currentPiece = board.getCurrentPiece();
        if (currentPiece != null) {
            renderPiece(currentPiece, false);
            int[] ghostPos = board.getGhostPosition();
            Tetromino ghost = currentPiece.clone();
            ghost.setY(ghostPos[1]);
            renderPiece(ghost, true);
        }
        renderHoldPiece(board.getHoldPiece());
        renderNextPieces(board.getNextPieces(3));
        updateStats(score, level, lines, highScore);
    }

    /**
     * Updates the background with a gradient based on the current level.
     *
     * @param level The current game level
     */
    private void updateBackground(int level) {
        Stop[] stops = new Stop[] {
                new Stop(0, Color.hsb(level * 10 % 360, 0.8, 0.8)),
                new Stop(1, Color.hsb((level * 10 + 180) % 360, 0.8, 0.8))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        background.setFill(gradient);
    }

    /**
     * Updates the game board grid based on the current grid state.
     *
     * @param grid The game board grid array
     */
    private void updateGrid(int[][] grid) {
        if (grid == null) return;
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                int code = grid[y][x];
                Color color = code == 0 ? Color.BLACK : Tetromino.getColorFromCode(code);
                gridRectangles[y][x].setFill(color);
            }
        }
    }

    /**
     * Clears temporary rectangles used for rendering pieces.
     */
    private void clearTemporaryPieces() {
        boardPane.getChildren().removeAll(temporaryPieceRectangles);
        temporaryPieceRectangles.clear();
    }

    /**
     * Renders a tetromino piece on the game board.
     *
     * @param piece   The tetromino to render
     * @param isGhost Whether the piece is a ghost piece (semi-transparent)
     */
    private void renderPiece(Tetromino piece, boolean isGhost) {
        if (piece == null) return;
        int[][] shape = piece.getShape();
        if (shape == null) return;
        Color color = piece.getColor();
        if (isGhost) {
            color = color.deriveColor(0, 1, 1, 0.3);
        }
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    int gridX = piece.getX() + c;
                    int gridY = piece.getY() + r;
                    if (gridY >= 0 && gridY < ROWS && gridX >= 0 && gridX < COLS) {
                        double rectX = offsetX + gridX * cellSize;
                        double rectY = offsetY + gridY * cellSize;
                        Rectangle rect = new Rectangle(rectX, rectY, cellSize - 1, cellSize - 1);
                        rect.setFill(color);
                        rect.setEffect(new DropShadow(3, Color.gray(0.8)));
                        boardPane.getChildren().add(rect);
                        temporaryPieceRectangles.add(rect);
                    }
                }
            }
        }
    }

    /**
     * Renders the hold piece in the hold pane.
     *
     * @param hold The hold tetromino
     */
    private void renderHoldPiece(Tetromino hold) {
        renderPieceInPane(hold, holdPane);
    }

    /**
     * Renders the next pieces in the next pane.
     *
     * @param nextPieces List of next tetrominos to display
     */
    private void renderNextPieces(List<Tetromino> nextPieces) {
        nextPane.getChildren().clear();
        if (nextPieces == null) return;
        double startY = 0;
        for (Tetromino next : nextPieces) {
            if (next == null) continue;
            int[][] shape = next.getShape();
            if (shape == null) continue;
            Color color = next.getColor();
            double pieceHeight = shape.length * cellSize;
            double startX = (nextPane.getWidth() - shape[0].length * cellSize) / 2;
            for (int r = 0; r < shape.length; r++) {
                for (int c = 0; c < shape[r].length; c++) {
                    if (shape[r][c] != 0) {
                        Rectangle rect = new Rectangle(startX + c * cellSize, startY + r * cellSize, cellSize - 1, cellSize - 1);
                        rect.setFill(color);
                        rect.setEffect(new DropShadow(3, Color.gray(0.8)));
                        nextPane.getChildren().add(rect);
                    }
                }
            }
            startY += pieceHeight + 10;
        }
    }

    /**
     * Renders a tetromino in a specified pane (e.g., hold or next).
     *
     * @param tetromino The tetromino to render
     * @param pane      The pane in which to render the tetromino
     */
    private void renderPieceInPane(Tetromino tetromino, Pane pane) {
        pane.getChildren().clear();
        if (tetromino != null) {
            int[][] shape = tetromino.getShape();
            if (shape == null) return;
            Color color = tetromino.getColor();
            double startX = (pane.getWidth() - shape[0].length * cellSize) / 2;
            double startY = (pane.getHeight() - shape.length * cellSize) / 2;
            for (int r = 0; r < shape.length; r++) {
                for (int c = 0; c < shape[r].length; c++) {
                    if (shape[r][c] != 0) {
                        Rectangle rect = new Rectangle(startX + c * cellSize, startY + r * cellSize, cellSize - 1, cellSize - 1);
                        rect.setFill(color);
                        rect.setEffect(new DropShadow(3, Color.gray(0.8)));
                        pane.getChildren().add(rect);
                    }
                }
            }
        }
    }

    /**
     * Updates the stat labels with the current game stats.
     *
     * @param score     Current score
     * @param level     Current level
     * @param lines     Total lines cleared
     * @param highScore Current high score
     */
    private void updateStats(int score, int level, int lines, int highScore) {
        scoreLabel.setText("Score: " + score);
        levelLabel.setText("Level: " + level);
        linesLabel.setText("Lines: " + lines);
        highScoreLabel.setText("High Score: " + highScore);
    }
}