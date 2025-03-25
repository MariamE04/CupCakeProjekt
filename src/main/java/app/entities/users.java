package app.entities;

public class users {
    private String email;
    private String password;
    private double balance;

    public users(String email, String password, double balance) {
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "users{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}
