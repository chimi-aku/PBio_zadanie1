package com.example.zadanie1;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.*;

public class HelloApplication extends Application {

    public static int histogramRed[] = new int[256];
    public static int histogramGreen[] = new int[256];
    public static int histogramBlue[] = new int[256];
    public static int histogramAvg[] = new int[256];

    String imagePath = "C:\\Users\\Chimi\\Desktop\\PB SEM VI\\Podstawy Biometrii\\Zadanie1\\image2.jpg";


    @Override
    public void start(Stage stage) throws IOException {





        File file = new File(imagePath);

        Button loadBtn = new Button("Load");
        loadBtn.setMinWidth(100);
        loadBtn.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));

                    File selectedFile = fileChooser.showOpenDialog(stage);

                    System.out.println(selectedFile);
                }
        );

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(loadBtn);


        // Buffor Image

        InputStream stream = new FileInputStream(imagePath);
        BufferedImage img = ImageIO.read(file);

        BufferedImage binaryImage = convertToBinarization(img);
        Image convertedImage = convertToFxImage(binaryImage);


        Image orginalImage = new Image(stream);
        ImageView orginalImageView = new ImageView();
        orginalImageView.setImage(orginalImage);
        orginalImageView.setX(10);
        orginalImageView.setY(10);
        orginalImageView.setFitHeight(300);
        orginalImageView.setFitWidth(300);
        orginalImageView.setPreserveRatio(true);


        ImageView convertedImageView = new ImageView();
        convertedImageView.setImage(convertedImage);
        convertedImageView.setX(10);
        convertedImageView.setY(380);
        convertedImageView.setFitHeight(300);
        convertedImageView.setFitWidth(300);


        convertedImageView.setPreserveRatio(true);


        VBox ImageBox = new VBox();
        ImageBox.getChildren().addAll(orginalImageView, convertedImageView);

        // Histogram display
        // Defining Axis
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> histogramChart = new LineChart<>(xAxis, yAxis);
        histogramChart.setCreateSymbols(false);


        // Defining Series
        XYChart.Series seriesRed = new XYChart.Series<>();
        XYChart.Series seriesGreen = new XYChart.Series<>();
        XYChart.Series seriesBlue= new XYChart.Series<>();
        XYChart.Series seriesAvg = new XYChart.Series<>();

        // Setting Series Labels
        seriesRed.setName("red");
        seriesGreen.setName("green");
        seriesBlue.setName("blue");
        seriesAvg.setName("average");

        // Setting Series Data
        for(int i = 0; i < 256; i++) {
            seriesRed.getData().add(new XYChart.Data(i, histogramRed[i]));
            seriesGreen.getData().add(new XYChart.Data(i, histogramGreen[i]));
            seriesBlue.getData().add(new XYChart.Data(i, histogramBlue[i]));
            seriesAvg.getData().add(new XYChart.Data(i, histogramAvg[i]));
        }

        histogramChart.getData().addAll(seriesRed, seriesAvg, seriesGreen, seriesBlue );


        HBox hBox = new HBox();
        hBox.getChildren().addAll(ImageBox, histogramChart, buttonBox);

        Group root = new Group(hBox);
        Scene scene = new Scene(root, 1295, 800);

        stage.setTitle("Binarization And Histogram");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    private static BufferedImage convertToBinarization(BufferedImage image) {
        // Get the width of the image.
        int width = image.getWidth();
        // Get the height of the image.
        int height = image.getHeight();
        // Establish a 2D array to keep image data.
        int[][] result = new int[width][height];
        // The height of the image.
        for (int row = 0; row < width; row++) {
            // The width of the image.
            for (int col = 0; col < height; col++) {
                // Acquire RGB value.
                result[row][col] = image.getRGB(row, col);
                // Acqurie integer type of RGB value.
                int iRet = result[row][col];
                // The red variable.
                int iR = 0;
                // The green variable.
                int iG = 0;
                // The bule variable.
                int iB = 0;
                // The average variable
                int iAvg = 0;




                // Get blue pixel of the blue channel.
                iB = ((int) iRet & 0xff);
                histogramBlue[iB]++;
                // Get green pixel of the green channel.
                iG = (((int) iRet & 0x00ff00) >> 8);
                histogramGreen[iG]++;
                // Get red pixel of the red channel.
                iR = (((int) iRet & 0xff0000) >> 16);
                histogramRed[iR]++;
                // Transform RGB color space to gray scale.
                iAvg = ( iR + iG + iB ) / 3;
                histogramAvg[iAvg]++;
                // This is our threshold, you can change it.
                if ( iAvg > 120 )
                {
                    // This is white pixel.
                    iRet = 0xffffff;
                    // Set the white pixel into the image.
                    image.setRGB(row, col, iRet);
                }
                else
                {
                    // Set the black pixel into the image.
                    image.setRGB(row, col, 0);
                }
            }
        }
        // return result value.
        return image;
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }
}


