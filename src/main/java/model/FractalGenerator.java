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

    public void moveHorizontal(boolean left) {
        int nbMove = width / 3;
        double newX;

        if (left) {
            for (int i = width - 1; i > nbMove; i--) {
                System.arraycopy(divergenceIndex[i - 1], 0, divergenceIndex[i], 0, height);
            }
            newX = range.x - nbMove;
        } else {
            for (int i = 0; i < nbMove * 2; i++) {
                System.arraycopy(divergenceIndex[i + 1], 0, divergenceIndex[i], 0, height);
            }
            newX = range.x + nbMove;
        }

        Rectangle2D.Double missingRange = new Rectangle2D.Double(newX, range.y, nbMove, range.height);
        computeDivergenceIndex(missingRange);

        range = new Rectangle2D.Double(newX, range.y, range.width, range.height);
    }


    public void moveVertical(boolean up) {
        int nbMove = height / 3;
        double newY;

        if (up) {
            for (int j = height - 1; j > nbMove; j--) {
                for (int i = 0; i < width; i++) {
                    divergenceIndex[i][j] = divergenceIndex[i][j - 1];
                }
            }
            newY = range.y - nbMove;
        } else {
            for (int j = 0; j < 2 * nbMove; j++) {
                for (int i = 0; i < width; i++) {
                    divergenceIndex[i][j] = divergenceIndex[i][j + 1];
                }
            }
            newY = range.y + nbMove;
        }

        Rectangle2D.Double missingRange = new Rectangle2D.Double(range.x, newY, range.width, nbMove);
        computeDivergenceIndex(missingRange);

        range = new Rectangle2D.Double(range.x, newY, range.width, range.height);
    }
}