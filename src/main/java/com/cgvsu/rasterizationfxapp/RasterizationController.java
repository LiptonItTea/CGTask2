package com.cgvsu.rasterizationfxapp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
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
                Rasterization.drawLineVu(canvas.getGraphicsContext2D().getPixelWriter(), 400, 400, mouseEvent.getX(), mouseEvent.getY(), Color.BLACK);
            }
        });

        double x1 = 400;
        double y1 = 400;
        double len = 200;

        int amount = 200;
        double deltaTheta = 2 * Math.PI / amount;

        for (int i = 0; i < amount; i++){
            double theta = i * deltaTheta;
            double x2 = x1 + Math.cos(theta) * len;
            double y2 = y1 + Math.sin(theta) * len;

            Rasterization.drawLineVu(canvas.getGraphicsContext2D().getPixelWriter(), x1, y1, x2, y2, Color.BLACK);
        }
    }

}