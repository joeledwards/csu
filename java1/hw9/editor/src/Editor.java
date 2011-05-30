import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Editor
extends JFrame
implements ActionListener, ChangeListener, KeyListener
{
    public static void main(String[] args)
    {
        Editor window = new Editor("Editor");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setVisible(true);
    }

 // Class Components
    private String clipboard = null;
    private String title;
    private File defaultOpenDir = new File(".");
    private File defaultSaveDir = new File(".");
    private Terminator terminator = null;

    private int currentTab = 0;
    private boolean isDoubleBuffered = false;
    
    private JTabbedPane tabs = new JTabbedPane();

    private JPanel buttonPanel = new JPanel(new BorderLayout());
    private JButton loadButton = new JButton("Load");
    private JButton saveButton = new JButton("Save");

    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu   = new JMenu("File");
    private JMenu editMenu   = new JMenu("Edit");

 // File Menu Items
    private JMenuItem newFileMenuItem    = new JMenuItem("New",     KeyEvent.VK_N);
    private JMenuItem openFileMenuItem   = new JMenuItem("Open",    KeyEvent.VK_O);
    private JMenuItem saveFileMenuItem   = new JMenuItem("Save",    KeyEvent.VK_S);
    private JMenuItem closeFileMenuItem  = new JMenuItem("Close",   KeyEvent.VK_C);
    private JMenuItem saveAsFileMenuItem = new JMenuItem("Save As", KeyEvent.VK_A);
    private JMenuItem quitFileMenuItem   = new JMenuItem("Quit",    KeyEvent.VK_Q);

 // Edit Menu Items
    private JMenuItem selectAllEditMenuItem = new JMenuItem("Select All", KeyEvent.VK_A);
    private JMenuItem copyEditMenuItem   = new JMenuItem("Copy",  KeyEvent.VK_C);
    private JMenuItem cutEditMenuItem    = new JMenuItem("Cut",   KeyEvent.VK_X);
    private JMenuItem pasteEditMenuItem  = new JMenuItem("Paste", KeyEvent.VK_P);


    public Editor(String title)
    {
        super(title);
        this.title = title;
        setMinimumSize(new Dimension(400, 400));

        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        createMenu();
        setJMenuBar(menuBar);
    
        setLayout(new BorderLayout());
        buttonPanel.add(loadButton, BorderLayout.WEST);
        buttonPanel.add(saveButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        add(tabs, BorderLayout.CENTER);
        createNewTab(null);
        pack();

        terminator = new Terminator(this, tabs);
        this.addWindowListener(terminator);
        loadButton.addActionListener(this);
        saveButton.addActionListener(this); 

     // Make sure keyboard shortcut combinations work everywhere
        this.addKeyListener(this);
        this.getContentPane().addKeyListener(this);
        tabs.addKeyListener(this);
        loadButton.addKeyListener(this);
        saveButton.addKeyListener(this); 

        updateGui();
    }

    private void createMenu()
    {
     // File Menu
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        fileMenu.add(newFileMenuItem);
        fileMenu.add(openFileMenuItem);
        fileMenu.add(saveFileMenuItem);
        fileMenu.add(saveAsFileMenuItem);
        fileMenu.add(closeFileMenuItem);
        fileMenu.add(quitFileMenuItem);
        newFileMenuItem.addActionListener(this);
        openFileMenuItem.addActionListener(this);
        saveFileMenuItem.addActionListener(this);
        saveAsFileMenuItem.addActionListener(this);
        closeFileMenuItem.addActionListener(this);
        quitFileMenuItem.addActionListener(this);

     // Edit Menu
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);
        editMenu.add(selectAllEditMenuItem);
        editMenu.add(copyEditMenuItem);
        editMenu.add(cutEditMenuItem);
        editMenu.add(pasteEditMenuItem);
        selectAllEditMenuItem.addActionListener(this);
        copyEditMenuItem.addActionListener(this);
        cutEditMenuItem.addActionListener(this);
        pasteEditMenuItem.addActionListener(this);
    }

 // Tab Operations
    private void createNewTab(File file)
    {
        Tab tab = new Tab(this, tabs, file, isDoubleBuffered);
        tabs.setSelectedIndex(tabs.indexOfComponent(tab));
    }

    private void closeTabAt(int index)
    {
        if (tabs.getTabCount() >= 1) {
            tabs.removeTabAt(index);
        }
    }

    private void closeCurrentTab()
    {
        int index = tabs.getSelectedIndex();
        if (index >= 0) {
            Tab tab = (Tab)tabs.getComponentAt(index);
            tab.close();
        }
    }

    private int getOpenIndex(File file) {
        int total = tabs.getTabCount();
        Tab tab = null;
        for (int i = 0; i < total; i++) {
            tab = (Tab)tabs.getComponentAt(i);
            if ((tab.getFile() != null) && tab.getFile().equals(file)) {
                return i;
            }
        }
        return -1;
    }


 // File Operations
    private void open()
    {
        // select file to load
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");
        fileChooser.setCurrentDirectory(defaultOpenDir);
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            defaultOpenDir = fileChooser.getCurrentDirectory();
            File file = fileChooser.getSelectedFile();

            int openIndex = getOpenIndex(file);
            if (openIndex < 0) {
                Tab tab = (Tab)(tabs.getSelectedComponent());
                if ((tab != null) && tab.isFresh()) {
                    tab.setFile(file);
                } else {
                    createNewTab(file);
                }
            } else {
                tabs.setSelectedIndex(openIndex);
            }
        }
    }

    private void save()
    {
        int index = tabs.getSelectedIndex();
        if (index < 0) {
            return;
        }

        Tab tab = (Tab)tabs.getComponentAt(index);
        if (tab.getFile() == null) {
            saveAs();
        } else {
            tab.save();
        }
    }

    private void saveAs()
    {
        int index = tabs.getSelectedIndex();
        if (index < 0) {
            return;
        }

        Tab tab = (Tab)tabs.getComponentAt(index);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As File");
        fileChooser.setCurrentDirectory(defaultSaveDir);
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            defaultSaveDir = fileChooser.getCurrentDirectory();
            File file = fileChooser.getSelectedFile();
            tab.saveToFile(file);
        }
    }

 // Edit Operations
    private void selectAll()
    {
        int index = tabs.getSelectedIndex();
        if (index < 0) {
            return;
        }
        Tab tab = (Tab)tabs.getComponentAt(index);
        tab.getArea().selectAll();
    }

    private static int EDIT_OPERATION_COPY  = 1;
    private static int EDIT_OPERATION_CUT   = 2;
    private static int EDIT_OPERATION_PASTE = 3;

    private void editOperation(int operation) {
        int index = tabs.getSelectedIndex();
        if (index >= 0) {
            Tab tab = (Tab)tabs.getComponentAt(index);
            JTextArea area = tab.getArea();
            int selectionStart = area.getSelectionStart();
            int selectionEnd = area.getSelectionEnd();
            int selectionWidth = selectionEnd - selectionStart;
            int caretPosition = area.getCaretPosition();
            if (selectionWidth > 0) {
                if ((operation == EDIT_OPERATION_COPY) ||
                    (operation == EDIT_OPERATION_CUT)) {
                    clipboard = area.getSelectedText();
                    if (operation == EDIT_OPERATION_CUT) {
                        area.replaceSelection("");
                    }
                } else if (operation == EDIT_OPERATION_PASTE) {
                    area.replaceSelection(clipboard);
                }
            } else if ((operation == EDIT_OPERATION_PASTE) &&
                       (caretPosition >= 0)) {
                area.insert(clipboard, caretPosition);
            }
        }
    }

    private void copy()
    {
        editOperation(EDIT_OPERATION_COPY);
    }

    private void cut()
    {
        editOperation(EDIT_OPERATION_CUT);
    }

    private void paste()
    {
        editOperation(EDIT_OPERATION_PASTE);
    }

 // Quit the Application
    private void quit() {
        terminator.windowClosing(null);
    }

 // Keep the GUI up-to-date after various events
    public void updateGui()
    {
        int index = tabs.getSelectedIndex();
        if (index >= 0) {
            Tab tab = (Tab)tabs.getComponentAt(index);
            if (tab.isFresh()) {
                saveFileMenuItem.setEnabled(false);
                saveAsFileMenuItem.setEnabled(false);
            } else {
                saveAsFileMenuItem.setEnabled(true);
                if (tab.isSaved()) {
                    saveFileMenuItem.setEnabled(false);
                } else {
                    saveFileMenuItem.setEnabled(true);
                }
            }
            closeFileMenuItem.setEnabled(true);

            JTextArea area = tab.getArea();
            int selectionStart = area.getSelectionStart();
            int selectionEnd = area.getSelectionEnd();
            int selectionWidth = selectionEnd - selectionStart;
            if (selectionWidth > 0) {
                cutEditMenuItem.setEnabled(true);
                copyEditMenuItem.setEnabled(true);
            } else {
                cutEditMenuItem.setEnabled(false);
                copyEditMenuItem.setEnabled(false);
            }

            int caretPosition = area.getCaretPosition();
            if (((caretPosition >= 0) || (selectionWidth > 0)) && (clipboard != null)) {
                pasteEditMenuItem.setEnabled(true);
            } else {
                pasteEditMenuItem.setEnabled(false);
            }
        } else {
            closeFileMenuItem.setEnabled(false);
            saveFileMenuItem.setEnabled(false);
            saveAsFileMenuItem.setEnabled(false);
        }
    }

 // ActionListener
    public void actionPerformed(ActionEvent evt)
    {
        Object source = evt.getSource();
        if (source == loadButton) {
            open();
        }
        else if (source == saveButton) {
            save();
        }
        else if (source == openFileMenuItem) {
            open();
        }
        else if (source == saveFileMenuItem) {
            save();
        }
        else if (source == saveAsFileMenuItem) {
            saveAs();
        }
        else if (source == newFileMenuItem) {
            createNewTab(null);
        }
        else if (source == closeFileMenuItem) {
            closeCurrentTab();
        }
        else if (source == quitFileMenuItem) {
            quit();
        }
        else if (source == selectAllEditMenuItem) {
            selectAll();
        }
        else if (source == copyEditMenuItem) {
            copy();
        }
        else if (source == cutEditMenuItem) {
            cut();
        }
        else if (source == pasteEditMenuItem) {
            paste();
        }
        updateGui();
    }

 // StateListener
    public void stateChanged(ChangeEvent evt) {
        Object source = evt.getSource();
        if (source == tabs) {
            updateGui();
        }
    }

 // KeyListener
    public void keyPressed(KeyEvent evt) {
        int modifiers = evt.getModifiersEx();
        int keyCode = evt.getKeyCode();

     // Ctrl + <key>
        int onmask = KeyEvent.CTRL_DOWN_MASK;
        int offmask = KeyEvent.SHIFT_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;
        if ((modifiers & (onmask | offmask)) == onmask) {
            if (keyCode == KeyEvent.VK_S) {
                save();
            } else if (keyCode == KeyEvent.VK_A) {
                selectAll();
            } else if (keyCode == KeyEvent.VK_Q) {
                quit();
            } else if (keyCode == KeyEvent.VK_N) {
                createNewTab(null);
            } else if (keyCode == KeyEvent.VK_O) {
                open();
            } else if (keyCode == KeyEvent.VK_W) {
                closeCurrentTab();
            }
        }

     // Ctrl + Shift + <key>
        onmask = KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK;
        offmask = KeyEvent.ALT_DOWN_MASK;
        if ((modifiers & (onmask | offmask)) == onmask) {
            if (keyCode == KeyEvent.VK_S) {
                saveAs();
            }
        }

        updateGui();
    }

    public void keyReleased(KeyEvent evt) {
        updateGui();
    }

    public void keyTyped(KeyEvent evt) {
        updateGui();
    }
}
