class Account {
    synchronized void deposit(int amount, String name) {
        int balance;

        System.out.println(name + " trying to deposit " + amount);

        System.out.println(name + " getting balance...");
        balance = getBalance();
        System.out.println(name + " balance got is " + balance);

        balance += amount;

        System.out.println(name + " setting balance...");
        setBalance(balance);
        System.out.println(name + " new balance set to " +
                Deposit.balance);
    }

    int getBalance() {
        try {  
            // simulate the delay in getting balance remotely
            Thread.sleep(5000);
        } catch (InterruptedException e) {}

        return Deposit.balance;
    }

    void setBalance(int balance) {
        try {  
            // simulate the delay in setting new balance remotely
            Thread.sleep(5000);
        } catch (InterruptedException e) {}

        Deposit.balance = balance;
    }
}
