package io.deeplay.reversi.GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Реверси");
        primaryStage.setHeight(700);
        primaryStage.setWidth(700);
        primaryStage.setMaxWidth(700);
        primaryStage.setMaxHeight(700);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);

        InputStream iconStream = getClass().getResourceAsStream("icon.png");
        Image image = new Image(iconStream);
        primaryStage.getIcons().add(image);

        Label helloWorldLabel = new Label("Hello world!");
        helloWorldLabel.setAlignment(Pos.CENTER);
        Scene primaryScene = new Scene(helloWorldLabel);
        primaryStage.setScene(primaryScene);

        primaryStage.show();

//        BorderPane border = new BorderPane();
//        HBox control = new HBox();
//        control.setPrefHeight(40);
//        control.getSpacing();
//        control.setAlignment(Pos.BASELINE_CENTER);
//        Button start = new Button("Начать");
//        start.setOnMouseClicked(
//                event -> {
//                    border.setCenter(this.buildGrid());
//                }
//        );
//        control.getChildren().addAll(start);
//        border.setBottom(control);
//        border.setCenter(this.buildGrid());
//        primaryStage.setScene(new Scene(border, 700,700));
//        primaryStage.setTitle("Реверси");
//        primaryStage.setResizable(false);
//        primaryStage.show();
//    }
//
//    private Group buildGrid() {
//        Group panel = new Group();
//        for (int i = 0; i <8 ; i++) {
//            for (int j = 0; j < 8 ; j++) {
//                panel.getChildren().add(
//                  new Rectangle(i*40, j*40, 40, 40)
//                );
//            }
//        }
//        return panel;
    }
}