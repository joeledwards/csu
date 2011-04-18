class Deposit {
    static int balance = 1000;  // simulate the balance kept remotely

    public static void main(String[] args) {
        Account account = new Account();
        DepositThread first, second;

        first  = new DepositThread(account, 1000, "#1");
        second = new DepositThread(account, 1000, "\t\t\t\t#2");

        // start the transactions

        first.start();
        second.start();

        // wait for both transactions to finish

        try {
            first.join();
            second.join();
        } catch (InterruptedException e) {}

        // print out the final balance

        System.out.println("*** Final balance is " + balance);
    }
}
