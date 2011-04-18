import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

public class Banking
    extends JApplet
    implements ActionListener
{
    public static final long serialVersionUID = 1L;

    private boolean         running = false;
    private BankDeposit     depositThread;
    private QueueNotifier<String> notifyThread;

    private JSpinner        threadCountSpinner;
    private JLabel          threadCountLabel;
    private JSpinner        delaySpinner;
    private JLabel          delayLabel;
    private JCheckBox       syncCheckBox;
    private JButton         startButton;
    private JButton         cancelButton;
    private JTextArea       viewArea;
    private JScrollPane     viewScroll;

    private JButton         hNotifyButton;

    public void init() {
        JPanel mainPanel        = new JPanel();
        BoxLayout mainLayout    = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

        JPanel threadPanel      = new JPanel();
        BoxLayout threadLayout  = new BoxLayout(threadPanel, BoxLayout.X_AXIS);
        threadCountSpinner      = new JSpinner();
        threadCountLabel        = new JLabel("Thread Count: ");
        threadPanel.add(threadCountSpinner);
        threadPanel.add(threadCountLabel);
        mainPanel.add(threadPanel);

        JPanel delayPanel       = new JPanel();
        BoxLayout delayLayout   = new BoxLayout(delayPanel, BoxLayout.X_AXIS);
        delaySpinner            = new JSpinner();
        delayLabel              = new JLabel("Sub-transaction Delay: ");
        delayPanel.add(delaySpinner);
        delayPanel.add(delayLabel);
        mainPanel.add(delayPanel);

        JPanel syncPanel        = new JPanel(new BorderLayout());
        syncCheckBox            = new JCheckBox("Synchronize");
        syncPanel.add(syncCheckBox, BorderLayout.WEST);
        mainPanel.add(syncPanel);

        JPanel controlPanel     = new JPanel(new BorderLayout());
        startButton             = new JButton("Start");
        cancelButton            = new JButton("Cancel");
        controlPanel.add(startButton, BorderLayout.WEST);
        controlPanel.add(cancelButton, BorderLayout.EAST);
        mainPanel.add(controlPanel);

        JPanel viewPanel        = new JPanel(new BorderLayout());
        viewArea                = new JTextArea();
        viewScroll              = new JScrollPane(viewArea);
        viewPanel.add(viewScroll, BorderLayout.CENTER);
        mainPanel.add(viewPanel);

        hNotifyButton = new JButton();

        getContentPane().add(mainPanel);

     // Configure widgets
        startButton.addActionListener(this);
        cancelButton.addActionListener(this);
        threadCountSpinner.addChangeListener(new SpinDelegate(1, 26));
        threadCountSpinner.setValue(2);
        delaySpinner.addChangeListener(new SpinDelegate(0, Integer.MAX_VALUE));
        delaySpinner.setValue(1000);
        syncCheckBox.setSelected(false);
        viewArea.setEditable(false);
        viewArea.setColumns(200);
        viewArea.setRows(20);

        hNotifyButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        LinkedBlockingQueue<String> ioQueue = new LinkedBlockingQueue<String>(2048);
        BankingPrinter printCore = new BankingPrinter(ioQueue);
        PrintStream printer = new PrintStream(printCore);
        Object source = evt.getSource();
        if (source == startButton) {
            if (!running) {
                viewArea.setText("");
                depositThread = new BankDeposit((Integer)threadCountSpinner.getValue(),
                                                (Integer)delaySpinner.getValue(),
                                                syncCheckBox.isSelected(),
                                                printer);
                notifyThread = new QueueNotifier<String>(ioQueue, hNotifyButton);
            }
            updateGui();
        } else if (source == cancelButton) {
            notifyThread.halt("NOW");
            updateGui();
        } else if (source == hNotifyButton) {
            String text;
            while ((text = notifyThread.take()) != null) {
                viewArea.append(text);
            }
            updateGui();
        }
    }

    public void updateGui() {
        if (running) {
            startButton.setEnabled(false);
            cancelButton.setEnabled(true);
        } else {
            startButton.setEnabled(true);
            cancelButton.setEnabled(false);
        }
    }

    private static void usage() {
        System.out.println("usage: account.jar [num_threads [delay [sync]]]");
        System.out.println("       num_threads = number of account interaction threads to run (1-26)");
        System.out.println("       delay       = number of milliseconds between operations");
        System.out.println("                     (minimum=0, default=1000)");
        System.out.println("       sync        = runs in thread safe mode if this keyword is supplied");
        System.out.println("                     (not case sensitive)");
        System.exit(1);
    }

    public static void main(String[] args) {
        int threadCount = 2;
        int delay = 1000;
        boolean synchronize = false;
        if (args.length > 3) {
            usage();
        }
        if ((args.length > 0)) {
            try {
                threadCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                usage();
            }
            if ((threadCount < 1) || (threadCount > 26)) {
                usage();
            }
        }
        if ((args.length > 1)) {
            try {
                delay = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                usage();
            }
            if (delay < 1) {
                usage();
            }
        }
        if ((args.length > 2)) {
            if (args[2].toLowerCase().equals("sync")) {
                synchronize = true;
            } else {
                usage();
            }
        }
    }
}
