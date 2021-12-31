package model;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
//TODO : doc
/**
 * This class generates Julia sets
 */
public class MandelbrotGenerator extends FractalGenerator{

    @Override
    public String toString(){
        return "\nhauteur x largeur = " + height + " x " + width ;
    }

    private MandelbrotGenerator(int width, int height, int maxIter, Rectangle2D.Double range){
        super(height, width, maxIter, range);
        divergenceIndex = new double[width][height];
    }

    
    int divergenceIndex(double x, double y){
        int indice = 0;
        Complex cnst = new Complex(x, y); //constante
        Complex var = new Complex(0, 0);
        while (indice < max_iter-1 && var.getAbs() <= RADIUS){
            var.mandel(cnst);
            indice++;
        }
        return indice;
    }

    /*
    double a = 0, b = 0;
    int iteration = 0;
    while (a*a+b*b <= 4 && iteration < max) {
        double a_new = a*a - b*b + x;
        b = 2*a*b + y;
        a = a_new;
        iteration++;
    }
    return iteration;
    }
} */

    //////////////
    // BUILDER
    /////////////
    public static class Builder {
        private Rectangle2D.Double range;
        private int width;
        private int height;
        private int maxIter;

        public static Builder newInstance() {
            return new Builder();
        }

        public MandelbrotGenerator build() {
            if (width <= 0) width = 800;
            if (height <= 0) height = 800;
            if (maxIter < 300 || maxIter > 2000) maxIter = 999;
            range = Objects.requireNonNullElseGet(range, () -> new Rectangle2D.Double(-1, -1, 2, 2));
            return new MandelbrotGenerator(width, height, maxIter, range);
        }

        public Builder width(int w) {
            width = w;
            return this;
        }
        public Builder height(int h) {
            height = h;
            return this;
        }

        public Builder range(double r, double i, double w, double h) {
            if (w <= 0) w = 2;
            if (h <= 0) h = 2;
            range = new Rectangle2D.Double(r, i, w, h);
            return this;
        }

        public Builder maxIter(int m) {
            maxIter = m;
            return this;
        }

    }
}