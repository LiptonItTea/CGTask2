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

    private static Color getColorWithIntensity(Color color, Color bgColor, double intensity){
//        double red = color.getRed() * bgColor.getRed();
//        double green = color.getGreen() * bgColor.getGreen();
//        double blue = color.getBlue() * bgColor.getBlue();
        double red = Math.min(color.getRed(), bgColor.getRed());
        double green = Math.min(color.getGreen(), bgColor.getGreen());
        double blue = Math.min(color.getBlue(), bgColor.getBlue());

        int ir = (int) (red * 255);
        int ig = (int) (green * 255);
        int ib = (int) (blue * 255);

        double newIntensity = Math.max(bgColor.getOpacity(), intensity);

        return Color.rgb(ir, ig, ib, newIntensity);
//        return Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness(), intensity);
    }

    private static double floatPart(double num){
        return num % 1;
    }

    public static void drawLineVu(
            final PixelWriter pixelWriter,
            final PixelReader pixelReader,
            double x1, double y1,
            double x2, double y2,
            final Color color) {
        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
        if(steep) {
            double temp = x1;
            x1 = y1;
            y1 = temp;

            temp = x2;
            x2 = y2;
            y2 = temp;
        }
        if(x1 > x2){
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
        if(steep) {
            pixelWriter.setColor(ypxl1, xpxl1, getColorWithIntensity(color, pixelReader.getColor(ypxl1, xpxl1), 1.0));
        }
        else {
            pixelWriter.setColor(xpxl1, ypxl1, getColorWithIntensity(color, pixelReader.getColor(xpxl1, ypxl1), 1.0));
        }
        double intery = y1;
        if(!steep)
            intery += gradient;

        xend = x2;
        yend = y2;
        int xpxl2 = (int) xend;
        int ypxl2 = (int) yend;
        if(steep) {
            pixelWriter.setColor(ypxl2, xpxl2, getColorWithIntensity(color, pixelReader.getColor(ypxl2, xpxl2), 1.0));
        }
        else {
            pixelWriter.setColor(xpxl2, ypxl2, getColorWithIntensity(color, pixelReader.getColor(xpxl2, ypxl2), 1.0));
        }

        System.out.printf("%d %d %d %f%n", xpxl1, xpxl2, (int) intery, gradient);
        if(steep) {
            for (int x = xpxl1; x <= xpxl2 - 1; x++){
                pixelWriter.setColor((int) intery, x, getColorWithIntensity(color, pixelReader.getColor((int) intery, x), 1 - floatPart(intery)));
                pixelWriter.setColor((int) (intery + 1), x, getColorWithIntensity(color, pixelReader.getColor((int) (intery + 1), x), floatPart(intery)));

                intery += gradient;
            }
        }
        else {
            for (int x = xpxl1 + 1; x <= xpxl2; x++){
                pixelWriter.setColor(x, (int) intery, getColorWithIntensity(color, pixelReader.getColor(x, (int) intery), 1 - floatPart(intery)));
                pixelWriter.setColor(x, (int) (intery + 1), getColorWithIntensity(color, pixelReader.getColor(x, (int) (intery + 1)), floatPart(intery)));

                intery += gradient;
            }
        }
    }
}