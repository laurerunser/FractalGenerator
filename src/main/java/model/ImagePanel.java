package model;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private final Controller controller;
    private ColorCode.Code colorCode;
    private ImageDisplay imageDisplay;
    protected int width;
    protected int height;

    public ImagePanel(Controller controller) {
        this.controller = controller;
        this.colorCode = ColorCode.Code.HUE2;
        makePanel();
    }

    private void makePanel() {
        this.setLayout(new BorderLayout());
        imageDisplay = new ImageDisplay();
        this.add(imageDisplay, BorderLayout.CENTER);
        addEastToolbar();
        addColorsButtons();
        addResetAndSaveButtons();
    }

    private void addEastToolbar() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        panel.add(addNavigationButtons());
        this.add(panel, BorderLayout.EAST);
    }

    private JPanel addNavigationButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(48 + 48, 48 + 48));

        BasicArrowButton left = new BasicArrowButton(BasicArrowButton.WEST);
        left.setPreferredSize(new Dimension(48, 48));
        left.addActionListener(e -> controller.move(true, true));
        BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST);
        right.setPreferredSize(new Dimension(48, 48));
        right.addActionListener(e -> controller.move(true, false));
        BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
        up.setPreferredSize(new Dimension(48, 48));
        up.addActionListener(e -> controller.move(false, true));
        BasicArrowButton down = new BasicArrowButton(BasicArrowButton.WEST);
        down.setPreferredSize(new Dimension(48, 48));
        down.addActionListener(e -> controller.move(false, false));

        // TODO : choose how much to move

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        panel.add(up, BorderLayout.NORTH);
        panel.add(down, BorderLayout.SOUTH);
        return panel;
    }

    private void addColorsButtons() {
        ButtonGroup bg = new ButtonGroup();
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        for (ColorCode.Code c : ColorCode.Code.values()) {
            JRadioButton b = new JRadioButton(c.toString());
            b.addActionListener(e -> changeColorCode(c));
            bg.add(b);
            p.add(b);
            if (c.toString().equals(ColorCode.Code.HUE2.toString()))
                b.isSelected(); // default button is HUE2 // TODO fix
        }
        this.add(p, BorderLayout.SOUTH);
    }

    private void addResetAndSaveButtons() {
        JButton reset = new JButton("Reset zoom");
        reset.addActionListener(e -> controller.resetFractal());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.add(reset);
        panel.add(makeSaveButton());

        this.add(panel, BorderLayout.NORTH);
    }

    private JButton makeSaveButton() {
        JButton save = new JButton("Save image");
        save.addActionListener(e -> controller.saveImage(false, "img.png", colorCode));
        // TODO : let the user choose where to save the image and the filename
        return save;
    }

    public void changeColorCode(ColorCode.Code c) {
        this.colorCode = c;
        drawImage(false);
    }

    public void addImageDisplay(int width, int height) {
        this.width = width;
        this.height = height;
        drawImage(true);
    }

    public void drawImage(boolean compute) {
        if (compute) {
            controller.computeFractal(null);
        }
        imageDisplay.repaint();
    }

    private class ImageDisplay extends JPanel {
        private BufferedImage image;

        @Override
        public Dimension getPreferredSize() {
            return image == null ? new Dimension(800, 800) : new Dimension(image.getWidth(), image.getHeight());
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            if (width != 0 && height != 0) {
                image = controller.makeImage(colorCode);
                g2d.drawImage(image, 0, 0,this);
            } else {
                BufferedImage tmp = new BufferedImage(200,200, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i< 200; i++) {
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
