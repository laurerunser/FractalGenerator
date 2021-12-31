package view;

import controller.Controller;
import model.ColorCode;
import model.FractalGenerator;
import model.FractalType;

import java.util.Locale;
import java.util.Scanner;

// TODO : load from a file

/**
 * This is the main class to launch the CLI interface.
 */
public class CLI {

    /**
     * Launches the interactive command line.
     *
     * @param sc  The Scanner to read the answers from
     * @param ask If true, asks the questions on System.out; otherwise just reads the answers
     */
    public static void askArgsAndRun(Scanner sc, boolean ask) {
        // TODO catch all possible mistakes from user
        if (ask) System.out.println("Type of fractal ? Possible answers : Julia Mandelbrot ");
        FractalType type = findFractalType(sc.next());
        if (ask) System.out.println("width of picture ? ");
        int width = Integer.parseInt(sc.next());
        if (ask) System.out.println("height of picture ? ");
        int height = Integer.parseInt(sc.next());
        if (ask) System.out.println("starting x coordinate ? ");
        double startX = Double.parseDouble(sc.next());
        if (ask) System.out.println("starting y coordinate ? ");
        double startY = Double.parseDouble(sc.next());
        if (ask) System.out.println("ending x coordinate ? ");
        double endX = Double.parseDouble(sc.next());
        if (ask) System.out.println("ending y coordinate ? ");
        double endY = Double.parseDouble(sc.next());
        if (ask) System.out.println("color code ? Possible values : HUE1, HUE2, HUE3");
        ColorCode.Code code = ColorCode.fromString(sc.next());

        // TODO : let the CLI user choose filename

        if (type == FractalType.JULIA) {
            if (ask) System.out.println("real part of the julia constant ? ");
            double constR = Double.parseDouble(sc.next());
            if (ask) System.out.println("imaginary part of the julia constant ? ");
            double constI = Double.parseDouble(sc.next());

            FractalGenerator generator = FractalGenerator.Builder.newInstance().type(FractalType.JULIA).width(width).height(height)
                    .polynomeConstant(constR, constI)
                    .range(startX, startY, endX - startX, endY - startY).build();
            Controller c = new Controller(generator);
            c.saveImage(true, "test.png", code);

        } else if (type == FractalType.MANDELBROT) {
            FractalGenerator generator = FractalGenerator.Builder.newInstance().type(FractalType.MANDELBROT).width(width).height(height)
                    .range(startX, startY, endX - startX, endY - startY).build();
            Controller c = new Controller(generator);
            c.saveImage(true, "test.png", code);

        }
    }

    /**
     * Helper method to find the corresponding
     *
     * @param name the String
     * @return the FractalType that corresponds to the string
     */
    private static FractalType findFractalType(String name) {
        if (name.toLowerCase(Locale.ROOT).equals("julia")) {
            return FractalType.JULIA;
        } else if (name.toLowerCase(Locale.ROOT).equals("mandelbrot")) {
            return FractalType.MANDELBROT;
        } else {
            System.out.println("Illegal argument : " + name);
            System.out.println("Possible values are 'Julia' or 'Mandelbrot'");
            System.exit(1);
        }
        return null;
    }
}
