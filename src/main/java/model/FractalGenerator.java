package model;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This is the abstract class that modelises a generator for fractals.
 */
public abstract class FractalGenerator {
    /**
     * The radius. Used to compute the fractal.
     * Could be changed ? But it doesn't seem to make much difference in the fractal
     */
    protected static int RADIUS = 4;
    /**
     * The Polynome associated with the fractal generator.
     * Can be null for some generators (for ex Mandelbrot)
     */
    protected Polynome polynom;
    /**
     * The current range of the complex plane being considered
     */
    protected Rectangle2D.Double range;
    /**
     * The initial range of the complex plane.
     * Used to reset the fractal to its initial state.
     */
    protected Rectangle2D.Double initialRange;
    /**
     * The width of the final picture
     */
    protected int width;
    /**
     * The height of the final picture
     */
    protected int height;
    /**
     * The array that holds the divergence index for each pixel in the final picture
     */
    protected double[][] divergenceIndex;
    /**
     * The maximum number of iteration
     */
    protected int max_iter;

    /**
     * Creates a new FractalGenerator
     *
     * @param height   the height of the final picture
     * @param width    the width of the final picture
     * @param max_iter the maximum number of iterations
     * @param range    the range of the complex plane to consider
     */
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

    /**
     * @return the range
     */
    public Rectangle2D.Double getRange() {
        return range;
    }

    /**
     * Resets the range to the initial one (before any zooming or navigation happened)
     */
    public void resetRange() {
        range = initialRange;
    }

    /**
     * @return the width of the final picture
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the final picture
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the maximum number of iterations
     */
    public int getMaxIter() {
        return max_iter;
    }

    /**
     * @return the array of divergence index
     */
    public double[][] getDivergenceIndex() {
        return divergenceIndex; // TODO : defensive copy ?
    }

    /**
     * Computes all the divergence index for the given range.
     */
    public void computeDivergenceIndex(Rectangle2D.Double range) {
        IntStream.range(0, width).parallel().forEach(x -> {
            IntStream.range(0, height).parallel().forEach(y -> {
                double xI = range.x + (x * range.getWidth()) / width;
                double yJ = range.y + (y * range.getHeight()) / height;
                divergenceIndex[x][y] = divergenceIndex(xI, yJ);
            });
        });

        System.out.println(range.x + " " + range.y + " " + range.width + " " + range.height);
    }

    /**
     * Compute all the divergence index for the current range.
     */
    public void computeDivergenceIndex() {
        computeDivergenceIndex(range);
    }

    /**
     * Computes the divergence index for a number
     *
     * @param x the real part of the number
     * @param y the imaginary part of the number
     * @return the divergence index
     */
    protected abstract int divergenceIndex(double x, double y);

    /**
     * This class is a builder for different types of fractals
     */
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
