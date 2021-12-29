package view;

import controller.Controller;
import model.FractalGenerator;
import model.JuliaGenerator;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

public class SettingsPanel extends JPanel {
    private JRadioButton julia;
    private JRadioButton mandel;
    private JSpinner widthField;
    private JSpinner heightField;
    private JSpinner nbIterField;
    private JSpinner[] spinnersRange;
    private JSpinner[] juliaSpinners;
    private final Controller controller;

    public SettingsPanel(Controller controller) {
        this.controller = controller;
        this.setLayout(new GridLayout(5, 1));
        this.add(makeFractalTypeButtons(), 0);
        this.add(makeNbIterationsButton(), 1);
        this.add(makeWidthAndHeightButtons(), 2);
        this.add(makeRangeFields(), 3);
        this.add(makeValidateButton(), 4);
        this.setVisible(true);
    }


    private JPanel makeFractalTypeButtons() {
        // choice of the fractal
        ButtonGroup bg = new ButtonGroup();
        julia = new JRadioButton("Julia", true);
        mandel = new JRadioButton("Mandelbrot", false);
        bg.add(julia);
        bg.add(mandel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.add(julia);
        buttons.add(mandel);

        // choice of the Julia constant
        juliaSpinners = new JSpinner[2];
        JPanel juliaSpinnersPanel = initComplexeNbSpinners("Julia constant", juliaSpinners, 0, 0);

        JPanel juliaConst = new JPanel();
        juliaConst.setLayout(new GridLayout(2, 0));
        JLabel juliaExpl = new JLabel("If you selected the Julia fractal, enter a constant for the polynome.");
        juliaConst.add(juliaExpl);
        juliaConst.add(juliaSpinnersPanel);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Choose your factal type"));
        panel.add(buttons);
        panel.add(juliaConst);
        return panel;
    }

    private JPanel makeNbIterationsButton() {
        JLabel label = new JLabel("Maximum number of iterations");
        nbIterField = new JSpinner(new SpinnerNumberModel(999, 300, 2000, 1));
        nbIterField.setToolTipText("The bigger this number gets, the more details there will be. Don't go too big" +
                "or it will take a very long time to generate the fractal.");
        nbIterField.addChangeListener(e -> commitEditSpinner(nbIterField, 300, 2000));

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(nbIterField);
        return panel;
    }

    private JPanel makeWidthAndHeightButtons() {
        JLabel widthLabel = new JLabel("Width");
        JLabel heightLabel = new JLabel("Height");

        widthField = new JSpinner(new SpinnerNumberModel(800, 20, 2000, 1));
        widthField.setToolTipText("The width of your final picture. The bigger it is, the more details you" +
                "will be able to see");
        widthField.addChangeListener(e -> commitEditSpinner(widthField, 20, 2000));

        heightField = new JSpinner(new SpinnerNumberModel(800, 20, 2000, 1));
        heightField.setToolTipText("The height of your final picture. The bigger is it, the more details you" +
                "will be able to see");
        heightField.addChangeListener(e -> commitEditSpinner(heightField, 20, 2000));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(widthLabel);
        panel.add(widthField);
        panel.add(heightLabel);
        panel.add(heightField);
        panel.setBorder(BorderFactory.createTitledBorder("Dimensions of the picture"));
        return panel;
    }

    private JPanel makeRangeFields() {
        JLabel explanation = new JLabel("You can choose which part of the complexe plane you want to compute." +
                "You can always zoom in or out later.");

        spinnersRange = new JSpinner[4];
        JPanel startPanel = initComplexeNbSpinners("Bottom-left corner", spinnersRange, 0, -1);
        JPanel endPanel = initComplexeNbSpinners("Upper-right corner", spinnersRange, 2, 1);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Part of the complexe plane to consider"));
        panel.add(explanation);
        panel.add(startPanel);
        panel.add(endPanel);
        return panel;
    }

    private JPanel makeValidateButton() {
        JButton button = new JButton("Generate");

        button.addActionListener(e -> createGenerator());

        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }

    private void commitEditSpinner(JSpinner spinner, int min, int max) {
        try {
            spinner.commitEdit();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null,
                    "Invalid value, please input a value between " + min + " and " + max);
        }
    }

    private void initRangeSpinner(JSpinner s) {
        s.setToolTipText("Choose an integer value between -300 and 300");
        s.addChangeListener(e -> commitEditSpinner(s, -300, 300));
    }

    private JPanel initComplexeNbSpinners(String labelStr, JSpinner[] spinners, int startIndex, int defaultValue) {
        JLabel plus = new JLabel(" + ");
        JLabel i = new JLabel(" * i");
        JLabel label = new JLabel(labelStr);

        for(int j = startIndex; j<startIndex+2; j++) {
            spinners[j] = new JSpinner(new SpinnerNumberModel(defaultValue, -300, 300, 0.25));
            initRangeSpinner(spinners[j]);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));

        panel.add(label);
        panel.add(spinners[startIndex]);
        panel.add(plus);
        panel.add(spinners[startIndex+1]);
        panel.add(i);

        return panel;
    }

    private void createGenerator() {
        FractalGenerator generator = null;
        double[] range = parseSpinnerDouble(spinnersRange);
        int width = Integer.parseInt(widthField.getValue().toString());
        int height = Integer.parseInt(heightField.getValue().toString());
        int maxIter = Integer.parseInt(nbIterField.getValue().toString());
        if (julia.isSelected()) {
            double[] juliaNb = parseSpinnerDouble(juliaSpinners);
            generator = JuliaGenerator.Builder.newInstance()
                    .width(width).height(height)
                    .range(range[0], range[1], range[2], range[3])
                    .polynomeConstant(juliaNb[0], juliaNb[1])
                    .maxIter(maxIter)
                    .build();
        } else {
            // TODO mandelbrot
        }
        controller.setGenerator(generator);
    }

    private double[] parseSpinnerDouble(JSpinner[] spinners) {
        double[] result = new double[spinners.length];
        for (int i = 0; i < spinners.length; i++) {
            result[i] = Double.parseDouble(spinners[i].getValue().toString());
        }
        return result;
    }
}