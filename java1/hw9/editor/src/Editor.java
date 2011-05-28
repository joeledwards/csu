import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    private JMenuBar menuBar = null;

    private Confirm confirmQuit = new Confirm("Tab Contents Modified",
                                              "This tab has been modified. Do you wish to close without saving?");
    private Confirm confirmClose = new Confirm("Confirm Quit",
                                               "There are tabs with modified content. Do you wish to quit?");

    public Editor(String title)
    {
        super(title);
        this.title = title;
        setMinimumSize(new Dimension(400, 400));

        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        menuBar = createMenu();
        setJMenuBar(menuBar);
    
        setLayout(new BorderLayout());
        buttonPanel.add(loadButton, BorderLayout.WEST);
        buttonPanel.add(saveButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        add(tabs, BorderLayout.CENTER);
        newTab(null);
        pack();

        loadButton.addActionListener(this);
        saveButton.addActionListener(this); }

    private JMenuBar createMenu()
    {
        JMenuBar menuBar = new JMenuBar();

     // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem openFile = new JMenuItem("Open", KeyEvent.VK_O);
        fileMenu.add(openFile);

        JMenuItem saveFile = new JMenuItem("Save", KeyEvent.VK_S);
        fileMenu.add(saveFile);

        JMenuItem saveAsFile = new JMenuItem("Save As", KeyEvent.VK_A);
        fileMenu.add(saveAsFile);

     // Tab Menu
        JMenu tabMenu = new JMenu("Tab");
        tabMenu.setMnemonic(KeyEvent.VK_T);
        menuBar.add(tabMenu);

        JMenuItem newTab = new JMenuItem("New", KeyEvent.VK_N);
        tabMenu.add(newTab);

        JMenuItem closeTab = new JMenuItem("Close", KeyEvent.VK_C);
        tabMenu.add(closeTab);

        return menuBar;
    }

    private void newTab(File file)
    {
        Tab tab = new Tab(tabs, file, isDoubleBuffered);
        tabs.setSelectedIndex(tabs.indexOfComponent(tab));
    }

    private void closeTabAt(int index)
    {
        if (tabs.getTabCount() >= 1) {
            tabs.removeTabAt(index);
        }
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
            newTab(null);
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
