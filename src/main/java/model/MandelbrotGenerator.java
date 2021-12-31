package model;

import java.awt.geom.Rectangle2D;

/**
 * This class generates the Mandelbrot set
 */
public class MandelbrotGenerator extends FractalGenerator {

    /**
     * Creates a new MandelbrotGenerator
     *
     * @param width   the width of the final picture
     * @param height  the height of the final picture
     * @param maxIter the maximum number of iterations
     * @param range   the range of the complex plane to consider
     */
    MandelbrotGenerator(int width, int height, int maxIter, Rectangle2D.Double range) {
        super(height, width, maxIter, range);
    }

    @Override
    public String toString() {
        return "Mandelbrot : \n" + super.toString();
    }

    protected int divergenceIndex(double x, double y) {
        int indice = 0;
        Complex cnst = new Complex(x, y);
        Complex var = new Complex(0, 0);
        while (indice < max_iter - 1 && var.getAbs() <= RADIUS) {
            var.mandel(cnst);
            indice++;
        }
        return indice;
    }
}