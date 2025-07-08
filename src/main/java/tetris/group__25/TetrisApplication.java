package tetris.group__25;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import tetris.group__25.engine.GameEngine;
import tetris.group__25.render.Renderer;

import java.util.List;

public class TetrisApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(700);

        Rectangle background = new Rectangle();
        background.setFill(Color.BLACK);

        // Game board UI with improved styling
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        borderPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        Pane boardPane = new Pane();
        boardPane.setStyle("-fx-background-color: rgba(20, 20, 40, 0.8); -fx-border-color: #00FFFF; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Enhanced side panels
        VBox holdArea = new VBox(10);
        Label holdLabel = new Label("HOLD");
        holdLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        Pane holdPane = new Pane();
        holdPane.setPrefSize(120, 120);
        holdPane.setStyle("-fx-background-color: rgba(40, 40, 80, 0.9); -fx-border-color: #FF00FF; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        holdArea.getChildren().addAll(holdLabel, holdPane);
        holdArea.setAlignment(Pos.CENTER);

        VBox nextArea = new VBox(10);
        Label nextLabel = new Label("NEXT");
        nextLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        Pane nextPane = new Pane();
        nextPane.setPrefSize(120, 360);
        nextPane.setStyle("-fx-background-color: rgba(40, 40, 80, 0.9); -fx-border-color: #FF00FF; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        nextArea.getChildren().addAll(nextLabel, nextPane);
        nextArea.setAlignment(Pos.CENTER);

        // Compact stats area
        HBox statsArea = new HBox(15);
        statsArea.setAlignment(Pos.CENTER);
        statsArea.setPadding(new Insets(10));
        statsArea.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-border-color: #00FFFF; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label scoreLabel = new Label("Score: 0");
        Label levelLabel = new Label("Level: 0");
        Label linesLabel = new Label("Lines: 0");
        Label highScoreLabel = new Label("High Score: 0");

        String compactStatsStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: rgba(0, 100, 150, 0.3); -fx-padding: 8 12; -fx-border-color: #00FFFF; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4;";
        scoreLabel.setStyle(compactStatsStyle);
        levelLabel.setStyle(compactStatsStyle);
        linesLabel.setStyle(compactStatsStyle);
        highScoreLabel.setStyle(compactStatsStyle);

        statsArea.getChildren().addAll(scoreLabel, levelLabel, linesLabel, highScoreLabel);

        borderPane.setCenter(boardPane);
        borderPane.setLeft(holdArea);
        borderPane.setRight(nextArea);
        borderPane.setBottom(statsArea);

        StackPane rootStack = new StackPane(background, borderPane);
        background.widthProperty().bind(rootStack.widthProperty());
        background.heightProperty().bind(rootStack.heightProperty());

        Scene scene = new Scene(rootStack, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Initialize game engine and renderer
        Renderer renderer = new Renderer(boardPane, holdPane, nextPane, background, rootStack,
                scoreLabel, levelLabel, linesLabel, highScoreLabel);
        GameEngine engine = new GameEngine(scene, renderer);

        // Enhanced menu background with animation
        Rectangle menuBackground = new Rectangle();
        menuBackground.widthProperty().bind(rootStack.widthProperty());
        menuBackground.heightProperty().bind(rootStack.heightProperty());
        LinearGradient menuGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(15, 15, 35)),
                new Stop(0.3, Color.rgb(35, 15, 55)),
                new Stop(0.7, Color.rgb(55, 35, 75)),
                new Stop(1, Color.rgb(25, 45, 95)));
        menuBackground.setFill(menuGradient);

        // Improved title with better sizing
        Label titleLabel = new Label("TETRIS");
        titleLabel.setStyle(
                "-fx-font-size: 42px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-padding: 20;"
        );
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        // Enhanced subtitle
        Label subtitleLabel = new Label("The Ultimate Experience");
        subtitleLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-style: italic;" +
                        "-fx-text-fill: #AAAAFF;" +
                        "-fx-effect: dropshadow(gaussian, #AAAAFF, 10, 0.6, 0, 0);"
        );
        subtitleLabel.setAlignment(Pos.CENTER);

        // Main Menu with better layout
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(350);
        menuBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.85);" +
                        "-fx-padding: 35;" +
                        "-fx-border-color: linear-gradient(to right, #00FFFF, #FF00FF);" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.7), 30, 0.8, 0, 0);"
        );

        String btnStyle =
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(0, 255, 255, 0.4), rgba(255, 0, 255, 0.4));" +
                        "-fx-padding: 12 25;" +
                        "-fx-border-color: #00FFFF;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.6), 12, 0.6, 0, 0);" +
                        "-fx-cursor: hand;";

        String btnHoverStyle =
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(0, 255, 255, 0.7), rgba(255, 0, 255, 0.7));" +
                        "-fx-padding: 12 25;" +
                        "-fx-border-color: #FFFF00;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 255, 0, 0.9), 18, 0.8, 0, 0);" +
                        "-fx-cursor: hand;" +
                        "-fx-scale-x: 1.05;" +
                        "-fx-scale-y: 1.05;";

        StackPane menuContainer = new StackPane(menuBackground);

        // Set up back to menu callback
        engine.setOnBackToMenu(() -> {
            if (!rootStack.getChildren().contains(menuContainer)) {
                rootStack.getChildren().add(menuContainer);
            }
        });

        for (String text : List.of("Start Game", "High Scores", "Instructions", "Quit")) {
            Button b = new Button(text);
            b.setStyle(btnStyle);
            b.setPrefWidth(280);
            b.setPrefHeight(55);

            // Enhanced hover effects
            b.setOnMouseEntered(e -> b.setStyle(btnHoverStyle));
            b.setOnMouseExited(e -> b.setStyle(btnStyle));

            b.setOnAction(ae -> {
                switch (text) {
                    case "Start Game" -> {
                        rootStack.getChildren().remove(menuContainer);
                        engine.start();
                    }
                    case "Quit" -> primaryStage.close();
                    case "High Scores" -> {
                        VBox scoreBox = new VBox(15);
                        scoreBox.setAlignment(Pos.CENTER);
                        scoreBox.setPrefWidth(300);

                        Label scoreTitle = new Label("High Scores");
                        scoreTitle.setStyle(
                                "-fx-font-size: 24px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-text-fill: #FFFFFF;" +
                                        "-fx-alignment: center;"
                        );

                        Label hs = new Label("Best Score: " + String.format("%,d", engine.getHighScore()));
                        hs.setStyle(
                                "-fx-font-size: 20px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-text-fill: #FFFFFF;" +
                                        "-fx-alignment: center;"
                        );

                        Label clickHint = new Label("Click anywhere to close");
                        clickHint.setStyle(
                                "-fx-font-size: 12px;" +
                                        "-fx-text-fill: #AAAAAA;" +
                                        "-fx-font-style: italic;" +
                                        "-fx-alignment: center;"
                        );

                        scoreBox.getChildren().addAll(scoreTitle, hs, clickHint);
                        scoreBox.setStyle(
                                "-fx-background-color: rgba(0, 0, 0, 0.95);" +
                                        "-fx-padding: 30;" +
                                        "-fx-border-color: #00FFFF;" +
                                        "-fx-border-width: 2;" +
                                        "-fx-border-radius: 15;" +
                                        "-fx-background-radius: 15;" +
                                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.8), 20, 0.8, 0, 0);" +
                                        "-fx-alignment: center;"
                        );

                        StackPane popup = new StackPane(scoreBox);
                        popup.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
                        rootStack.getChildren().add(popup);
                        popup.setOnMouseClicked(ev -> rootStack.getChildren().remove(popup));
                    }
                    case "Instructions" -> {
                        VBox instBox = new VBox(12);
                        instBox.setAlignment(Pos.CENTER);
                        instBox.setPrefWidth(400);

                        Label instTitle = new Label("GAME CONTROLS");
                        instTitle.setStyle(
                                "-fx-font-size: 22px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-text-fill: #FFFFFF;" +
                                        "-fx-alignment: center;"
                        );

                        GridPane controlsGrid = new GridPane();
                        controlsGrid.setAlignment(Pos.CENTER);
                        controlsGrid.setHgap(15);
                        controlsGrid.setVgap(8);

                        String[][] controls = {
                                {"← →", "Move Left/Right"},
                                {"↑", "Rotate Piece"},
                                {"↓", "Soft Drop"},
                                {"SPACE", "Hard Drop"},
                                {"C", "Hold Piece"},
                                {"P", "Pause/Resume"},
                                {"M", "Menu (when paused)"},
                                {"R", "Restart (game over)"}
                        };

                        for (int i = 0; i < controls.length; i++) {
                            Label key = new Label(controls[i][0]);
                            key.setStyle(
                                    "-fx-font-size: 14px;" +
                                            "-fx-font-weight: bold;" +
                                            "-fx-text-fill: #FFFF00;" +
                                            "-fx-background-color: rgba(255, 255, 0, 0.1);" +
                                            "-fx-padding: 4 8;" +
                                            "-fx-border-color: #FFFF00;" +
                                            "-fx-border-width: 1;" +
                                            "-fx-border-radius: 3;" +
                                            "-fx-background-radius: 3;"
                            );

                            Label description = new Label(controls[i][1]);
                            description.setStyle(
                                    "-fx-font-size: 14px;" +
                                            "-fx-text-fill: #FFFFFF;"
                            );

                            controlsGrid.add(key, 0, i);
                            controlsGrid.add(description, 1, i);
                        }

                        Label clickHint = new Label("Click anywhere to close");
                        clickHint.setStyle(
                                "-fx-font-size: 11px;" +
                                        "-fx-text-fill: #AAAAAA;" +
                                        "-fx-font-style: italic;" +
                                        "-fx-alignment: center;"
                        );

                        instBox.getChildren().addAll(instTitle, controlsGrid, clickHint);
                        instBox.setStyle(
                                "-fx-background-color: rgba(0, 0, 0, 0.95);" +
                                        "-fx-padding: 25;" +
                                        "-fx-border-color: #00FFFF;" +
                                        "-fx-border-width: 2;" +
                                        "-fx-border-radius: 12;" +
                                        "-fx-background-radius: 12;" +
                                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.7), 18, 0.7, 0, 0);"
                        );

                        StackPane popup = new StackPane(instBox);
                        popup.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
                        rootStack.getChildren().add(popup);
                        popup.setOnMouseClicked(ev -> rootStack.getChildren().remove(popup));
                    }
                }
            });
            menuBox.getChildren().add(b);
        }

        VBox fullMenuBox = new VBox(25);
        fullMenuBox.setAlignment(Pos.CENTER);
        fullMenuBox.getChildren().addAll(titleLabel, subtitleLabel, menuBox);

        menuContainer.getChildren().add(fullMenuBox);
        rootStack.getChildren().add(menuContainer);

        primaryStage.setTitle("Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
