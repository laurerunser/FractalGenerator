package model;

import java.awt.*;

public class ColorCode {
    public enum Code {
        HUE1,
        HUE2,
        HUE3,
    }

    public static ColorCode.Code fromString(String str) {
        return switch (str) {
            case "HUE1" -> Code.HUE1;
            case "HUE2" -> Code.HUE2;
            case "HUE3" -> Code.HUE3;
            default -> throw new IllegalArgumentException("Color code doesn't exist");
        };
    }

    public static int getColor(double indice, int max_iter, Code code) {
        if (indice == max_iter - 1) return Color.black.getRGB();
        return switch (code) {
            case HUE1 -> getHue(indice, max_iter);
            case HUE2 -> getHue2(indice);
            case HUE3 -> getHue3(indice);
        };
    }

    private static int getHue(double indice, int max_iter) {
        return Color.HSBtoRGB((float)indice/max_iter, 0.7f, 0.7f);
    }

    private static int getHue2(double indice) {
        return Color.HSBtoRGB((float) indice / 100.0F, 1F, 1F);
    }

    private static int getHue3(double indice) {
        float hue = 0.7f + (float) indice / 200f;
        return Color.HSBtoRGB(hue, 1f, 1f);
    }
}
