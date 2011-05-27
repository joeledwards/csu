import java.io.File;
import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Tab
extends JPanel
implements ChangeListener
{
    private JTextArea area = new JTextArea();
    private boolean fresh = true;
    private boolean saved = true;
    private File file = null;

    public Tab(boolean isDoubleBuffered)
    {
        super(new BorderLayout(), isDoubleBuffered);
        add(area, BorderLayout.CENTER);
    }

    public Tab(File file, boolean isDoubleBuffered)
    {
        super(new BorderLayout(), isDoubleBuffered);
        this.file = file;
        fresh = false;
        add(area, BorderLayout.CENTER);
    }

    public boolean isFresh()
    {
        return fresh;
    }

    public boolean isSaved()
    {
        return saved;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    private void load() {
        // read file contents to the text area
    }

    private void store() {
        // write text area contents to the file
    }

 // Delegate methods
    public void stateChanged(ChangeEvent evt) {
        Object source = evt.getSource();
    }
}
