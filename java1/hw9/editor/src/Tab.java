import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Tab
extends JPanel
{
    private JTextArea area = new JTextArea();

    public Tab(boolean isDoubleBuffered)
    {
        super(new BorderLayout(), isDoubleBuffered);
    }

    private void _init()
    {
        add(area, BorderLayout.CENTER);
    }
}
