import java.applet.Applet;
import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;

public class CalculatorGUI
    extends Applet
{
    public static final long serialVersionUID = 1L;

    protected TextField fieldA = new TextField(20);
    protected TextField fieldB = new TextField(20);

    protected Label resultLabel = new Label("0.                                            ");

    protected Button addButton = new Button("+");
    protected Button subButton = new Button("-");
    protected Button mulButton = new Button("X");
    protected Button divButton = new Button("/");

    protected CalculatorLogic logic = new CalculatorLogic(fieldA, fieldB, resultLabel);

    public void init() {
        // Add widgets to Applet
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
    }
}
