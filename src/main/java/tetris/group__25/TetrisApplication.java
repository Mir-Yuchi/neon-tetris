package tetris.group__25;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
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
        Rectangle background = new Rectangle();
        background.setFill(Color.BLACK);

        // Game board UI
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        Pane boardPane = new Pane();
        boardPane.setStyle("-fx-background-color: transparent;");

        VBox holdArea = new VBox(10);
        Label holdLabel = new Label("HOLD");
        Pane holdPane = new Pane();
        holdPane.setPrefSize(120, 120);
        holdPane.setStyle("-fx-background-color: transparent;");
        holdArea.getChildren().addAll(holdLabel, holdPane);
        holdArea.setAlignment(Pos.CENTER);

        VBox nextArea = new VBox(10);
        Label nextLabel = new Label("NEXT");
        Pane nextPane = new Pane();
        nextPane.setPrefSize(120, 120);
        nextPane.setStyle("-fx-background-color: transparent;");
        nextArea.getChildren().addAll(nextLabel, nextPane);
        nextArea.setAlignment(Pos.CENTER);

        HBox statsArea = new HBox(30);
        Label scoreLabel = new Label("Score: 0");
        Label levelLabel = new Label("Level: 0");
        Label linesLabel = new Label("Lines: 0");
        Label highScoreLabel = new Label("High Score: 0");
        statsArea.getChildren().addAll(scoreLabel, levelLabel, linesLabel, highScoreLabel);
        statsArea.setAlignment(Pos.CENTER);
        statsArea.setStyle("-fx-background-color: transparent;");

        borderPane.setCenter(boardPane);
        borderPane.setLeft(holdArea);
        borderPane.setRight(nextArea);
        borderPane.setBottom(statsArea);

        StackPane rootStack = new StackPane(background, borderPane);
        background.widthProperty().bind(rootStack.widthProperty());
        background.heightProperty().bind(rootStack.heightProperty());

        Scene scene = new Scene(rootStack, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Initialize game engine and renderer
        Renderer renderer = new Renderer(boardPane, holdPane, nextPane, background, rootStack,
                scoreLabel, levelLabel, linesLabel, highScoreLabel);
        GameEngine engine = new GameEngine(scene, renderer);

        // Create menu background
        Rectangle menuBackground = new Rectangle();
        menuBackground.widthProperty().bind(rootStack.widthProperty());
        menuBackground.heightProperty().bind(rootStack.heightProperty());
        LinearGradient menuGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(25, 25, 50)),
                new Stop(0.5, Color.rgb(50, 25, 75)),
                new Stop(1, Color.rgb(25, 50, 100)));
        menuBackground.setFill(menuGradient);

        // Title label
        Label titleLabel = new Label("NEON TETRIS");
        titleLabel.setStyle(
                "-fx-font-size: 48px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to right, #00FFFF, #FF00FF, #FFFF00);" +
                        "-fx-effect: dropshadow(gaussian, #00FFFF, 20, 0.8, 0, 0);"
        );

        // Main Menu overlay
        VBox menuBox = new VBox(25);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8);" +
                        "-fx-padding: 40;" +
                        "-fx-border-color: linear-gradient(to right, #00FFFF, #FF00FF);" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.6), 25, 0.7, 0, 0);"
        );

        String btnStyle =
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(0, 255, 255, 0.3), rgba(255, 0, 255, 0.3));" +
                        "-fx-padding: 15 30;" +
                        "-fx-border-color: #00FFFF;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.5), 10, 0.5, 0, 0);" +
                        "-fx-cursor: hand;";

        String btnHoverStyle =
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(0, 255, 255, 0.6), rgba(255, 0, 255, 0.6));" +
                        "-fx-padding: 15 30;" +
                        "-fx-border-color: #FFFF00;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 255, 0, 0.8), 15, 0.7, 0, 0);" +
                        "-fx-cursor: hand;";

        StackPane menuContainer = new StackPane(menuBackground);

        // Set up back to menu callback
        engine.setOnBackToMenu(() -> {
            if (!rootStack.getChildren().contains(menuContainer)) {
                rootStack.getChildren().add(menuContainer);
            }
        });

        for (String text : List.of("Start Game", "Scores", "Instructions", "Quit")) {
            Button b = new Button(text);
            b.setStyle(btnStyle);
            b.setPrefWidth(200);
            b.setPrefHeight(50);

            // Add hover effects
            b.setOnMouseEntered(e -> b.setStyle(btnHoverStyle));
            b.setOnMouseExited(e -> b.setStyle(btnStyle));

            b.setOnAction(ae -> {
                switch (text) {
                    case "Start Game" -> {
                        rootStack.getChildren().remove(menuContainer);
                        engine.start();
                    }
                    case "Quit" -> primaryStage.close();
                    case "Scores" -> {
                        Label hs = new Label("High Score: " + engine.getHighScore());
                        hs.setStyle(
                                "-fx-font-size: 32px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-text-fill: #00FFFF;" +
                                        "-fx-background-color: rgba(0, 0, 0, 0.9);" +
                                        "-fx-padding: 30;" +
                                        "-fx-border-color: #00FFFF;" +
                                        "-fx-border-width: 3;" +
                                        "-fx-border-radius: 15;" +
                                        "-fx-background-radius: 15;" +
                                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.8), 20, 0.7, 0, 0);"
                        );
                        StackPane popup = new StackPane(hs);
                        popup.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
                        rootStack.getChildren().add(popup);
                        popup.setOnMouseClicked(ev -> rootStack.getChildren().remove(popup));
                    }
                    case "Instructions" -> {
                        Label inst = new Label(
                                "CONTROLS:\n\n" +
                                        "← / → : Move Left/Right\n" +
                                        "↑ : Rotate Piece\n" +
                                        "↓ : Soft Drop\n" +
                                        "SPACE : Hard Drop\n" +
                                        "C : Hold Piece\n" +
                                        "P : Pause/Resume\n" +
                                        "M : Back to Menu (when paused)\n\n" +
                                        "Click anywhere to close"
                        );
                        inst.setStyle(
                                "-fx-font-size: 18px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-text-fill: #00FFFF;" +
                                        "-fx-background-color: rgba(0, 0, 0, 0.9);" +
                                        "-fx-padding: 25;" +
                                        "-fx-border-color: #00FFFF;" +
                                        "-fx-border-width: 2;" +
                                        "-fx-border-radius: 10;" +
                                        "-fx-background-radius: 10;" +
                                        "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.6), 15, 0.6, 0, 0);" +
                                        "-fx-alignment: center;"
                        );
                        StackPane popup = new StackPane(inst);
                        popup.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
                        rootStack.getChildren().add(popup);
                        popup.setOnMouseClicked(ev -> rootStack.getChildren().remove(popup));
                    }
                }
            });
            menuBox.getChildren().add(b);
        }

        VBox fullMenuBox = new VBox(30);
        fullMenuBox.setAlignment(Pos.CENTER);
        fullMenuBox.getChildren().addAll(titleLabel, menuBox);

        menuContainer.getChildren().add(fullMenuBox);
        rootStack.getChildren().add(menuContainer);

        primaryStage.setTitle("Neon Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
