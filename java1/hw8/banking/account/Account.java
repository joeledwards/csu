package account;

public interface Account {
    public boolean deposit(long amount, String source) throws InterruptedException;
    public boolean withdraw(long amount, String source) throws InterruptedException;
    public boolean transfer(long amount, Account target, String source) throws InterruptedException;

    public long getBalance(String source) throws InterruptedException;
}
