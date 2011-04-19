package account;

import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.UUID;

public class BankAccount
    implements Account
{
    private static int nextID = 1;
    public final int ID = nextID++;

    private GregorianCalendar cal = new GregorianCalendar();
    private volatile long balance = 0;
    private int delay = 0;
    private boolean sync = true;
    private PrintStream stream = System.out;

    public BankAccount() {}

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setStream(PrintStream stream) {
        this.stream = stream;
    }

    public boolean getSync() {
        return sync;
    }

    public int getDelay() {
        return delay;
    }

    public PrintStream getStream() {
        return stream;
    }

    public boolean deposit(long amount, String source) 
    throws InterruptedException
    {
        stream.printf("%s [REQUEST] %d> Deposit %d\n", source, ID, amount);
        AccountOperation op = new AccountOperation();
        op.action = AccountAction.DEPOSIT;
        op.value = amount;
        AccountResult r;
        if (this.sync) {
            r = performOperation(op, source);
        } else {
            r = performOperationUnsync(op, source);
        }
        return r.success;
    }

    public boolean withdraw(long amount, String source) 
    throws InterruptedException
    {
        stream.printf("%s [REQUEST] %d> Withdraw %d\n", source, ID, amount);
        AccountOperation op = new AccountOperation();
        op.action = AccountAction.WITHDRAW;
        op.value = amount;
        AccountResult r;
        if (this.sync) {
            r = performOperation(op, source);
        } else {
            r = performOperationUnsync(op, source);
        }
        return r.success;
    }

    public long getBalance(String source) 
    throws InterruptedException
    {
        stream.printf("%s [REQUEST] %d> Balance statement.\n", source, ID);
        AccountOperation op = new AccountOperation();
        op.action = AccountAction.BALANCE;
        AccountResult r;
        if (this.sync) {
            r = performOperation(op, source);
        } else {
            r = performOperationUnsync(op, source);
        }
        return r.balance;
    }

    public boolean transfer(long amount, Account target, String source) 
    throws InterruptedException
    {
        boolean success = false;
        stream.printf("%s [REQUEST] %d> Transfer %d to account %d\n", source, ID, amount, ((BankAccount)target).ID);
        // Attempt to withdraw funds from our account
        stream.printf("%s [TRANSFER] %d> Attempting withdrawl...\n", source, ID, amount, ((BankAccount)target).ID);
        if (this.withdraw(amount, source)) {
            stream.printf("%s [TRANSFER] %d> Withdrawl succeeded. Attempting deposit to %d...\n", source, ID, ((BankAccount)target).ID);
            // If we successfully withdraw, then attempt to deposit to the target
            if (!target.deposit(amount, source)) {
                stream.printf("%s [TRANSFER] %d> Deposit to %d failed. Transfer cancelled.\n", source, ID, ((BankAccount)target).ID);
                // If we cannot deposit to the target, we re-deposit to our own account
                this.deposit(amount, source);
            } else {
                stream.printf("%s [TRANSFER] %d> Deposit to %d succeeded. Transfer complete.\n", source, ID, ((BankAccount)target).ID);
                success = true;
            }
        } else {
            stream.printf("%s [TRANSFER] %d> Withdrawl failed. Transfer cancelled.\n", source, ID);
        }
        return success;
    }

    /**
     * Perform an atomic operation. All other operations pass through this 
     * method in order to ensure dependent data is synchronized. This method
     * can perform queries and return multi-value responses rather than the 
     * primitive returned by the methods declared by the Account interface.
     * 
     * @param op    The operation to perform on this account. 
     *              AccountOperation.action is the action that should be 
     *              taken (one of thos defined in the AccountAction enum).
     *              AccountOperation.value is the amount applied to the
     *              action (not used for balance inquiries).
     *
     * @return      Result.balance is always the balance of this account
     *              after the requetsed operation has been performed.
     *              Result.success communicates whether or not the action
     *              was performed successfully. The meaning of R.value is the
     *              amount of the modification (0 if the operation failed).
     */
    public synchronized AccountResult performOperation(AccountOperation op, String source) 
    throws InterruptedException
    {
        return performOperationUnsync(op, source);
    }

    public AccountResult performOperationUnsync(AccountOperation op, String source) 
    throws InterruptedException
    {
        AccountResult r = new AccountResult();
        r.action = op.action;
        switch (op.action) {
            case BALANCE:
                r.success = true;
                r.value = balance;
                sleep(delay);
                r.balance = balance;
                break;
            case DEPOSIT:
                stream.printf("%s [DEPOSIT] %d> Original balance is %d\n", source, ID, balance);
                sleep(delay);
                stream.printf("%s [DEPOSIT] %d> Determining if deposit amount is valid...\n", source, ID);
                if (op.value > 0) {
                    stream.printf("%s [DEPOSIT] %d> Deposit amount is valid.\n", source, ID);
                    sleep(delay);
                    long new_balance = balance;
                    sleep(delay);
                    stream.printf("%s [DEPOSIT] %d> Calculating new balance...\n", source, ID);
                    new_balance = new_balance + op.value;
                    sleep(delay);
                    stream.printf("%s [DEPOSIT] %d> Balance calculated (%d + %d = %d).\n", source, ID, balance, op.value, new_balance);
                    sleep(delay);
                    stream.printf("%s [DEPOSIT] %d> Setting new balance to %d...\n", source, ID, new_balance);
                    sleep(delay);
                    balance = new_balance;
                    stream.printf("%s [DEPOSIT] %d> Balance is now %d.\n", source, ID, new_balance);
                    r.value = op.value;
                    r.success = true;
                } else {
                    stream.printf("%s [DEPOSIT] %d> Deposit amount is invalid.\n", source, ID);
                    r.balance = balance;
                    r.value = 0;
                    r.success = false;
                }
                r.balance = balance;
                break;
            case WITHDRAW:
                stream.printf("%s [WITHDRAW] %d> Original balance is %d\n", source, ID, balance);
                sleep(delay);
                stream.printf("%s [WITHDRAW] %d> Determining if funds are available...\n", source, ID);
                sleep(delay);
                if (balance >= op.value) {
                    stream.printf("%s [WITHDRAW] %d> Funds are available.\n", source, ID);
                    sleep(delay);
                    long new_balance = balance;
                    sleep(delay);
                    stream.printf("%s [WITHDRAW] %d> Calculating new balance...\n", source, ID);
                    new_balance = new_balance - op.value;
                    sleep(delay);
                    stream.printf("%s [WITHDRAW] %d> Balance calculated (%d - %d = %d).\n", source, ID, balance, op.value, new_balance);
                    sleep(delay);
                    stream.printf("%s [WITHDRAW] %d> Setting new balance to %d...\n", source, ID, new_balance);
                    sleep(delay);
                    balance = new_balance;
                    stream.printf("%s [WITHDRAW] %d> Balance is now %d.\n", source, ID, new_balance);
                    r.success = true;
                    r.value = op.value;
                } else {
                    stream.printf("%s [WITHDRAW] %d> Insufficient funds.\n", source, ID);
                    r.value = 0;
                    r.success = false;
                }
                r.balance = balance;
                break;
        }
        r.timestamp = cal.getTimeInMillis();
        return r;
    }

    private static void sleep(int delay) 
    throws InterruptedException
    {
        if (delay > 0) {
            Thread.sleep(delay);
        }
    }
}
