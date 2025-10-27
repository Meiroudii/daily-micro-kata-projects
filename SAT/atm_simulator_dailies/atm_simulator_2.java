import java.util.*;

class Bank_Account {
    private double balance;

    public Bank_Account(double initial_balance) {
        this.balance = initial_balance;
    }

    public double get_balance() {
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

public class atm_simulator_2 {
    private Scanner get_input;
    private Bank_Account account;
    private String flash_message = "";

    public atm_simulator_2(double initial_balance) {
        get_input = new Scanner(System.in);
        account = new Bank_Account(initial_balance);
    }

    private void flash(String msg) {
        flash_message = msg;
    }

    public void show_menu() {
        clear_screen();
        Date today = new Date();
        System.out.printf("""
            \t\t\tToday: %s
            \t\t\t_____________________________
            \t\t\t 1. Check Balance
            \t\t\t 2. Withdraw Cash
            \t\t\t 3. Deposit Cash
            \t\t\t 4. EXIT
            \t\t\t=============================
            \t\t\t[=]Select a numerical option:
        """, today);
        if (!flash_message.isEmpty()) {
            System.out.println("\n[!] " + flash_message);
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            show_menu();
            String choice = get_input.nextLine().trim();
            flash_message = "";

            switch (choice) {
                case "1" -> flash(String.format("Current Balance: %.2f", account.get_balance()));
                case "2" -> handle_withdraw();
                case "3" -> handle_deposit();
                case "4" -> {
                    flash("SHUTTING DOWN...");
                    running = false;
                }
                default -> flash("Invalid choice. Select 1-4 only.");
            }
        }
        get_input.close();
    }

    private void handle_withdraw() {
        try {
            System.out.println("Enter amount:");
            double amount = Double.parseDouble(get_input.nextLine());
            if (account.withdraw(amount)) {
                flash(String.format("Withdrew %.2f successfully.", amount));
            } else {
                flash("Insufficient balance or invalid amount.");
            }
        } catch (NumberFormatException e) {
            flash("Invalid input.");
        }
    }

    private void handle_deposit() {
        try {
            System.out.println("Enter amount:");
            double amount = Double.parseDouble(get_input.nextLine());
            if (account.deposit(amount)) {
                flash(String.format("Deposited %.2f successfully.", amount));
            } else {
                flash("Invalid amount.");
            }
        } catch (NumberFormatException e) {
            flash("Invalid input.");
        }
    }

    private void clear_screen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner get_int = new Scanner(System.in);
        Random rng = new Random();

        int otp_size = 9999;
        int otp = 0;
        int otp_input = -1;

        // keep looping until user enters the right OTP
        while (otp != otp_input) {
            otp = rng.nextInt(otp_size) + 1000;
            System.out.printf("Your current OTP: %d\n>>> ", otp);
            otp_input = get_int.nextInt();
            get_int.nextLine(); // consume leftover newline

            if (otp == otp_input) {
                atm_simulator_2 atm = new atm_simulator_2(0.0);
                atm.run();
            } else {
                System.out.println("Incorrect OTP! Increasing penalty...");
                otp_size += 999999; // 🔥 OTP penalty preserved
                System.out.println("Next OTP range increased. Try again.\n");
            }
        }

        get_int.close();
    }
}
