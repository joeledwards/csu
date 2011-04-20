import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextComponent;

public class CalculatorLogic
    implements ActionListener
{
    private TextComponent  inputA   = null;
    private TextComponent  inputB   = null;
    private Label          output   = null;
    private ScrollingLabel scroller = null;

    public CalculatorLogic(TextComponent inputA, TextComponent inputB, 
                           Label output, ScrollingLabel scroller) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.output = output;
        this.scroller = scroller;
    }

    public void actionPerformed(ActionEvent evt) {
        double valueA = 0.0;
        double valueB = 0.0;
        double result = 0.0;
        try { 
            valueA = Double.parseDouble(inputA.getText());
            valueB = Double.parseDouble(inputB.getText());
            Button source = (Button)evt.getSource();
            String op = source.getLabel();
            if ("+".compareTo(op) == 0) {
                result = valueA + valueB;
                output.setText("" + result);
                updateScroller(result);
                scroller.setBackground(Color.BLUE);
            } else if ("-".compareTo(op) == 0) {
                result = valueA - valueB;
                output.setText("" + result);
                updateScroller(result);
                scroller.setBackground(Color.YELLOW);
            } else if ("X".compareTo(op) == 0) {
                result = valueA * valueB;
                output.setText("" + result);
                updateScroller(result);
                scroller.setBackground(Color.ORANGE);
            } else if ("/".compareTo(op) == 0) {
                if (valueB == 0.0) {
                    output.setText("Divide by zero!");
                    scroller.setBackground(Color.LIGHT_GRAY);
                } else {
                    output.setText("" + (valueA / valueB));
                    updateScroller((int)(valueA + valueB));
                    updateScroller(result);
                    scroller.setBackground(Color.GREEN);
                }
            } else {
                output.setText("Unsupported Operation!");
                scroller.setBackground(Color.LIGHT_GRAY);
            }
        } catch (NumberFormatException e) {
            output.setText("Invalid Input!");
            scroller.setBackground(Color.LIGHT_GRAY);
        }
    }

    private void updateScroller(double value) {
        scroller.setDelay(250 + ((int)value % 500));
    }
}
