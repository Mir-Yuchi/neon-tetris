package tetris.group__25.render;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.util.Duration;
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
    private VBox gameOverBox, pauseBox;
    private final Rectangle[][] gridRectangles;
    private final List<Rectangle> tempRects = new ArrayList<>();
    private double cellSize, offsetX, offsetY;
    private Runnable onRestartGame, onBackToMenu;
    private boolean isAnimating = false;

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
        initializeGameOverBox();
        initializePauseBox();

        boardPane.widthProperty().addListener((o, v1, v2) -> updateGridLayout());
        boardPane.heightProperty().addListener((o, v1, v2) -> updateGridLayout());
    }

    public void setOnRestartGame(Runnable callback) {
        this.onRestartGame = callback;
    }

    public void setOnBackToMenu(Runnable callback) {
        this.onBackToMenu = callback;
    }

    private void initializeGrid() {
        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLS; x++) {
                Rectangle r = new Rectangle();
                r.setFill(Color.rgb(35, 40, 60));
                r.setStroke(Color.rgb(70, 75, 95));
                r.setStrokeWidth(1);
                boardPane.getChildren().add(r);
                gridRectangles[y][x] = r;
            }
        updateGridLayout();
    }

    private void initializeGameOverBox() {
        gameOverBox = new VBox(20);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setPrefWidth(400);

        Label gameOverTitle = new Label("GAME OVER");
        gameOverTitle.setStyle(
                "-fx-font-size: 36px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #E74C3C;" +
                "-fx-font-family: 'Arial Black', Arial, sans-serif;" +
                "-fx-alignment: center;"
        );

        Label gameOverSubtitle = new Label("Better luck next time!");
        gameOverSubtitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: #BDC3C7;" +
                "-fx-font-family: Arial, sans-serif;" +
                "-fx-alignment: center;"
        );

        String buttonStyle =
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: #3498DB;" +
                "-fx-padding: 12 24;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-alignment: center;" +
                "-fx-font-family: Arial, sans-serif;";

        String buttonHoverStyle = buttonStyle +
                "-fx-background-color: #2980B9;";

        Button restartButton = new Button("Restart Game (R)");
        restartButton.setStyle(buttonStyle);
        restartButton.setPrefWidth(200);
        restartButton.setOnMouseEntered(e -> restartButton.setStyle(buttonHoverStyle));
        restartButton.setOnMouseExited(e -> restartButton.setStyle(buttonStyle));
        restartButton.setOnAction(e -> {
            if (onRestartGame != null) onRestartGame.run();
        });

        Button menuButton = new Button("Back to Menu (M)");
        menuButton.setStyle(buttonStyle);
        menuButton.setPrefWidth(200);
        menuButton.setOnMouseEntered(e -> menuButton.setStyle(buttonHoverStyle));
        menuButton.setOnMouseExited(e -> menuButton.setStyle(buttonStyle));
        menuButton.setOnAction(e -> {
            if (onBackToMenu != null) onBackToMenu.run();
        });

        gameOverBox.getChildren().addAll(gameOverTitle, gameOverSubtitle, restartButton, menuButton);
        gameOverBox.setStyle(
                "-fx-background-color: rgba(20, 20, 30, 0.95);" +
                "-fx-padding: 40;" +
                "-fx-border-color: #E74C3C;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-alignment: center;"
        );
        gameOverBox.setVisible(false);
        rootStack.getChildren().add(gameOverBox);
        StackPane.setAlignment(gameOverBox, Pos.CENTER);
    }

    private void initializePauseBox() {
        pauseBox = new VBox(20);
        pauseBox.setAlignment(Pos.CENTER);
        pauseBox.setPrefWidth(320);

        Label pauseTitle = new Label("GAME PAUSED");
        pauseTitle.setStyle(
                "-fx-font-size: 32px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F39C12;" +
                "-fx-font-family: 'Arial Black', Arial, sans-serif;" +
                "-fx-alignment: center;"
        );

        Label pauseInstructions = new Label("Press P to Resume\nPress M for Menu");
        pauseInstructions.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #ECF0F1;" +
                "-fx-text-alignment: center;" +
                "-fx-alignment: center;" +
                "-fx-line-spacing: 5px;" +
                "-fx-font-family: Arial, sans-serif;"
        );

        pauseBox.getChildren().addAll(pauseTitle, pauseInstructions);
        pauseBox.setStyle(
                "-fx-background-color: rgba(20, 20, 30, 0.95);" +
                "-fx-padding: 35;" +
                "-fx-border-color: #F39C12;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-alignment: center;"
        );
        pauseBox.setVisible(false);
        rootStack.getChildren().add(pauseBox);
        StackPane.setAlignment(pauseBox, Pos.CENTER);
    }

    public void showPauseOverlay() { pauseBox.setVisible(true); }
    public void hidePauseOverlay() { pauseBox.setVisible(false); }
    public void showGameOverOverlay() { gameOverBox.setVisible(true); }
    public void hideGameOverOverlay() { gameOverBox.setVisible(false); }

    private void updateGridLayout() {
        double w = boardPane.getWidth(), h = boardPane.getHeight();
        if (w <= 0 || h <= 0) return;
        cellSize = Math.min(w / COLS, h / ROWS) * 0.95;
        offsetX = (w - COLS * cellSize) / 2;
        offsetY = (h - ROWS * cellSize) / 2;

        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLS; x++) {
                Rectangle r = gridRectangles[y][x];
                r.setX(offsetX + x * cellSize);
                r.setY(offsetY + y * cellSize);
                r.setWidth(cellSize - 2);
                r.setHeight(cellSize - 2);
            }

        double size4 = 4 * cellSize;
        holdPane.setPrefSize(size4 + 20, size4 + 20);
        nextPane.setPrefHeight((size4 + 30) * 3);
    }

    public void render(Board board, int score, int level, int lines, int highScore) {
        gameOverBox.setVisible(board.isGameOver());
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
        Color baseColor = Color.rgb(25, 30, 45);
        Color accentColor = Color.rgb(40, 45, 70);
        Color highlightColor = Color.rgb(50, 55, 85);

        Stop[] stops = {
                new Stop(0, baseColor),
                new Stop(0.3, accentColor),
                new Stop(0.7, highlightColor),
                new Stop(1, baseColor)
        };
        background.setFill(new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE, stops));
    }

    private void updateGrid(int[][] grid) {
        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLS; x++) {
                int code = grid[y][x];
                if (code == 0) {
                    gridRectangles[y][x].setFill(Color.rgb(35, 40, 60));
                } else {
                    Color baseColor = Tetromino.getColorFromCode(code);
                    gridRectangles[y][x].setFill(baseColor.deriveColor(0, 0.8, 1.1, 0.9));
                }
            }
    }

    private void renderPiece(Tetromino t, boolean ghost) {
        if (t == null) return;
        Color c = t.getColor();
        if (ghost) {
            c = c.deriveColor(0, 0.3, 1.2, 0.4);
        } else {
            c = c.deriveColor(0, 0.8, 1.1, 0.95);
        }
        int[][] shape = t.getShape();
        for (int r = 0; r < shape.length; r++)
            for (int c2 = 0; c2 < shape[r].length; c2++)
                if (shape[r][c2] != 0) {
                    double x = offsetX + (t.getX()+c2)*cellSize;
                    double y = offsetY + (t.getY()+r)*cellSize;
                    Rectangle rect = new Rectangle(x, y, cellSize-2, cellSize-2);
                    rect.setFill(c);
                    rect.setStroke(Color.rgb(255, 255, 255, 0.3));
                    rect.setStrokeWidth(1);
                    boardPane.getChildren().add(rect);
                    tempRects.add(rect);
                }
    }

    private void renderNextPieces(List<Tetromino> list) {
        nextPane.getChildren().clear();
        double yOff = 20;
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
                                cellSize-2, cellSize-2
                        );
                        Color enhancedColor = t.getColor().deriveColor(0, 0.8, 1.1, 0.9);
                        rect.setFill(enhancedColor);
                        rect.setStroke(Color.rgb(255, 255, 255, 0.3));
                        rect.setStrokeWidth(1);
                        nextPane.getChildren().add(rect);
                    }
            yOff += shape.length*cellSize + 25;
        }
    }

    public void animateLineClearing(List<Integer> clearedLines, Runnable onComplete) {
        if (clearedLines.isEmpty()) {
            onComplete.run();
            return;
        }

        isAnimating = true;
        List<Rectangle> linesToAnimate = new ArrayList<>();

        for (int line : clearedLines) {
            for (int x = 0; x < COLS; x++) {
                linesToAnimate.add(gridRectangles[line][x]);
            }
        }

        ParallelTransition parallelTransition = new ParallelTransition();

        for (Rectangle rect : linesToAnimate) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), rect);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            ScaleTransition scaleOut = new ScaleTransition(Duration.millis(300), rect);
            scaleOut.setFromX(1.0);
            scaleOut.setFromY(1.0);
            scaleOut.setToX(1.1);
            scaleOut.setToY(1.1);

            rect.setFill(Color.WHITE);

            ParallelTransition rectAnimation = new ParallelTransition(fadeOut, scaleOut);
            parallelTransition.getChildren().add(rectAnimation);
        }

        parallelTransition.setOnFinished(e -> {
            for (Rectangle rect : linesToAnimate) {
                rect.setOpacity(1.0);
                rect.setScaleX(1.0);
                rect.setScaleY(1.0);
            }
            isAnimating = false;
            onComplete.run();
        });

        parallelTransition.play();
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    private void updateStats(int score, int level, int lines, int highScore) {
        scoreLabel.setText("Score: " + String.format("%,d", score));
        levelLabel.setText("Level: " + level);
        linesLabel.setText("Lines: " + lines);
        highScoreLabel.setText("High: " + String.format("%,d", highScore));
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
                            cellSize-2, cellSize-2
                    );
                    Color enhancedColor = t.getColor().deriveColor(0, 0.8, 1.1, 0.9);
                    rect.setFill(enhancedColor);
                    rect.setStroke(Color.rgb(255, 255, 255, 0.3));
                    rect.setStrokeWidth(1);
                    pane.getChildren().add(rect);
                }
    }
}
