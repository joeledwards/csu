import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;

public class CalculatorGUI
    extends Applet
{
    public static final long serialVersionUID = 1L;

    protected ScrollingLabel scroller = new ScrollingLabel("Roses are red; violets are blue! ", 250);
    protected Thread scrollThread;

    protected TextField fieldA  = new TextField(20);
    protected TextField fieldB  = new TextField(20);

    protected Label resultLabel = new Label("Results will be displayed here.");

    protected Button addButton  = new Button("+");
    protected Button subButton  = new Button("-");
    protected Button mulButton  = new Button("X");
    protected Button divButton  = new Button("/");

    protected CalculatorLogic logic = new CalculatorLogic(fieldA, fieldB, resultLabel, scroller);

    public void init() {
        // Add widgets to Applet
        add(scroller);
        add(fieldA);
        add(fieldB);
        add(resultLabel);
        add(addButton);
        add(subButton);
        add(mulButton);
        add(divButton);

        // Attached ActionListeners to our buttons
        addButton.addActionListener(logic);
        subButton.addActionListener(logic);
        mulButton.addActionListener(logic);
        divButton.addActionListener(logic);

        // Configure scroller
        scroller.setBackground(Color.ORANGE);
        System.out.println("Attempting to get font...");
        Font font = scroller.getFont();
        String fontName = "Helvatica";
        if (font != null) {
            fontName = font.getFontName();
        }
        Dimension dimensions = new Dimension(390, 30);
        if (scroller.isPreferredSizeSet()) {
            dimensions = new Dimension(390, scroller.getPreferredSize().height);
        }
        scroller.setPreferredSize(dimensions);
        scroller.setFont(new Font(fontName, Font.BOLD, 20));
        scrollThread = new Thread(scroller);
        scroller.setOwner(scrollThread);
        scroller.pause();
        scrollThread.start();
    }

    public void start() {
        scroller.play();
    }

    public void close() {
        scroller.pause();
    }
}
