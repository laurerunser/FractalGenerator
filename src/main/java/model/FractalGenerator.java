package model;

import java.awt.geom.Rectangle2D;
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


}
