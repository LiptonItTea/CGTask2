package com.cgvsu.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {

    public static void drawRectangle(
            final GraphicsContext graphicsContext,
            final int x, final int y,
            final int width, final int height,
            final Color color)
    {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        for (int row = y; row < y + height; ++row)
            for (int col = x; col < x + width; ++col)
                pixelWriter.setColor(col, row, color);
    }

    public static void drawLineVu(
            final GraphicsContext graphicsContext,
            final int x1, final int y1,
            final int x2, final int y2,
            final Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        if (x1 == x2) { // vertical line
            for (int i = y1; i <= y2; i++){
                pixelWriter.setColor(x1, i, color);
            }
            return;
        }

        if (y1 == y2) { // horizontal line
            for (int i = x1; i <= x2; i++) {
                pixelWriter.setColor(i, y1, color);
            }
            return;
        }
    }
}
