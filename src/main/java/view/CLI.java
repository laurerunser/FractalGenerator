package view;

import controller.Controller;
import model.ColorCode;
import model.FractalGenerator;
import model.FractalType;

import java.util.Locale;
import java.util.Scanner;

// TODO doc
public class CLI {

    public static void askArgsAndRun(Scanner sc) {
        // TODO catch all possible mistakes from user
        System.out.println("Type of fractal ? Possible answers : Julia Mandelbrot "); // todo fix when adding Mandelbrot
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

        // TODO : let the CLI user choose filename

        if (type == FractalType.JULIA) {
            System.out.println("real part of the julia constant ? ");
            double constR = Double.parseDouble(sc.next());
            System.out.println("imaginary part of the julia constant ? ");
            double constI = Double.parseDouble(sc.next());


            FractalGenerator generator = FractalGenerator.Builder.newInstance().type(FractalType.JULIA).width(width).height(height)
                    .polynomeConstant(constR, constI)
                    .range(startX, startY, endX - startX, endY - startY).build();
            Controller c = new Controller(generator);
            c.saveImage(true, "test.png", code);
        }else if (type == FractalType.MANDELBROT){

            FractalGenerator generator = FractalGenerator.Builder.newInstance().type(FractalType.MANDELBROT).width(width).height(height)
                    .range(startX, startY, endX - startX, endY - startY).build();
            Controller c = new Controller(generator);
            c.saveImage(true, "test.png", code);

        }
    }

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
