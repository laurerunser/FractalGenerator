package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private final Controller controller;
    private JPanel settingsPanel;
    private ImagePanel imagePanel;

    public GUI(Controller controller) {
        this.controller = controller;
        makeGUI();
        this.setTitle("Fractal generator");
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void makeGUI() {
        settingsPanel = new SettingsPanel(controller);
        imagePanel = new ImagePanel(controller);
        this.setLayout(new GridLayout(1,0));
        this.add(imagePanel);
        this.add(settingsPanel);
    }

    public ImagePanel getImagePanel() { return imagePanel; }
}
