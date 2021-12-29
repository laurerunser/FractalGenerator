package controller;

import model.ColorCode;
import model.FractalGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

// TODO doc

public class Controller {
    private FractalGenerator generator;
    private BufferedImage image;

    public Controller(FractalGenerator generator) {
        this.generator = generator;
    }

    public void saveImage(boolean makeImage, String filename, ColorCode.Code colorCode) {
        if (filename.isEmpty()) {
            filename = "test.png";
        }

        File outputfile = new File(filename);
        if(makeImage) {
            generator.computeDivergenceIndex(generator.getRange());
            image = makeImage(colorCode);
        }

        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.out.println("pb writing image " + filename);
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

}
