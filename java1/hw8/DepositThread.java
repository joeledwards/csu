
class DepositThread extends Thread {

    Account account;
    int     deposit_amount;
    String  message;

    DepositThread(Account account, int amount, String message) {
        this.message  = message;
        this.account  = account;
        this.deposit_amount = amount;
    }

    public void run() {
        account.deposit(deposit_amount, message);
    }
}
