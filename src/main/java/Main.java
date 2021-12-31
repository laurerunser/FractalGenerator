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
}

