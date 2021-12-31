package model;

import java.awt.geom.Rectangle2D;
//TODO : doc
/**
 * This class generates Julia sets
 */
public class JuliaGenerator extends FractalGenerator {

    JuliaGenerator(Polynome poly, int width, int height, int maxIter, Rectangle2D.Double range) {
        super(height, width, maxIter, range);
        this.polynom = poly;
    }

    @Override
    public String toString() {
        return "Julia : \n" + super.toString() + "\npolynome : " + polynom.toString();
    }

    int divergenceIndex(double x, double y) {
        int indice = 0;
        Complex zn = new Complex(x, y);
        while (indice < max_iter - 1 && zn.getAbs() <= RADIUS) {
            zn = polynom.apply(zn);
            indice++;
        }
        return indice;
    }
}