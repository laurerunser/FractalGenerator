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

    protected int divergenceIndex(double x, double y) {
        int indice = 0;
        Complex zn = new Complex(x, y);
        while (indice < max_iter - 1 && zn.getAbs() < 4) {
            double tmp = zn.ree * zn.ree - zn.img * zn.img;
            zn.img = 2.0 * zn.ree * zn.img + polynom.coef.get(0).ree;
            zn.ree = tmp + polynom.coef.get(0).img;
            indice++;
        }
        // For some reason, using polynom.apply() made everything super slow
        // There is probably a problem with that implementation, but we don't have enough time to
        // figure it out.
        return indice;
    }
}