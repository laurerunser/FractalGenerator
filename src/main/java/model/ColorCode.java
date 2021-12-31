package model;

import java.awt.*;

/**
 * This class modelises the various color codes
 */
public class ColorCode {

    /**
     * @param str a string with the name of the color code
     * @return the Code that corresponds
     */
    public static ColorCode.Code fromString(String str) {
        return switch (str) {
            case "HUE1" -> Code.HUE1;
            case "HUE2" -> Code.HUE2;
            case "HUE3" -> Code.HUE3;
            default -> throw new IllegalArgumentException("Color code doesn't exist");
        };
    }

    /**
     * @param indice   the divergence index
     * @param max_iter the maximum number of iterations
     * @param code     the color code
     * @return the rgb color
     */
    public static int getColor(double indice, int max_iter, Code code) {
        if (indice == max_iter - 1) return Color.black.getRGB();
        return switch (code) {
            case HUE1 -> getHue(indice, max_iter);
            case HUE2 -> getHue2(indice);
            case HUE3 -> getHue3(indice);
        };
    }

    /**
     * @param indice   the divergence index
     * @param max_iter the maximum number of iterations
     * @return the rgb color
     */
    private static int getHue(double indice, int max_iter) {
        return Color.HSBtoRGB((float) indice / max_iter, 0.7f, 0.7f);
    }

    /**
     * @param indice the divergence index
     * @return the rgb color
     */
    private static int getHue2(double indice) {
        return Color.HSBtoRGB((float) indice / 100.0F, 1F, 1F);
    }

    /**
     * @param indice the divergence index
     * @return the rgb color
     */
    private static int getHue3(double indice) {
        float hue = 0.7f + (float) indice / 200f;
        return Color.HSBtoRGB(hue, 1f, 1f);
    }

    /**
     * The enum of all available color codes
     */
    public enum Code {
        HUE1,
        HUE2,
        HUE3,
    }
}
