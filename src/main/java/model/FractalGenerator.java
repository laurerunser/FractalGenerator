package model;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.stream.IntStream;

// TODO : doc
public abstract class FractalGenerator {

    protected Polynome polynom;
    protected Rectangle2D.Double range;
    protected Rectangle2D.Double initialRange;

    protected int width;
    protected int height;
    protected double[][] divergenceIndex = null;

    protected int max_iter;
    protected static int RADIUS = 4;

    public FractalGenerator(int height, int width, int max_iter, Rectangle2D.Double range) {
        this.height = height;
        this.width = width;
        this.max_iter = max_iter;
        this.range = range;
        this.initialRange = new Rectangle2D.Double(range.x, range.y, range.width, range.height);
        divergenceIndex = new double[width][height];
    }

    @Override
    public String toString() {
        return "\nhauteur x largeur = " + height + " x " + width
                + "\nstarting point : " + range.x + " + " + range.y + " *i"
                + "\nending point : " + (range.x + range.width) + " + " + (range.y + range.height) + " *i";
    }

    public Rectangle2D.Double getRange() {
        return range;
    }

    public void resetRange() {
        range = initialRange;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxIter() {
        return max_iter;
    }

    public double[][] getDivergenceIndex() {
        return divergenceIndex; // TODO : defensive copy ?
    }

    /**
     * Computes all the divergence index
     */
    public void computeDivergenceIndex(Rectangle2D.Double range) {
        IntStream.range(0, width).parallel().forEach(x -> {
            IntStream.range(0, height).parallel().forEach(y -> {
                double xI = range.x + (x * range.getWidth()) / width;
                double yJ = range.y + (y * range.getHeight()) / height;
                divergenceIndex[x][y] = divergenceIndex(xI, yJ);
            });
        });
    }

    abstract int divergenceIndex(double x, double y);


    //////////////
    // BUILDER
    /////////////
    public static class Builder {
        private Polynome polynom;
        private Rectangle2D.Double range;
        private int width;
        private int height;
        private int maxIter;
        private FractalType type;

        public static Builder newInstance() {
            return new Builder();
        }

        public FractalGenerator build() {
            if (width <= 0) width = 800;
            if (height <= 0) height = 800;
            if (maxIter < 300 || maxIter > 2000) maxIter = 999;
            range = Objects.requireNonNullElseGet(range, () -> new Rectangle2D.Double(-1, -1, 2, 2));

            if (type == FractalType.JULIA) {
                polynom = Objects.requireNonNullElseGet(polynom, () -> Polynome.buildPolynomeSimple(new Complex(0, 0)));
                return new JuliaGenerator(polynom, width, height, maxIter, range);
            } else if (type == FractalType.MANDELBROT) {
                return new MandelbrotGenerator(width, height, maxIter, range);
            }

            return null;
        }

        public Builder type(FractalType type) {
            this.type = type;
            return this;
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
