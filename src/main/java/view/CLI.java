package view;

import controller.Controller;
import model.ColorCode;
import model.FractalGenerator;
import model.FractalType;
import model.JuliaGenerator;

import java.util.Scanner;

// TODO doc
public class CLI {

    public static void askArgsAndRun(Scanner sc) {
        // TODO catch all possible mistakes from user
        System.out.println("Type of fractal ? Possible answers : Julia "); // todo fix when adding Mandelbrot
        FractalType type = findFractalType(sc.next());
        System.out.println("width of picture ? ");
        int width = Integer.parseInt(sc.next());
        System.out.println("height of picture ? ");
        int height = Integer.parseInt(sc.next());
        System.out.println("starting x coordinate ? ");
        double startX = Double.parseDouble(sc.next());
        System.out.println("starting y coordinate ? ");
        double startY = Double.parseDouble(sc.next());
        System.out.println("ending x coordinate ? ");
        double endX = Double.parseDouble(sc.next());
        System.out.println("ending y coordinate ? ");
        double endY = Double.parseDouble(sc.next());
        System.out.println("color code ? Possible values : HUE1, HUE2, HUE3"); // TODO fix
        ColorCode.Code code = ColorCode.fromString(sc.next());

        if (type == FractalType.JULIA) {
            System.out.println("real part of the julia constant ? ");
            double constR = Double.parseDouble(sc.next());
            System.out.println("imaginary part of the julia constant ? ");
            double constI = Double.parseDouble(sc.next());

            // TODO : let the CLI user choose filename

            FractalGenerator generator = JuliaGenerator.Builder.newInstance().width(width).height(height)
                    .polynomeConstant(constR, constI)
                    .range(startX, startY, endX-startX, endY-startY).build();
            Controller c = new Controller(generator);
            c.saveImage(true, "test.png", code);
        }
    }

    private static FractalType findFractalType(String name) {
        if (name.equals("Julia")) {
            return FractalType.JULIA;
        } else if (name.equals("Mandelbrot")) {
            return FractalType.MANDELBROT;
        } else {
            System.out.println("Illegal argument : " + name);
            System.out.println("Run with option `-h` to see help");
            System.exit(1);
        }
        return null;
    }
}
