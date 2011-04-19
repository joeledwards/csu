import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;

import account.AccountResult;
import account.AccountAction;
import account.BankAccount;
import account.AccountOperation;

class BankDeposit 
    extends Thread
{
    private int threadCount;
    private int delay;
    private boolean synchronize;
    private PrintStream stream;
    private BankingThread[] threads;
    private AbstractButton complete;


    public BankDeposit(int threadCount, int delay, boolean synchronize, PrintStream stream, AbstractButton complete) {
        this.threadCount = threadCount;
        this.delay = delay;
        this.synchronize = synchronize;
        this.stream = stream;
        this.complete = complete;
    }

    public BankDeposit(int threadCount, int delay, boolean synchronize, PrintStream stream) {
        this.threadCount = threadCount;
        this.delay = delay;
        this.synchronize = synchronize;
        this.stream = stream;
        this.complete = null;
    }

    public void run() {
        BankAccount account1 = new BankAccount();
        account1.setSync(synchronize);
        account1.setDelay(delay);
        account1.setStream(stream);
        BankAccount account2 = new BankAccount();
        account2.setSync(synchronize);
        account2.setDelay(delay);
        account2.setStream(stream);

        threads = new BankingThread[threadCount];

        String leadingTabs = "";
        for (int i=0; i < threadCount; i++) {
            threads[i] = new BankingThread(account1, account2, leadingTabs + (char)(65+i));
            // start the transaction
            threads[i].start();
            leadingTabs += "\t";
        }

        // wait for all transactions to finish
        try {
            for (int i=0; i < threadCount; i++) {
                threads[i].join();
            }
            // print out the final balances
            stream.println("Account " +account1.ID+ " final balance: " + account1.getBalance("main"));
            stream.println("Account " +account2.ID+ " final balance: " + account2.getBalance("main"));
            if (complete != null) {
                SwingUtilities.invokeLater(new Clicker(complete));
            }
        } catch (InterruptedException e) {
            for (int i=0; i < threadCount; i++) {
                threads[i].halt();
            }
            for (int i=0; i < threadCount; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e2) {;}
            }
        }
    } // run()

    public void halt() {
        this.interrupt();
    }

} // class BankDeposit
