package com.cgvsu.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
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

    private static Color getColorWithIntensity(Color startColor, Color endColor, double k, Color bgColor, double intensity){
        double red = startColor.getRed() + k * (endColor.getRed() - startColor.getRed());
        double green = startColor.getGreen() + k * (endColor.getGreen() - startColor.getGreen());
        double blue = startColor.getBlue() + k * (endColor.getBlue() - startColor.getBlue());

//        red = Math.min(red, bgColor.getRed());
//        green = Math.min(green, bgColor.getGreen());
//        blue = Math.min(blue, bgColor.getBlue());

        int ir = (int) (red * 255);
        int ig = (int) (green * 255);
        int ib = (int) (blue * 255);
        double newIntensity = Math.max(bgColor.getOpacity(), intensity);

        return Color.rgb(ir, ig, ib, newIntensity);
    }

    private static double floatPart(double num){
        return num % 1;
    }

    private static double interpolationCoefficient(double x, double y, double x1, double y1, double x2, double y2, boolean invert){
        double result = (x - x1) * (x - x1) + (y - y1) * (y - y1);
        result /= (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        result = Math.sqrt(result);
        if(invert)
            result = 1.0 - result;
        return result;
    }

    public static void drawLineVu(
            final PixelWriter pixelWriter,
            final PixelReader pixelReader,
            double x1, double y1,
            double x2, double y2,
            final Color startColor,
            final Color endColor) {
        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
        boolean swap = false;
        if(steep) {
            double temp = x1;
            x1 = y1;
            y1 = temp;

            temp = x2;
            x2 = y2;
            y2 = temp;
        }
        if(x1 > x2){
            swap = true;
            double temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        double dx = x2 - x1;
        double dy = y2 - y1;

        double gradient;
        if(dx == 0) {
            gradient = 1.0;
        }
        else {
            gradient = dy / dx;
        }

        double xend = x1;
        double yend = y1;
        int xpxl1 = (int) xend;
        int ypxl1 = (int) yend;
        double k = 0.0;
        if(swap)
            k = 1.0;
        if(steep) {
            pixelWriter.setColor(ypxl1, xpxl1, getColorWithIntensity(startColor, endColor, k, pixelReader.getColor(ypxl1, xpxl1), 1.0));
        }
        else {
            pixelWriter.setColor(xpxl1, ypxl1, getColorWithIntensity(startColor, endColor, k, pixelReader.getColor(xpxl1, ypxl1), 1.0));
        }
        double intery = y1;
        if(!steep)
            intery += gradient;

        xend = x2;
        yend = y2;
        int xpxl2 = (int) xend;
        int ypxl2 = (int) yend;
        k = 1.0;
        if(swap)
            k = 0.0;
        if(steep) {
            pixelWriter.setColor(ypxl2, xpxl2, getColorWithIntensity(startColor, endColor, k, pixelReader.getColor(ypxl2, xpxl2), 1.0));
        }
        else {
            pixelWriter.setColor(xpxl2, ypxl2, getColorWithIntensity(startColor, endColor, k, pixelReader.getColor(xpxl2, ypxl2), 1.0));
        }

        System.out.printf("%d %d %d %f%n", xpxl1, xpxl2, (int) intery, gradient);
        if(steep) {
            for (int x = xpxl1; x <= xpxl2 - 1; x++){
                k = interpolationCoefficient(x, intery, xpxl1, ypxl1, xpxl2, ypxl2, swap);
                pixelWriter.setColor((int) intery, x, getColorWithIntensity(startColor, endColor, k, pixelReader.getColor((int) intery, x), 1 - floatPart(intery)));
                pixelWriter.setColor((int) (intery + 1), x, getColorWithIntensity(startColor, endColor, k, pixelReader.getColor((int) (intery + 1), x), floatPart(intery)));

                intery += gradient;
            }
        }
        else {
            for (int x = xpxl1 + 1; x <= xpxl2; x++){
                k = interpolationCoefficient(x, intery, xpxl1, ypxl1, xpxl2, ypxl2, swap);
                pixelWriter.setColor(x, (int) intery, getColorWithIntensity(startColor, endColor, k, pixelReader.getColor(x, (int) intery), 1 - floatPart(intery)));
                pixelWriter.setColor(x, (int) (intery + 1), getColorWithIntensity(startColor, endColor, k, pixelReader.getColor(x, (int) (intery + 1)), floatPart(intery)));

                intery += gradient;
            }
        }
    }
}