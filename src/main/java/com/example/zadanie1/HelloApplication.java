package com.example.zadanie1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        String imagePath = "C:\\Users\\Chimi\\Desktop\\PB SEM VI\\Podstawy Biometrii\\Zadanie1\\image1.jpg";



        InputStream stream = new FileInputStream(imagePath);
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setFitHeight(575);
        imageView.setPreserveRatio(true);
        Group root = new Group(imageView);
        Scene scene = new Scene(root, 595, 675);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}