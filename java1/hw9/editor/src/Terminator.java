import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Terminator
extends WindowAdapter
{
    public void windowClosing(WindowEvent evt) {
        System.exit(0);
    }
}
