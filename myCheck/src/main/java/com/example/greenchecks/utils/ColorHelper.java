package com.example.greenchecks.utils;


import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ColorHelper {

    /*
Calculates the estimated brightness of an Android Bitmap.
pixelSpacing tells how many pixels to skip each pixel. Higher values result in better performance, but a more rough estimate.
When pixelSpacing = 1, the method actually calculates the real average brightness, not an estimate.
This is what the calculateBrightness() shorthand is for.
Do not use values for pixelSpacing that are smaller than 1.
*/
    /*
    public int calculateBrightnessEstimate(android.graphics.Bitmap bitmap, int pixelSpacing) {
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return (R + B + G) / (n * 3);
    }

    public int calculateBrightness(android.graphics.Bitmap bitmap) {
        calculateBrightnessEstimate(bitmap, 1);
    }
    */

    public static double brightness(BufferedImage image) {
        Raster raster = image.getRaster();
        double sum = 0.0;

        for (int y = 0; y < image.getHeight(); ++y){
            for (int x = 0; x < image.getWidth(); ++x){
                sum += raster.getSample(x, y, 0);
            }
        }
        return sum / (image.getWidth() * image.getHeight());
    }
}
