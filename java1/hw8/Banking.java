import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class Banking
    extends JApplet
    implements ActionListener
{
    public static final long serialVersionUID = 1L;

    private boolean         running = false;
    private BankDeposit     depositThread;
    private QueueUpdater    notifyWorker;

    private JSpinner        threadCountSpinner;
    private JLabel          threadCountLabel;
    private JSpinner        delaySpinner;
    private JLabel          delayLabel;
    private JCheckBox       syncCheckBox;
    private JButton         startButton;
    private JButton         cancelButton;
    private JTextArea       viewArea;
    private JScrollPane     viewScroll;

    private JButton         hCompleteButton;

    public void init() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel oSpinPanel       = new JPanel(new BorderLayout());
        JPanel spinPanel        = new JPanel(new GridLayout(2, 2));
        threadCountSpinner      = new JSpinner();
        threadCountLabel        = new JLabel("Thread Count: ");
        spinPanel.add(threadCountLabel);
        spinPanel.add(threadCountSpinner);

        delaySpinner            = new JSpinner();
        delayLabel              = new JLabel("Sub-transaction Delay: ");
        spinPanel.add(delayLabel);
        spinPanel.add(delaySpinner);
        oSpinPanel.add(spinPanel, BorderLayout.WEST);
        add(oSpinPanel);

        JPanel syncPanel        = new JPanel(new BorderLayout());
        syncCheckBox            = new JCheckBox("Synchronize");
        syncPanel.add(syncCheckBox, BorderLayout.WEST);
        add(syncPanel);

        JPanel controlPanel     = new JPanel(new BorderLayout());
        startButton             = new JButton("Start");
        cancelButton            = new JButton("Cancel");
        controlPanel.add(startButton, BorderLayout.WEST);
        controlPanel.add(cancelButton, BorderLayout.EAST);
        add(controlPanel);

        JPanel viewPanel        = new JPanel(new BorderLayout());
        viewArea                = new JTextArea();
        viewScroll              = new JScrollPane(viewArea);
        viewPanel.add(viewScroll, BorderLayout.CENTER);
        add(viewPanel);

        hCompleteButton = new JButton();

     // Configure widgets
        startButton.addActionListener(this);
        cancelButton.addActionListener(this);
        threadCountSpinner.setModel(new SpinnerNumberModel(2, 1, 26, 1));
        delaySpinner.setModel(new SpinnerNumberModel(1000, 0, 60000,10));
        syncCheckBox.setSelected(false);
        viewArea.setEditable(false);
        viewArea.setColumns(200);
        viewArea.setRows(100);

        hCompleteButton.addActionListener(this);

        updateGui();
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == startButton) {
            LinkedBlockingQueue<String> ioQueue = new LinkedBlockingQueue<String>(2048);
            BankingPrinter printCore = new BankingPrinter(ioQueue, false /* Mirror to STDOUT */);
            PrintStream printer = new PrintStream(printCore, true /* Auto-Flush */);
            if (!running) {
                viewArea.setText("");
                depositThread = new BankDeposit((Integer)threadCountSpinner.getValue(),
                                                (Integer)delaySpinner.getValue(),
                                                syncCheckBox.isSelected(),
                                                printer,
                                                hCompleteButton);
                notifyWorker = new QueueUpdater(ioQueue, viewArea);
                notifyWorker.execute();
                depositThread.start();
                running = true;
            }
            updateGui();
        } else if (source == cancelButton) {
            if (running) {
                try {
                    if (depositThread != null) {
                        depositThread.halt();
                        depositThread.join();
                        depositThread = null;
                    }
                } catch (InterruptedException e) {;}
                if (notifyWorker != null) {
                    notifyWorker.halt();
                    notifyWorker = null;
                }
                running = false;
            }
            updateGui();
        } else if (source == hCompleteButton) {
            if (notifyWorker != null) {
                notifyWorker.halt();
                notifyWorker = null;
            }
            running = false;
            updateGui();
        }
    }

    public void updateGui() {
        if (running) {
            threadCountSpinner.setEnabled(false);
            delaySpinner.setEnabled(false);
            syncCheckBox.setEnabled(false);
            startButton.setEnabled(false);
            cancelButton.setEnabled(true);
        } else {
            threadCountSpinner.setEnabled(true);
            delaySpinner.setEnabled(true);
            syncCheckBox.setEnabled(true);
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
        if (args.length == 0) {
            JFrame frame = new JFrame("Banking");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(500, 500));
            frame.setMinimumSize(new Dimension(500,500));

            JApplet applet = new Banking(); 
            applet.init();
            applet.start();
            frame.add("Center", applet);
            frame.pack();
            frame.setVisible(true);
            return;
        }
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

        BankDeposit depositThread = new BankDeposit(threadCount, delay, synchronize, System.out);
        depositThread.start();
        try {
            depositThread.join();
        } catch (InterruptedException e) {;}
    }
}
