import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Editor
extends JFrame
implements  ActionListener
{
    public static void main(String[] args)
    {
        Editor window = new Editor("Editor");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.addWindowListener(new Terminator());
        window.setVisible(true);
    }

 // Class Components
    private String title;

    private int currentTab = 0;
    private boolean isDoubleBuffered = false;
    
    private JTabbedPane tabs = new JTabbedPane();

    private JPanel buttonPanel = new JPanel(new BorderLayout());
    private JButton loadButton = new JButton("Load");
    private JButton saveButton = new JButton("Save");

    public Editor(String title)
    {
        super(title);
        this.title = title;
        setMinimumSize(new Dimension(400, 400));

        setLayout(new BorderLayout());
        buttonPanel.add(loadButton, BorderLayout.WEST);
        buttonPanel.add(saveButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        add(tabs, BorderLayout.CENTER);
        newTab();
        pack();
    }

    private void newTab(File file)
    {
        String name = "Untitled";
        Tab tab = null;
        if (file == null) {
            tab = new Tab(isDoubleBuffered);
        } else {
            tab = new Tab(file, isDoubleBuffered);
            name = file.getName();
        }
        if (tabs.getTabCount() > 0) {
            tabs.insertTab(name, null, tab, "", tabs.getSelectedIndex());
        } else {
            tabs.addTab(name, tab);
        }
    }

    private void newTab() {
        newTab(null);
    }

    private void closeTab()
    {
        if (tabs.getTabCount() >= 1) {
            tabs.removeTabAt(tabs.getSelectedIndex());
        }
    }

    private void open(File file)
    {
        Tab tab = (Tab)(tabs.getSelectedComponent());
        if (tab.isFresh()) {
            tab.setFile(file);
        } else {
            newTab(file);
        }
    }

    private void save()
    {
    }

    private void saveAs()
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
 // - when opening a file, compare to those that are open. If it is one of those,
 //   give that file's tab focus
 // - when a tab gains focus, update the currentTab value

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
