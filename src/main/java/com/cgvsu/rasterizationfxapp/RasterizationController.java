package com.cgvsu.rasterizationfxapp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import com.cgvsu.rasterization.*;
import javafx.scene.paint.Color;

public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                WritableImage image = new WritableImage(800, 600);
                PixelWriter pixelWriter = image.getPixelWriter();
                PixelReader pixelReader = image.getPixelReader();
                Rasterization.drawLineVu(pixelWriter, pixelReader, 400, 300, (int) mouseEvent.getX(), (int) mouseEvent.getY(), Color.BLACK);
                canvas.getGraphicsContext2D().drawImage(image, 0, 0);
            }
        });
        WritableImage image = new WritableImage(800, 600);
        PixelWriter pixelWriter = image.getPixelWriter();
        Color white = Color.color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 0);
        for (int i = 0; i < 800; i++){
            for (int j = 0; j < 600; j++){
                pixelWriter.setColor(i, j, white);
            }
        }
        PixelReader pixelReader = image.getPixelReader();

        double x1 = 400;
        double y1 = 300;
        double len = 200;

        int amount = 200;
        double deltaTheta = 2 * Math.PI / amount;

        for (int i = 0; i < amount; i++){
            double theta = i * deltaTheta;
            double x2 = x1 + Math.cos(theta) * len;
            double y2 = y1 + Math.sin(theta) * len;

            Rasterization.drawLineVu(pixelWriter, pixelReader, (int) x1, (int) y1, (int) x2, (int) y2, Color.BLACK);
        }

        canvas.getGraphicsContext2D().drawImage(image, 0, 0);
    }

}