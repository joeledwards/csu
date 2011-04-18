import account.BankAccount;

class BankingThread
    extends Thread
{
    private BankAccount primary;
    private BankAccount secondary;

    public BankingThread(BankAccount primary, BankAccount secondary, String name) {
        super();
        setName(name);
        this.primary = primary;
        this.secondary = secondary;
    }

    public void run() {
        primary.deposit(5000, getName());
        secondary.deposit(2000, getName());
        primary.withdraw(500, getName());
        secondary.withdraw(250, getName());
        primary.transfer(250, secondary, getName());
    }
}
