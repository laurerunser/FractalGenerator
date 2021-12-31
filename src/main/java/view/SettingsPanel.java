package view;

import controller.Controller;
import model.FractalGenerator;
import model.FractalType;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.text.ParseException;

/**
 * This is the settings panel for the GUi view (on the right of the screen)
 */
public class SettingsPanel extends JPanel {
    /**
     * The controller
     */
    private final Controller controller;
    private JRadioButton mandel;
    /**
     * The JButtons to select the fractal type
     */
    private JRadioButton julia;
    /**
     * The field to select the width of the final picture
     */
    private JSpinner widthField;
    /**
     * The field to select the height of the final picture
     */
    private JSpinner heightField;
    /**
     * The field to select the maximum number of iteration
     * (used when computing the fractal)
     */
    private JSpinner nbIterField;
    /**
     * The fields to select the part of the complex plane.
     * - [0] -> start x
     * - [1] -> start y
     * - [2] -> end x
     * - [3] -> end y
     */
    private JSpinner[] spinnersRange;
    /**
     * The fields to select the complex constant for the Julia set.
     * - [0] -> the real part
     * - [1] -> the imaginary part
     */
    private JSpinner[] juliaSpinners;

    /**
     * Creates a new SettingsPanel, puts it on the right of the screen and makes it visible
     *
     * @param controller The controller to process the actions
     */
    public SettingsPanel(Controller controller) {
        this.controller = controller;
        this.setLayout(new GridLayout(0, 1));
        this.add(makeFractalTypeButtons(), 0);
        this.add(makeNbIterationsButton(), 1);
        this.add(makeWidthAndHeightButtons(), 2);
        this.add(makeRangeFields(), 3);
        this.add(makeValidateButton(), 4);
        this.add(makeNavigationButtons(), 5);
        this.add(makeZoomButtons(), 6);
        this.setVisible(true);
    }

    /**
     * Makes the buttons to choose the fractal type
     * and the fields to choose the Julia constant
     *
     * @return a JPanel with all the elements initialized
     */
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

    /**
     * @return a JPanel with a field to choose the maximum number of iterations
     */
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

    /**
     * @return a JPanel with 2 fields to choose the width and height of the final picture
     */
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

    /**
     * @return a JPanel with 4 fields to choose which parts of the complex plane should be considered
     */
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

    /**
     * @return a JPanel with a button to generate the fractal
     */
    private JPanel makeValidateButton() {
        JButton button = new JButton("Generate");

        button.addActionListener(e -> createGenerator());

        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }

    /**
     * When you change a value inside a JSpinner, you need to validate the edit
     * and make sure it is in the correct interval.
     * If it is not, creates a little dialog to tell the user, and keeps the old
     * value.
     *
     * @param spinner the spinner
     * @param min     the min value the spinner can take
     * @param max     the max value the spinner can take
     */
    private void commitEditSpinner(JSpinner spinner, int min, int max) {
        try {
            spinner.commitEdit();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null,
                    "Invalid value, please input a value between " + min + " and " + max);
        }
    }

    /**
     * Initializes a spinner with a tool tip text and the ActionListener
     * to validate the values.
     *
     * @param s the spinner
     */
    private void initRangeSpinner(JSpinner s) {
        s.setToolTipText("Choose an integer value between -300 and 300");
        s.addChangeListener(e -> commitEditSpinner(s, -300, 300));
    }

    /**
     * This method initializes 2 JSpinners to input a complex number.
     * The JSpinners will accept numbers from -300 to 300, with a step size of 0.25
     *
     * @param labelStr     The label of the complex
     * @param spinners     The array in which to put the JSpinners
     * @param startIndex   The index at which to put the first JSpinner in the array
     * @param defaultValue The default value of the fields
     * @return a JPanel wich contains the label + the fields to input a complex number
     */
    private JPanel initComplexeNbSpinners(String labelStr, JSpinner[] spinners, int startIndex, int defaultValue) {
        JLabel plus = new JLabel(" + ");
        JLabel i = new JLabel(" * i");
        JLabel label = new JLabel(labelStr);

        for (int j = startIndex; j < startIndex + 2; j++) {
            spinners[j] = new JSpinner(new SpinnerNumberModel(defaultValue, -300, 300, 0.25));
            initRangeSpinner(spinners[j]);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));

        panel.add(label);
        panel.add(spinners[startIndex]);
        panel.add(plus);
        panel.add(spinners[startIndex + 1]);
        panel.add(i);

        return panel;
    }

    /**
     * Gets the values from all the fields and creates a FractalGenerator from them.
     * Then tells the controller to start generating the fractal.
     */
    private void createGenerator() {
        FractalGenerator generator = null;
        double[] range = parseSpinnerDouble(spinnersRange);
        int width = Integer.parseInt(widthField.getValue().toString());
        int height = Integer.parseInt(heightField.getValue().toString());
        int maxIter = Integer.parseInt(nbIterField.getValue().toString());

        FractalGenerator.Builder builder =
                FractalGenerator.Builder.newInstance()
                        .width(width).height(height)
                        .range(range[0], range[1], range[2] - range[0], range[3] - range[1])
                        .maxIter(maxIter);

        if (julia.isSelected()) {
            double[] juliaNb = parseSpinnerDouble(juliaSpinners);
            generator = builder
                    .type(FractalType.JULIA)
                    .polynomeConstant(juliaNb[0], juliaNb[1])
                    .build();
        } else if (mandel.isSelected()) {
            generator = builder
                    .type(FractalType.MANDELBROT)
                    .build();
        }
        assert (generator != null);
        controller.setGenerator(generator);
    }

    /**
     * Parses the spinners to get the values inside
     *
     * @param spinners the JSpinner array to parse
     * @return an array of the values inside the JSpinners
     */
    private double[] parseSpinnerDouble(JSpinner[] spinners) {
        double[] result = new double[spinners.length];
        for (int i = 0; i < spinners.length; i++) {
            result[i] = Double.parseDouble(spinners[i].getValue().toString());
        }
        return result;
    }

    /**
     * @return a JPanel with navigation buttons inside
     */
    private JPanel makeNavigationButtons() {
        JPanel panel = new JPanel();

        BasicArrowButton left = new BasicArrowButton(BasicArrowButton.WEST);
        left.setPreferredSize(new Dimension(48, 48));
        left.addActionListener(e -> controller.move(true, true));
        BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST);
        right.setPreferredSize(new Dimension(48, 48));
        right.addActionListener(e -> controller.move(true, false));

        BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
        up.setPreferredSize(new Dimension(48, 48));
        up.addActionListener(e -> controller.move(false, true));
        BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
        down.setPreferredSize(new Dimension(48, 48));
        down.addActionListener(e -> controller.move(false, false));

        // TODO : choose how much to move

        panel.add(left);
        panel.add(right);
        panel.add(up);
        panel.add(down);
        return panel;
    }

    /**
     * @return a JPanel with the zoom buttons
     */
    private JPanel makeZoomButtons() {
        // TODO choose how much to zoom in
        JLabel zoom = new JLabel("Zoom : ");

        JButton in = new JButton("+");
        in.addActionListener(e -> controller.zoom(2));
        JButton out = new JButton("-");
        out.addActionListener(e -> controller.zoom(0.5));

        JPanel panel = new JPanel();
        panel.add(zoom);
        panel.add(in);
        panel.add(out);
        return panel;
    }
}