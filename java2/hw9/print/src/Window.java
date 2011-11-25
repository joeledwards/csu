import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Window
    extends JFrame
    implements ActionListener, ChangeListener
{

 // Class Components
    private String clipboard = null;
    private String title;
    private File defaultSaveDir = new File(".");
    private Terminator terminator = null;

    private boolean isDoubleBuffered = false;
    
    private Display display = new Display(this);

    private JPanel buttonPanel = new JPanel(new BorderLayout());
    private JButton printButton = new JButton("Print");

    private JPanel countPanel = new JPanel(new BorderLayout());
    private JButton refreshButton = new JButton("Refresh");
    private JSpinner barCountSpinner = new JSpinner();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu   = new JMenu("File");

 // File Menu Items
    private JMenuItem saveFileMenuItem   = new JMenuItem("Save",    KeyEvent.VK_S);
    private JMenuItem saveAsFileMenuItem = new JMenuItem("Save As", KeyEvent.VK_A);
    private JMenuItem printMenuItem      = new JMenuItem("Print",   KeyEvent.VK_P);
    private JMenuItem quitFileMenuItem   = new JMenuItem("Quit",    KeyEvent.VK_Q);

    public Window(String title)
    {
        super(title);
        this.title = title;
        setMinimumSize(new Dimension(400, 400));

        createMenu();
        setJMenuBar(menuBar);
    
        setLayout(new BorderLayout());
        buttonPanel.add(countPanel, BorderLayout.WEST);
        buttonPanel.add(printButton, BorderLayout.EAST);

        countPanel.add(refreshButton, BorderLayout.WEST);
        countPanel.add(barCountSpinner, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
        add(display, BorderLayout.CENTER);
        pack();

        barCountSpinner.setModel(new SpinnerNumberModel(6, 5, 32, 1));
        terminator = new Terminator(this);
        this.addWindowListener(terminator);
        refreshButton.addActionListener(this);
        printButton.addActionListener(this); 

        refresh();
    }

    public int getBarCount() {
        return (Integer)(barCountSpinner.getValue());
    }

    private void createMenu()
    {
     // File Menu
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        fileMenu.add(saveFileMenuItem);
        fileMenu.add(saveAsFileMenuItem);
        fileMenu.add(printMenuItem);
        fileMenu.add(quitFileMenuItem);
        saveFileMenuItem.addActionListener(this);
        saveAsFileMenuItem.addActionListener(this);
        printMenuItem.addActionListener(this);
        quitFileMenuItem.addActionListener(this);
    }

 // File Operations
    private void refresh()
    {
        // re-generate bars
        display.refresh();
    }

    private void save()
    {
        // save as a PNG file...
        if (display.getFile() == null) {
            saveAs();
        } else {
            display.save();
        }
    }

    private void saveAs()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As File");
        fileChooser.setCurrentDirectory(defaultSaveDir);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new PNGFileFilter());
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            defaultSaveDir = fileChooser.getCurrentDirectory();
            File file = fileChooser.getSelectedFile();
            display.saveToFile(file);
        }
    }

 // Quit the Application
    private void quit() {
        terminator.windowClosing(null);
    }

 // ActionListener
    public void actionPerformed(ActionEvent evt)
    {
        Object source = evt.getSource();
        if (source == refreshButton) {
            refresh();
        }
        else if (source == printButton) {
            print();
        }
        else if (source == printMenuItem) {
            print();
        }
        else if (source == saveFileMenuItem) {
            save();
        }
        else if (source == saveAsFileMenuItem) {
            saveAs();
        }
        else if (source == quitFileMenuItem) {
            quit();
        }
    }

 // Print
    public void print()
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable((Printable)display);
        if (job.printDialog()) {
            try {
                job.print();
            }
            catch (Exception ex) {
                System.out.printf("Print Error: %s\n", ex);
            }
        }
    }

 // StateListener
    public void stateChanged(ChangeEvent evt) {
        Object source = evt.getSource();
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
            } else if (keyCode == KeyEvent.VK_Q) {
                quit();
            } else if (keyCode == KeyEvent.VK_R) {
                refresh();
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
    }
}
