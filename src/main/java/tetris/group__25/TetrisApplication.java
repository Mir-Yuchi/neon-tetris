package tetris.group__25;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import tetris.group__25.engine.GameEngine;
import tetris.group__25.render.Renderer;

public class TetrisApplication extends Application {
    // Main application class for the Tetris game
    @Override
    public void start(Stage primaryStage) {
        Rectangle background = new Rectangle();
        background.setFill(Color.BLACK);

        // Create the border pane for UI components
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        // Game board
        Pane boardPane = new Pane();
        boardPane.setStyle("-fx-background-color: transparent;");

        // Hold area
        VBox holdArea = new VBox(10);
        Label holdLabel = new Label("HOLD");
        Pane holdPane = new Pane();
        holdPane.setPrefSize(120, 120);
        holdPane.setStyle("-fx-background-color: transparent;");
        holdArea.getChildren().addAll(holdLabel, holdPane);
        holdArea.setAlignment(Pos.CENTER);

        // Next pieces area
        VBox nextArea = new VBox(10);
        Label nextLabel = new Label("NEXT");
        Pane nextPane = new Pane();
        nextPane.setPrefSize(120, 120);
        nextPane.setStyle("-fx-background-color: transparent;");
        nextArea.getChildren().addAll(nextLabel, nextPane);
        nextArea.setAlignment(Pos.CENTER);

        // Stats area
        HBox statsArea = new HBox(30);
        Label scoreLabel = new Label("Score: 0");
        Label levelLabel = new Label("Level: 0");
        Label linesLabel = new Label("Lines: 0");
        Label highScoreLabel = new Label("High Score: 0");
        statsArea.getChildren().addAll(scoreLabel, levelLabel, linesLabel, highScoreLabel);
        statsArea.setAlignment(Pos.CENTER);
        statsArea.setStyle("-fx-background-color: transparent;");

        // Set up the border pane
        borderPane.setCenter(boardPane);
        borderPane.setLeft(holdArea);
        borderPane.setRight(nextArea);
        borderPane.setBottom(statsArea);

        // Create the root stack pane
        StackPane rootStack = new StackPane();
        rootStack.getChildren().add(background); // Background layer
        rootStack.getChildren().add(borderPane); // UI layer

        // Bind background size to root
        background.widthProperty().bind(rootStack.widthProperty());
        background.heightProperty().bind(rootStack.heightProperty());

        // Set up the scene
        Scene scene = new Scene(rootStack, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Initialize game components
        Renderer renderer = new Renderer(boardPane, holdPane, nextPane, background, rootStack,
                scoreLabel, levelLabel, linesLabel, highScoreLabel);
        GameEngine engine = new GameEngine(scene, renderer);

        primaryStage.setTitle("Neon Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();

        engine.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}