public class Account {
    private String name;
    private double balance;
    private int pin;
    private boolean access;

    public Account(String name, double balance, int pin) {
        this.name = name;
        this.balance = balance;
        this.pin = pin;
        this.access = false;
    }

    public String getName() {
        return name;
    }
    public double getBalance() {
        if (access) return balance;
        else return 0;
    }
    public boolean getAccess() { return access; }

    public void unlockWithPIN(int pin) {
        if(pin == this.pin)
            access = true;
    }

    public void logout() { this.access = false; }

    public void setBalance(double balance) {
        if (access) {
            this.balance = balance;
        }
    }

    public void deposit(double amount){
        this.setBalance(balance + amount);
    }

    public void withdraw(double amount){
        this.setBalance(balance - amount);
    }

}

