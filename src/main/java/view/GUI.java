package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * This is the main class for the GUI view
 */
public class GUI extends JFrame {
    /**
     * The controller to perform the actions
     */
    private final Controller controller;

    /**
     * A JPanel that contains all the buttons for the settings
     */
    private JPanel settingsPanel;

    /**
     * A JPanel that contains the image and a few buttons
     */
    private ImagePanel imagePanel;

    /**
     * Creates a new GUI view, initializes the elements and makes it visible
     *
     * @param controller the controller to perform the actions with
     */
    public GUI(Controller controller) {
        this.controller = controller;
        makeGUI();
        this.setTitle("Fractal generator");
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Helper method to create the internal JPanel and add them to the frame
     */
    private void makeGUI() {
        settingsPanel = new SettingsPanel(controller);
        imagePanel = new ImagePanel(controller);
        this.setLayout(new GridLayout(1, 0));
        this.add(imagePanel);
        this.add(settingsPanel);
    }

    /**
     * @return the imagePanel
     */
    public ImagePanel getImagePanel() {
        return imagePanel;
    }
}
