package tetris.group__25;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

        // Main Menu overlay
        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle(
                "-fx-background-color: rgba(0,0,0,0.7);" +
                        "-fx-padding: 30;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;"
        );
        String btnStyle =
                "-fx-font-size: 28;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: rgba(0,0,0,0.7);" +
                        "-fx-padding: 20;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;";

        for (String text : List.of("Start Game", "Scores", "Instructions", "Quit")) {
            Button b = new Button(text);
            b.setStyle(btnStyle);
            b.setOnAction(ae -> {
                switch (text) {
                    case "Start Game" -> {
                        rootStack.getChildren().remove(menuBox);
                        engine.start();
                    }
                    case "Quit" -> primaryStage.close();
                    case "Scores" -> {
                        // Simple popup
                        Label hs = new Label("High Score: " + engine.getHighScore());
                        hs.setStyle(btnStyle);
                        StackPane popup = new StackPane(hs);
                        popup.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
                        rootStack.getChildren().add(popup);
                        popup.setOnMouseClicked(ev -> rootStack.getChildren().remove(popup));
                    }
                    case "Instructions" -> {
                        Label inst = new Label(
                                "Controls:\n" +
                                        "←/→ Move\n" +
                                        "Up  Rotate\n" +
                                        "Down Soft Drop\n" +
                                        "Space Hard Drop\n" +
                                        "C Hold\n" +
                                        "P Pause/Resume"
                        );
                        inst.setStyle(btnStyle);
                        StackPane popup = new StackPane(inst);
                        popup.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
                        rootStack.getChildren().add(popup);
                        popup.setOnMouseClicked(ev -> rootStack.getChildren().remove(popup));
                    }
                }
            });
            menuBox.getChildren().add(b);
        }
        rootStack.getChildren().add(menuBox);

        primaryStage.setTitle("Neon Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
