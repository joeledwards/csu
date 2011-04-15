import java.awt.Button;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextComponent;

public class CalculatorLogic
    implements ActionListener
{
    private TextComponent inputA = null;
    private TextComponent inputB = null;
    private Label         output = null;

    public CalculatorLogic(TextComponent inputA, TextComponent inputB, Label output) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.output = output;
    }

    public void actionPerformed(ActionEvent evt) {
        double valueA = 0.0;
        double valueB = 0.0;
        try { 
            valueA = Double.parseDouble(inputA.getText());
            valueB = Double.parseDouble(inputB.getText());
            Button source = (Button)evt.getSource();
            String op = source.getLabel();
            if ("+".compareTo(op) == 0) {
                output.setText("" + (valueA + valueB));
            } else if ("-".compareTo(op) == 0) {
                output.setText("" + (valueA - valueB));
            } else if ("X".compareTo(op) == 0) {
                output.setText("" + (valueA * valueB));
            } else if ("/".compareTo(op) == 0) {
                if (valueB == 0.0) {
                    output.setText("Divide by zero!");
                } else {
                    output.setText("" + (valueA / valueB));
                }
            } else {
                output.setText("Unsupported Operation!");
            }
        } catch (NumberFormatException e) {
            output.setText("Invalid Input!");
        }
    }
}
