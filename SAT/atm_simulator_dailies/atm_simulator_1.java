import java.util.*;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
}

public class atm_simulator_1 {
    private Scanner scanner;
    private BankAccount account;
    private String flashMessage = "";

    public atm_simulator_1(double initialBalance) {
        scanner = new Scanner(System.in);
        account = new BankAccount(initialBalance);
    }

    private void flash(String msg) {
        flashMessage = msg;
    }

    public void showMenu() {
        clearScreen(); // makes it feel like a GUI refresh
        Date today = new Date();
        System.out.printf("""
            \t\t\t[DATE]: %s
            ===================================
            | 1. Check Balance                 |
            | 2. Withdraw Cash                 |
            | 3. Deposit Cash                  |
            | 4. EXIT                          |
            ===================================
            [STATUS]: %s
            -----------------------------------
            Select option: """, today, flashMessage);
    }

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();
            flashMessage = ""; // reset flash message

            switch (choice) {
                case "1" -> flash(String.format("Current balance: %.2f", account.getBalance()));
                case "2" -> handleWithdraw();
                case "3" -> handleDeposit();
                case "4" -> {
                    flash("Exiting... Thank you!");
                    running = false;
                }
                default -> flash("Invalid choice. Please select 1–4.");
            }
        }
        scanner.close();
    }

    private void handleWithdraw() {
        try {
            System.out.print("Enter amount to withdraw: ");
            double amount = Double.parseDouble(scanner.nextLine());
            if (account.withdraw(amount))
                flash(String.format("Withdrew %.2f successfully.", amount));
            else
                flash("Failed: Invalid amount or insufficient balance.");
        } catch (NumberFormatException e) {
            flash("Invalid input. Enter numeric only.");
        }
    }

    private void handleDeposit() {
        try {
            System.out.print("Enter amount to deposit: ");
            double amount = Double.parseDouble(scanner.nextLine());
            if (account.deposit(amount))
                flash(String.format("Deposited %.2f successfully.", amount));
            else
                flash("Failed: Invalid amount.");
        } catch (NumberFormatException e) {
            flash("Invalid input. Enter numeric only.");
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner get_input = new Scanner(System.in);
        Random rng = new Random();
        int otp = rng.nextInt(9000) + 1000; // always 4 digits
        System.out.printf("Your current OTP: %d\n>>> Enter your OTP: ", otp);

        int otp_input = get_input.nextInt();
        get_input.nextLine(); // consume leftover newline

        if (otp == otp_input) {
            atm_simulator_1 atm = new atm_simulator_1(500.0);
            atm.run();
        } else {
            System.out.println("Incorrect OTP.");
        }

        get_input.close();
    }
}
