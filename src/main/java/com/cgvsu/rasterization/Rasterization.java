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
//        int dir = (gradient > 0) ? 1 : -1;
        int dir = 1;

        double xend = Math.round(x1);
        double yend = Math.round(y1) + gradient * (xend - x1);
        double xgap = 1 - floatPart(x1 + 0.5);
        int xpxl1 = (int) xend;
        int ypxl1 = (int) yend;
        if(steep) {
            pixelWriter.setColor(ypxl1, xpxl1, getColorWithIntensity(color, pixelReader.getColor(ypxl1, xpxl1), (floatPart(yend)) * xgap));
            pixelWriter.setColor(ypxl1 - dir, xpxl1, getColorWithIntensity(color, pixelReader.getColor(ypxl1 - dir, xpxl1), (1 - floatPart(yend)) * xgap));
        }
        else {
            pixelWriter.setColor(xpxl1, ypxl1, getColorWithIntensity(color, pixelReader.getColor(xpxl1, ypxl1), (floatPart(yend)) * xgap));
            pixelWriter.setColor(xpxl1, ypxl1 - dir, getColorWithIntensity(color, pixelReader.getColor(xpxl1, ypxl1 - dir), (1 - floatPart(yend)) * xgap));
        }
        double intery = ypxl1 + gradient;

        xend = Math.round(x2);
        yend = Math.round(y2) + gradient * (xend - x2);
        xgap = floatPart(x1 + 0.5);
        int xpxl2 = (int) xend;
        int ypxl2 = (int) yend;
        if(steep) {
            pixelWriter.setColor(ypxl2, xpxl2, getColorWithIntensity(color, pixelReader.getColor(ypxl2, xpxl2), (floatPart(yend)) * xgap));
            pixelWriter.setColor(ypxl2 - dir, xpxl2, getColorWithIntensity(color, pixelReader.getColor(ypxl2 - dir, xpxl2), (1 - floatPart(yend)) * xgap));
        }
        else {
            pixelWriter.setColor(xpxl2, ypxl2, getColorWithIntensity(color, pixelReader.getColor(xpxl2, ypxl2), (floatPart(yend)) * xgap));
            pixelWriter.setColor(xpxl2, ypxl2 - dir, getColorWithIntensity(color, pixelReader.getColor(xpxl2, ypxl2 - dir), (1 - floatPart(yend)) * xgap));
        }

//        gradient = Math.abs(gradient);
        System.out.printf("%d %d %d %f%n", xpxl1, xpxl2, (int) intery, gradient);
        if(steep) {
            for (int x = xpxl1; x <= xpxl2; x++){
                pixelWriter.setColor((int) intery, x, getColorWithIntensity(color, pixelReader.getColor((int) intery, x), floatPart(intery)));
                pixelWriter.setColor((int) (intery - dir), x, getColorWithIntensity(color, pixelReader.getColor((int) (intery - dir), x), 1 - floatPart(intery)));

                intery += gradient;
//                error += gradient;
//                if(error >= 1.0){
//                    error -= 1.0;
//                    intery += dir;
//                }
            }
        }
        else {
            for (int x = xpxl1; x <= xpxl2; x++){
                pixelWriter.setColor(x, (int) intery, getColorWithIntensity(color, pixelReader.getColor(x, (int) intery), floatPart(intery)));
                pixelWriter.setColor(x, (int) (intery - dir), getColorWithIntensity(color, pixelReader.getColor(x, (int) (intery - dir)), 1 - floatPart(intery)));

                intery += gradient;
//                error += gradient;
//                if(error >= 1.0){
//                    error -= 1.0;
//                    intery += dir;
//                }
            }
        }
//        double A = y2 - y1;
//        double B = -(x2 - x1);
//        double C = -(y2 - y1) * x1 + (x2 - x1) * y1;
//        double sqrtAB = Math.sqrt(A * A + B * B);
//
//        int dx = Math.abs(x2 - x1);
//        int dy = Math.abs(y2 - y1);
//
//        int currY = y1;
//
//        int dir = y2 - y1;
//        if(dir > 0)
//            dir = 1;
//        else if(dir < 0)
//            dir = -1;
//
//        int deltaerr = dy + 1;
//        int error = 0;
//        if (steep) {
//            for (int x = x1; x <= x2; x++){
//                double gap = Math.abs(A * x + B * currY + C) / sqrtAB;
//                double gap2 = Math.abs(A * x + B * (currY + dir) + C) / sqrtAB;
////                pixelWriter.setColor(currY, x, getColorWithIntensity(color, pixelReader.getColor(currY, x), 1.0));
//
//                pixelWriter.setColor(currY, x, getColorWithIntensity(color, pixelReader.getColor(currY, x), 1 - gap / (gap + gap2)));
//                pixelWriter.setColor(currY + dir, x, getColorWithIntensity(color, pixelReader.getColor(currY - 1, x), 1 - gap2 / (gap + gap2)));
//
//                error += deltaerr;
//                if(error >= (dx + 1)){
//                    error -= dx + 1;
//                    currY += dir;
//                }
//            }
//        }
//        else {
//            for (int x = x1; x <= x2; x++){
//                double gap = Math.abs(A * x + B * currY + C) / sqrtAB;
//                double gap2 = Math.abs(A * x + B * (currY + dir) + C) / sqrtAB;
////                pixelWriter.setColor(x, currY, getColorWithIntensity(color, pixelReader.getColor(x, currY), 1.0));
//
//                pixelWriter.setColor(x, currY, getColorWithIntensity(color, pixelReader.getColor(x, currY), 1 - gap / (gap + gap2)));
//                pixelWriter.setColor(x, currY + dir, getColorWithIntensity(color, pixelReader.getColor(x, currY - 1), 1 - gap2 / (gap + gap2)));
//
//                error += deltaerr;
//                if(error >= (dx + 1)){
//                    error -= dx + 1;
//                    currY += dir;
//                }
//            }
//        }
    }
}
