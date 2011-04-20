import java.lang.Runnable;
import javax.swing.AbstractButton;

class Clicker 
implements Runnable
{
    private AbstractButton button;

    public Clicker(AbstractButton button) {
        this.button = button;
    }

    public void run() {
        button.doClick();
    }
}
