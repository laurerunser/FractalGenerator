package controller;

import model.ColorCode;
import model.FractalGenerator;
import view.GUI;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

// TODO doc

public class Controller {
    private FractalGenerator generator;
    private GUI view;
    private BufferedImage image;

    public Controller(FractalGenerator generator) {
        this.generator = generator;
    }
    public Controller() {
        view = new GUI(this);
    }

    public void setGenerator(FractalGenerator generator) {
        this.generator = generator;
        view.getImagePanel().addImageDisplay(generator.getWidth(), generator.getHeight());
    }

    public void saveImage(boolean makeImage, String filename, ColorCode.Code colorCode) {
        if (filename.isEmpty()) {
            filename = "test.png";
        }

        File outputfile = new File(filename);
        if(makeImage) {
            generator.computeDivergenceIndex();
            image = makeImage(colorCode);
        }

        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.out.println("pb writing image " + filename);
        }
    }

    public void computeFractal(Rectangle2D.Double range) {
        if (range != null) {
            generator.computeDivergenceIndex(range);
        } else {
            generator.computeDivergenceIndex();
        }
    }

    public BufferedImage makeImage(ColorCode.Code colorCode) {
        int width = generator.getWidth();
        int height = generator.getHeight();

        double[][] divergenceIndex = generator.getDivergenceIndex();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        IntStream.range(0, width).parallel().forEach(x ->
                IntStream.range(0, height).parallel().forEach(y ->
                        image.setRGB(x, y, ColorCode.getColor(divergenceIndex[x][y], generator.getMaxIter(), colorCode))));

        return image;
    }

    public void move(boolean horizontal, boolean direction) {
        Rectangle2D.Double range = generator.getRange();
        int dir = direction?-1:1;
        if (horizontal) {
            range.x = range.x + dir * (range.width / 3);
        } else {
            range.y = range.y + dir * (range.height / 3);
        }
        view.getImagePanel().drawImage(true);
    }

    public void resetFractal() {
        generator.resetRange();
        view.getImagePanel().drawImage(true);
    }

    // TODO : choose how much to zoom (replace '2' by a variable 'scale')
    public void zoom(int x, int y) {
        Rectangle2D.Double range = generator.getRange();
        // 2*2 bc scale factor is 2, and also we only need half of the
        // width bc the point (x,y) is the new center of the screen
        range.x = (range.x + (double)x/generator.getWidth() * range.width) - range.width / (2*2);
        range.y = (range.y + (double)y/ generator.getHeight() * range.height) - range.height / (2*2);

        range.width /= 2;
        range.height /= 2;

        view.getImagePanel().drawImage(true);
    }



}
