package tetris.group__25.render;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import tetris.group__25.engine.Board;
import tetris.group__25.tetromino.Tetromino;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static final int ROWS = 20, COLS = 10;
    private final Pane boardPane, holdPane, nextPane;
    private final Rectangle background;
    private final StackPane rootStack;
    private final Label scoreLabel, levelLabel, linesLabel, highScoreLabel;
    private Label gameOverLabel, pauseLabel;
    private final Rectangle[][] gridRectangles;
    private final List<Rectangle> tempRects = new ArrayList<>();
    private double cellSize, offsetX, offsetY;

    public Renderer(Pane boardPane, Pane holdPane, Pane nextPane,
                    Rectangle background, StackPane rootStack,
                    Label scoreLabel, Label levelLabel,
                    Label linesLabel, Label highScoreLabel) {
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

        initializeGrid();
        initializeGameOverLabel();
        initializePauseLabel();

        boardPane.widthProperty().addListener((o, v1, v2) -> updateGridLayout());
        boardPane.heightProperty().addListener((o, v1, v2) -> updateGridLayout());
    }

    private void initializeGrid() {
        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLS; x++) {
                Rectangle r = new Rectangle();
                r.setFill(Color.BLACK);
                r.setStroke(Color.gray(0.2));
                r.setStrokeWidth(0.5);
                r.setEffect(new DropShadow(3, Color.gray(0.8)));
                boardPane.getChildren().add(r);
                gridRectangles[y][x] = r;
            }
        updateGridLayout();
    }

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

    private void initializePauseLabel() {
        pauseLabel = new Label("Paused\nPress P to Resume");
        pauseLabel.setStyle(gameOverLabel.getStyle());
        pauseLabel.setAlignment(Pos.CENTER);
        pauseLabel.setVisible(false);
        rootStack.getChildren().add(pauseLabel);
        StackPane.setAlignment(pauseLabel, Pos.CENTER);
    }

    public void showPauseOverlay() { pauseLabel.setVisible(true); }
    public void hidePauseOverlay() { pauseLabel.setVisible(false); }

    private void updateGridLayout() {
        double w = boardPane.getWidth(), h = boardPane.getHeight();
        if (w <= 0 || h <= 0) return;
        cellSize = Math.min(w / COLS, h / ROWS);
        offsetX = (w - COLS * cellSize) / 2;
        offsetY = (h - ROWS * cellSize) / 2;

        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLS; x++) {
                Rectangle r = gridRectangles[y][x];
                r.setX(offsetX + x * cellSize);
                r.setY(offsetY + y * cellSize);
                r.setWidth(cellSize - 1);
                r.setHeight(cellSize - 1);
            }

        double size4 = 4 * cellSize;
        holdPane.setPrefSize(size4, size4);
        nextPane.setPrefHeight((size4 + 10) * 3);
    }

    public void render(Board board, int score, int level, int lines, int highScore) {
        gameOverLabel.setVisible(board.isGameOver());
        updateBackground(level);
        updateGrid(board.getGrid());
        boardPane.getChildren().removeAll(tempRects);
        tempRects.clear();

        Tetromino cur = board.getCurrentPiece();
        if (cur != null) {
            renderPiece(cur, false);
            Tetromino ghost = cur.clone();
            ghost.setY(board.getGhostPosition()[1]);
            renderPiece(ghost, true);
        }
        renderPieceInPane(board.getHoldPiece(), holdPane);
        renderNextPieces(board.getNextPieces(3));
        updateStats(score, level, lines, highScore);
    }

    private void updateBackground(int level) {
        Stop[] stops = {
                new Stop(0, Color.hsb(level * 10 % 360, 0.8, 0.8)),
                new Stop(1, Color.hsb((level * 10 + 180) % 360, 0.8, 0.8))
        };
        background.setFill(new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE, stops));
    }

    private void updateGrid(int[][] grid) {
        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLS; x++) {
                int code = grid[y][x];
                gridRectangles[y][x].setFill(
                        code == 0 ? Color.BLACK : Tetromino.getColorFromCode(code)
                );
            }
    }

    private void renderPiece(Tetromino t, boolean ghost) {
        if (t == null) return;
        Color c = t.getColor();
        if (ghost) c = c.deriveColor(0,1,1,0.3);
        int[][] shape = t.getShape();
        for (int r = 0; r < shape.length; r++)
            for (int c2 = 0; c2 < shape[r].length; c2++)
                if (shape[r][c2] != 0) {
                    double x = offsetX + (t.getX()+c2)*cellSize;
                    double y = offsetY + (t.getY()+r)*cellSize;
                    Rectangle rect = new Rectangle(x, y, cellSize-1, cellSize-1);
                    rect.setFill(c);
                    rect.setEffect(new DropShadow(3, Color.gray(0.8)));
                    boardPane.getChildren().add(rect);
                    tempRects.add(rect);
                }
    }

    private void renderNextPieces(List<Tetromino> list) {
        nextPane.getChildren().clear();
        double yOff=0;
        for (Tetromino t : list) {
            if (t == null) continue;
            int[][] shape = t.getShape();
            double startX = (nextPane.getWidth() - shape[0].length*cellSize)/2;
            for (int r=0;r<shape.length;r++)
                for (int c2=0;c2<shape[r].length;c2++)
                    if (shape[r][c2]!=0) {
                        Rectangle rect = new Rectangle(
                                startX + c2*cellSize,
                                yOff + r*cellSize,
                                cellSize-1, cellSize-1
                        );
                        rect.setFill(t.getColor());
                        rect.setEffect(new DropShadow(3, Color.gray(0.8)));
                        nextPane.getChildren().add(rect);
                    }
            yOff += shape.length*cellSize + 10;
        }
    }

    private void updateStats(int score, int level, int lines, int highScore) {
        scoreLabel.setText("Score: " + score);
        levelLabel.setText("Level: " + level);
        linesLabel.setText("Lines: " + lines);
        highScoreLabel.setText("High Score: " + highScore);
    }

    private void renderPieceInPane(Tetromino t, Pane pane) {
        pane.getChildren().clear();
        if (t == null) return;
        int[][] shape = t.getShape();
        double startX = (pane.getWidth() - shape[0].length*cellSize)/2;
        double startY = (pane.getHeight() - shape.length*cellSize)/2;
        for (int r=0;r<shape.length;r++)
            for (int c2=0;c2<shape[r].length;c2++)
                if (shape[r][c2]!=0) {
                    Rectangle rect = new Rectangle(
                            startX + c2*cellSize,
                            startY + r*cellSize,
                            cellSize-1, cellSize-1
                    );
                    rect.setFill(t.getColor());
                    rect.setEffect(new DropShadow(3, Color.gray(0.8)));
                    pane.getChildren().add(rect);
                }
    }
}
