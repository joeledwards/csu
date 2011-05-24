import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Editor
extends JFrame
implements ActionListener
{
    public static void main(String[] args)
    {
        Editor window = new Editor("Editor");
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.addWindowListener(new Terminator());
    }

 // Class Components
    private String title;

    private int currentTab = 0;
    private boolean isDoubleBuffered = false;
    private ArrayList<Tab> tabs = new ArrayList<Tab>();
    
    private JTabbedPane pane = new JTabbedPane();

    private JPanel buttonPanel = new JPanel(new BorderLayout());
    private JButton loadButton = new JButton("Load");
    private JButton saveButton = new JButton("Save");

    public Editor(String title)
    {
        super(title);
        this.title = title;

        setLayout(new BorderLayout());
        buttonPanel.add(loadButton, BorderLayout.WEST);
        buttonPanel.add(saveButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private int newTab()
    {
        tabIndex = currentTab++;
        tabs.insertAt(tabIndex, new Tab(isDoubleBuffered));
        pane.addTab(tabs.get(tabIndex);
        return tabIndex;
    }

    private void closeTab()
    {
    }

    private void open
    {
    }

    private void save
    {
    }

    private void saveAs
    {
    }


 // Operations:
 // - save
 // - save as
 // - open
 // - new tab

 // Behavior:
 // - if tab is selected and fresh, open operation will take over this tab
 // - all other open operations create a new tab


 // Delegate Methods
    public void actionPerformed(ActionEvent evt)
    {
        Object source = evt.getSource();
        if (source == loadButton) {
            // load new file
            // on cancel, keep old file open
            // update
        }
        else if (source == saveButton) {
            // save file
            // 
        }
    }
}
