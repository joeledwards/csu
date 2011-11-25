import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class Terminator
extends WindowAdapter
{
    private Window window = null;
    private Display display = null;

    public Terminator(Window window)
    {
        this.window = window;
    }

    public void windowClosing(WindowEvent evt) {
        window.setVisible(false);
        System.exit(0);
    }
}
