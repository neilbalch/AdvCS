import java.io.Serializable;

public class Account implements Comparable<Account>, Serializable {
    private String first;
    private String last;
    private int pin;
    private double balance;

    public Account(String first, String last, int pin, double balance) {
        this.first = first;
        this.last = last;
        this.pin = pin;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object oth) {
        Account o = (Account) oth;
        return o.getFirst().equalsIgnoreCase(first) && o.getLast().equalsIgnoreCase(last);
    }

    @Override
    public int compareTo(Account o) {
        int val = last.compareToIgnoreCase(o.getLast());

        if (val != 0) return val;
        else return first.compareToIgnoreCase(o.getFirst());
    }

    @Override
    public String toString() {
        return first + " " + last;
    }

    public String toStringAll() {
        return first + " " + last + " " + pin + " " + balance;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
