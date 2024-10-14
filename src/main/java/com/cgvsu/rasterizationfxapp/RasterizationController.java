package com.cgvsu.rasterizationfxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

//        Rasterization.drawRectangle(canvas.getGraphicsContext2D(), 200, 300, 200, 100, Color.CHOCOLATE);
//        Rasterization.drawRectangle(canvas.getGraphicsContext2D(), 250, 250, 50, 200, Color.AQUA);
        Rasterization.drawLineVu(canvas.getGraphicsContext2D(), 10, 10, 10, 50, Color.BLACK);
        Rasterization.drawLineVu(canvas.getGraphicsContext2D(), 10, 10, 50, 10, Color.BLACK);

        double x1 = 400;
        double y1 = 400;
        double len = 200;

        int amount = 50;
        double deltaTheta = 2 * Math.PI / amount;

        for (int i = 0; i < amount; i++){
            double theta = i * deltaTheta;
            double x2 = x1 + Math.cos(theta) * len;
            double y2 = y1 + Math.sin(theta) * len;

            Rasterization.drawLineVu(canvas.getGraphicsContext2D(), x1, y1, x2, y2, Color.BLACK);
        }
//        Rasterization.drawLineVu(canvas.getGraphicsContext2D(), 10, 10, 50, 20, Color.BLACK);
    }

}