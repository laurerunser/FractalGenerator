package model;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
//TODO : doc
/**
 * This class generates Julia sets
 */
public class JuliaGenerator extends FractalGenerator{

    @Override
    public String toString(){
        return "\nhauteur x largeur = " + height + " x " + width
                + "\npolynome : " + polynom.toString();
    }

    private JuliaGenerator(Polynome poly, int width, int height, int maxIter, Rectangle2D.Double range){
        super(height, width, maxIter, range);
        divergenceIndex = new double[width][height];
        this.polynom = poly;
    }

    int divergenceIndex(double x, double y){
        int indice = 0;
        Complex zn = new Complex(x, y);
        while (indice < max_iter-1 && zn.getAbs() < 4){
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


    //////////////
    // BUILDER
    /////////////
    public static class Builder {
        private Polynome polynom;
        private Rectangle2D.Double range;
        private int width;
        private int height;
        private int maxIter;

        public static Builder newInstance() {
            return new Builder();
        }

        public JuliaGenerator build() {
            if (width <= 0) width = 800;
            if (height <= 0) height = 800;
            if (maxIter < 300 || maxIter > 2000) maxIter = 999;
            polynom = Objects.requireNonNullElseGet(polynom, () -> Polynome.buildPolynomeSimple(new Complex(0, 0)));
            range = Objects.requireNonNullElseGet(range, () -> new Rectangle2D.Double(-1, -1, 2, 2));
            return new JuliaGenerator(polynom, width, height, maxIter, range);
        }

        public Builder width(int w) {
            width = w;
            return this;
        }
        public Builder height(int h) {
            height = h;
            return this;
        }
        public Builder polynomeConstant(double r, double i) {
            polynom = Polynome.buildPolynomeSimple(new Complex(r, i));
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