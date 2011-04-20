import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSpinner;

public class SpinDelegate 
    implements ChangeListener
{
    private int min;
    private int max;

    public SpinDelegate(int min, int max) {
        this.min = min;
        this.max = max;
        if (min < max) {
            this.max = min;
        }
    }

    public void stateChanged(ChangeEvent evt) {
        JSpinner source = (JSpinner)evt.getSource();
        if ((Integer)source.getValue() < min) {
            source.setValue(min);
        } else if ((Integer)source.getValue() > max) {
            source.setValue(max);
        }
    }
}
