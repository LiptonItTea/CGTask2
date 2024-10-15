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

    private static Color getColorWithIntensity(Color color, double intensity){
        return Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness(), intensity);
    }

    private static double floatPart(double num){
        return num % 1;
    }

    private static void drawHorizontalLine(
            final GraphicsContext graphicsContext,
            int x1, int y, int x2,
            final Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        if(x1 > x2){
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }

        for (int x = x1; x <= x2; x++){
            pixelWriter.setColor(x, y, color);
        }
    }

    private static void drawVerticalLine(
            final GraphicsContext graphicsContext,
            int x, int y1, int y2,
            final Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        if(y1 > y2){
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }

        for (int y = y1; y <= y2; y++){
            pixelWriter.setColor(x, y, color);
        }
    }

    public static void drawLineVu(
            final GraphicsContext graphicsContext,
            double x1, double y1,
            double x2, double y2,
            final Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int startX = (int) x1;
        int startY = (int) y1;
        int endX = (int) x2;
        int endY = (int) y2;

        if (startX == endX) { // vertical line
            drawVerticalLine(graphicsContext, startX, startY, endY, color);
            return;
        }

        if (startY == endY) { // horizontal line
            drawHorizontalLine(graphicsContext, startX, startY, endX, color);
            return;
        }

        if(x1 > x2){ // swap
            double temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;

            startX = (int) x1;
            startY = (int) y1;
            endX = (int) x2;
            endY = (int) y2;
        }

        double dx = x2 - x1;
        double dy = y2 - y1;
        if(Math.abs(dx) > Math.abs(dy)){ // primary axis - Ox
            // already swapped coords so don't do it here.
            double k = dy / dx;

            double currY = y1;
            for (int currX = startX; currX <= endX; currX++){
                double intensity = floatPart(currY);

                pixelWriter.setColor(currX, (int) currY, getColorWithIntensity(color, 1 - intensity));
                pixelWriter.setColor(currX, (int) currY + 1, getColorWithIntensity(color, intensity));

                currY += k;
            }
        }
        else{ // primary axis - Oy
            // coords swapped by x, need to swap by y
            if(y1 > y2){ // swap
                double temp = x1;
                x1 = x2;
                x2 = temp;

                temp = y1;
                y1 = y2;
                y2 = temp;

                startX = (int) x1;
                startY = (int) y1;
                endX = (int) x2;
                endY = (int) y2;
            }
            double k = dx / dy;

            double currX = x1;
            for (int currY = startY; currY <= endY; currY++){
                double intensity = floatPart(currX);

                pixelWriter.setColor((int) currX, currY, getColorWithIntensity(color, 1 - intensity));
                pixelWriter.setColor((int) currX + 1, currY, getColorWithIntensity(color, intensity));

                currX += k;
            }
        }
    }
}
