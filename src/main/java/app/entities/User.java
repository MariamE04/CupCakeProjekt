package app.entities;

public class User {
    private String email;
    private String password;
    private double balance;
    private boolean isAdmin;

    public User(String email, String password, double balance) {
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.isAdmin = true;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isAdmin() {
        return true;
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
