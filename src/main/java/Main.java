import controller.Controller;
import view.CLI;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter(System.lineSeparator());
        System.out.println("Do you want the 'gui' or 'cli' version ? ");
        String input = sc.next();

        if (input.toLowerCase(Locale.ROOT).equals("cli")) {
            CLI.askArgsAndRun(sc, true);
        } else {
            Controller c = new Controller();
        }
    }

//    private static void help() { //TODO : fix help
//        System.out.println("To run the GUI version of this program, run it with the option `gui`");
//        System.out.println("To run the CLI version of this program, run it with option `cli`");
//        System.out.println("./program type width height startR startI endR endI hue [constR constI]");
//        System.out.println("where :");
//        System.out.println("type : is the type of fractal to draw. Currently supported : Julia, Mandlebrot");
//        System.out.println("width, height : the width and height of the final picture (in pixels)");
//        System.out.println("startR, startI : the starting point in the complexe plane");
//        System.out.println("endR, endI : the ending point in the complexe plane");
//        System.out.println("hue : the color code for the fractal"); // TODO : specify accepted values
//        System.out.println("constR, constI : only needed for the Julia set. This is the constant of the polynom to draw");
//        System.out.println("For example, to have a picture 800x900px of a Julia set with the constant -3+5i that covers" +
//                "the rectangle in the complexe plane with bottom-left corner -1-i and upper-right corner 2+4i :");
//        System.out.println("you need to type : ./program Julia 800 900 -1 -1 2 4 -3 5");
//    }
}

