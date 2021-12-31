package view;

import controller.Controller;
import model.ColorCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * This is the panel that contains the fractal image and a few buttons.
 */
public class ImagePanel extends JPanel {
    /**
     * The controller that performs the actions
     */
    private final Controller controller;
    /**
     * The width of the imageDisplay
     */
    protected int width;
    /**
     * The height of the imageDisplay
     */
    protected int height;
    /**
     * The current color code to make the image
     */
    private ColorCode.Code colorCode;
    /**
     * The JPanel that contains the image
     */
    private ImageDisplay imageDisplay;

    /**
     * Creates a new ImagePanel, with the initial color code HUE2, and makes it visible
     *
     * @param controller the Controller to perform the actions
     */
    public ImagePanel(Controller controller) {
        this.controller = controller;
        this.colorCode = ColorCode.Code.HUE2;
        makePanel();
    }

    /**
     * Initializes all the elements inside the ImagePanel
     */
    private void makePanel() {
        this.setLayout(new BorderLayout());
        imageDisplay = new ImageDisplay();
        this.add(imageDisplay, BorderLayout.CENTER);
        addColorsButtons();
        addResetAndSaveButtons();
    }

    /**
     * Create the buttons to choose the color code of the picture
     */
    private void addColorsButtons() {
        ButtonGroup bg = new ButtonGroup();
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        for (ColorCode.Code c : ColorCode.Code.values()) {
            JRadioButton b = new JRadioButton(c.toString());
            b.addActionListener(e -> changeColorCode(c));
            bg.add(b);
            p.add(b);
            if (c == ColorCode.Code.HUE2)
                b.setSelected(true); // default button is HUE2
        }
        this.add(p, BorderLayout.SOUTH);
    }

    /**
     * Create the buttons to save the picture,
     * and to reset the image to normal zoom and navigation
     */
    private void addResetAndSaveButtons() {
        JButton reset = new JButton("Reset zoom");
        reset.addActionListener(e -> controller.resetFractal());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.add(reset);
        panel.add(makeSaveButton());

        this.add(panel, BorderLayout.NORTH);
    }

    /**
     * @return the JButton to save the picture
     */
    private JButton makeSaveButton() {
        JButton save = new JButton("Save image");
        save.addActionListener(e -> {
            controller.saveImage(false, "image.png", colorCode);
        });
        return save;
    }

    /**
     * Changes the color code and redraws the image
     *
     * @param c the new color code
     */
    public void changeColorCode(ColorCode.Code c) {
        this.colorCode = c;
        drawImage(false);
    }

    /**
     * Creates the imageDisplay and adds it to the panel.
     * Also creates a MouseListener to deal zoom in when the display
     * is clicked.
     *
     * @param width  the width of the picture
     * @param height the height of the picture
     */
    public void addImageDisplay(int width, int height) {
        this.width = width;
        this.height = height;
        imageDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                controller.zoom(e.getX(), e.getY(), 2);
                imageDisplay.repaint();
            }
        });
        // using MouseAdapter and not MouseListener bc I don't want to define all the
        // methods, only mouseClicked
        drawImage(true);
    }

    /**
     * Redraws the image
     *
     * @param compute if true, the image is recomputed before being drawn. Otherwise,
     *                the current divergence index values are used
     */
    public void drawImage(boolean compute) {
        if (compute) {
            controller.computeFractal(null);
        }
        imageDisplay.repaint();
    }

    /**
     * This panel contains the fractal
     */
    private class ImageDisplay extends JPanel {
        /**
         * The image of the fractal to display
         */
        private BufferedImage image;

        @Override
        public Dimension getPreferredSize() {
            return image == null ? new Dimension(800, 800) : new Dimension(image.getWidth(), image.getHeight());
        }

        /**
         * Paints the image.
         * This is automatically called with the right args when the repaint() method is called
         * on the component.
         * The method should never be called alone. Use drawImage() to redraw the image.
         *
         * @param g the Graphics to repaint the component with
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            if (width != 0 && height != 0) {
                image = controller.makeImage(colorCode);
                g2d.drawImage(image, 0, 0, this);
            } else {
                BufferedImage tmp = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i < 200; i++) {
                    for (int j = 0; j< 200; j++) {
                        tmp.setRGB(i,j, 0xFFFFFF);
                    }
                }
                g2d.drawImage(tmp, 0, 0,this);
            }
            g2d.dispose();
        }
    }
}
