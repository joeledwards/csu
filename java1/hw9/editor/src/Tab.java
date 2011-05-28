import java.io.File;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class Tab
extends JPanel
implements ChangeListener, ActionListener
{
    private boolean fresh = true;
    private boolean saved = true;
    private File file = null;
    private JTabbedPane parent = null;
    private JPanel tabComponent = null;
    private JButton closeTabButton = null;
    private JTextArea area = new JTextArea();
    private JScrollPane scrollArea = new JScrollPane(area);

    private static ImageIcon closeTabIcon = Resources.getAsImageIcon("resources/icons/close.png", 10, 10);
    private static Dimension closeTabButtonSize = new Dimension(closeTabIcon.getIconWidth() + 4, closeTabIcon.getIconHeight() + 4);
    private static int untitledCount = 0;

    public Tab(JTabbedPane parent, File file, boolean isDoubleBuffered)
    {
        super(new BorderLayout(), isDoubleBuffered);
        assert(parent != null);
        this.parent = parent;
        this.file = file;
        add(scrollArea, BorderLayout.CENTER);

        String title = "Untitled" + ++untitledCount;
        if (file != null) {
            title = file.getName();
        }
        int index = parent.getTabCount();
        parent.insertTab(null, null, this, "", index);

        tabComponent = new JPanel(new BorderLayout());
        closeTabButton = new JButton(closeTabIcon);
        closeTabButton.setPreferredSize(closeTabButtonSize);
        tabComponent.add(new JLabel(title), BorderLayout.WEST);
        tabComponent.add(closeTabButton, BorderLayout.EAST);
        parent.setTabComponentAt(index, tabComponent);

        closeTabButton.addActionListener(this);

        if (file != null) {
            load();
        }
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

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == closeTabButton) {
            if (!isSaved()) {
                // promt: "File has been modified, do you wish to close without saving?"
            }
            parent.removeTabAt(parent.indexOfComponent(this));
        }
    }
}
