package account;

public interface Account {
    public boolean deposit(long amount, String source);
    public boolean withdraw(long amount, String source);
    public boolean transfer(long amount, Account target, String source);

    public long getBalance(String source);
}
