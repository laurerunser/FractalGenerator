package model;

import java.awt.geom.Rectangle2D;
//TODO : doc
/**
 * This class generates the Mandelbrot set
 */
public class MandelbrotGenerator extends FractalGenerator {

    MandelbrotGenerator(int width, int height, int maxIter, Rectangle2D.Double range) {
        super(height, width, maxIter, range);
    }

    @Override
    public String toString() {
        return "Mandelbrot : \n" + super.toString();
    }

    int divergenceIndex(double x, double y) {
        int indice = 0;
        Complex cnst = new Complex(x, y);
        Complex var = new Complex(0, 0);
        while (indice < max_iter-1 && var.getAbs() <= RADIUS){
            var.mandel(cnst);
            indice++;
        }
        return indice;
    }
}