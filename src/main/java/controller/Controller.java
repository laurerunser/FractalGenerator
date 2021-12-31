package controller;

import model.ColorCode;
import model.FractalGenerator;
import view.GUI;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.IntStream;

public class Controller {
    /**
     * The FractalGenerator.
     * Can be null at the beginning if in GUI mode,
     * and no Fractal has been generated yet.
     */
    private FractalGenerator generator;

    /**
     * The GUI view.
     * Can be null if in CLI mode.
     */
    private GUI view;

    /**
     * The generated fractal.
     */
    private BufferedImage image;

    /**
     * Creates a new Controller for the CLI mode.
     *
     * @param generator the Fractal Generator
     */
    public Controller(FractalGenerator generator) {
        this.generator = generator;
    }

    /**
     * Creates a new Controller for the GUI mode.
     * It immediatly creates a GUI view and makes it visible.
     */
    public Controller() {
        view = new GUI(this);
    }

    /**
     * Set a new FractalGenerator, then computes and draws the fractal
     *
     * @param generator the new FractalGenerator
     */
    public void setGenerator(FractalGenerator generator) {
        this.generator = generator;
        view.getImagePanel().addImageDisplay(generator.getWidth(), generator.getHeight());
    }

    /**
     * Saves the generated fractal image
     *
     * @param makeImage if true, computes the image beforehand
     * @param filename  the name of the file to save to
     * @param colorCode the color code to use (if generating the image
     */
    public void saveImage(boolean makeImage, String filename, ColorCode.Code colorCode) {
        if (filename.isEmpty()) {
            filename = "test.png";
        }

        File outputfile = new File(filename);
        if (makeImage) {
            generator.computeDivergenceIndex();
            image = makeImage(colorCode);
        }

        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.out.println("pb writing image " + filename);
        }

        try {
            FileWriter fo = new FileWriter(filename + ".txt");
            fo.write(generator.toString());
            fo.close();
        } catch (Exception e) {
            System.out.println("Couldn't save the text file with the settings.");
        }
    }

    /**
     * Compute the fractal for the given range
     *
     * @param range the range to consider. If it is null, use the entire range.
     */
    public void computeFractal(Rectangle2D.Double range) {
        if (range != null) {
            generator.computeDivergenceIndex(range);
        } else {
            generator.computeDivergenceIndex();
        }
    }

    /**
     * Make the image from the already computed divergence index.
     *
     * @param colorCode the color code to use
     * @return the BufferedImage of the fractal
     */
    public BufferedImage makeImage(ColorCode.Code colorCode) {
        int width = generator.getWidth();
        int height = generator.getHeight();

        double[][] divergenceIndex = generator.getDivergenceIndex();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        IntStream.range(0, width).parallel().forEach(x ->
                IntStream.range(0, height).parallel().forEach(y ->
                        image.setRGB(x, y, ColorCode.getColor(divergenceIndex[x][y], generator.getMaxIter(), colorCode))));
        // this parallel Stream doesn't seam to make much of a difference, at least for smaller images.
        // But it doesn't hurt, and it might help for huge sizes.
        return image;
    }

    /**
     * Moves inside of the fractal and redraws the image
     *
     * @param horizontal true = left, false = right
     * @param direction  true = up, false = down
     */
    public void move(boolean horizontal, boolean direction) {
        Rectangle2D.Double range = generator.getRange();
        int dir = direction ? -1 : 1;
        if (horizontal) {
            range.x = range.x + dir * (range.width / 3);
        } else {
            range.y = range.y + dir * (range.height / 3);
        }
        view.getImagePanel().drawImage(true);
    }

    /**
     * Resets the fractal to the original range and redraws the image
     */
    public void resetFractal() {
        generator.resetRange();
        view.getImagePanel().drawImage(true);
    }

    /**
     * Zooms in (or out) of the fractal and redraws the image
     *
     * @param x           the x-coordinate of the new center
     * @param y           the y-coordinate of the new center
     * @param scaleFactor the zoom factor
     */
    public void zoom(int x, int y, double scaleFactor) {
        Rectangle2D.Double range = generator.getRange();
        // scaleFactor*2 because we only need half of the
        // width/height => the point (x,y) is the new center of the screen
        range.x = (range.x + (double) x / generator.getWidth() * range.width) - range.width / (scaleFactor * 2);
        range.y = (range.y + (double) y / generator.getHeight() * range.height) - range.height / (scaleFactor * 2);

        range.width /= scaleFactor;
        range.height /= scaleFactor;

        view.getImagePanel().drawImage(true);
    }

    /**
     * Zooms in (or out) on the center point of the picture
     *
     * @param scaleFactor the zoom factor
     */
    public void zoom(double scaleFactor) {
        zoom(generator.getWidth() / 2, generator.getHeight() / 2, scaleFactor);
    }

}
