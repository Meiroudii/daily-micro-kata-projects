import java.util.*;

class BankAccount {
  private double bal;

  public BankAccount(double initBal) { bal = initBal; }

  public double getBal() { return bal; }

  public boolean deposit(double amount) {
    if (amount <= 0) return false;
    bal += amount;
    return true;
  }

  public boolean withdraw(double amount) {
    if (amount <= 0 || amount > bal) return false;
    bal -= amount;
    return true;
  }
}

public class atm_simulator_5 {
  private final Scanner in = new Scanner(System.in);
  private final BankAccount acc;
  private String notif = "";

  public atm_simulator_5(double initBal) { acc = new BankAccount(initBal); }

  private void notify(String msg) { notif = msg; }

  private void menu() {
    clear();
    System.out.printf("""
        \t\t\t[DATE]: %s
        \t\t===================================
        \t\t| 1. Check Balance                 |
        \t\t| 2. Withdraw Cash                 |
        \t\t| 3. Deposit Cash                  |
        \t\t| 4. EXIT                          |
        \t\t===================================
        \t\t[~]: %s
        \t\t-----------------------------------
        \t\tSelect option: """, new Date(), notif);
  }

  public void run() {
    boolean running = true;
    while (running) {
      menu();
      String c = in.nextLine().trim();
      notif = "";
      switch (c) {
        case "1" -> notify(String.format("Current Balance: %.2f", acc.getBal()));
        case "2" -> withdraw();
        case "3" -> deposit();
        case "4" -> {
          notify("SHUTTING DOWN");
          running = false;
        }
        default -> notify("Invalid choice.");
      }
    }
  }

  private void withdraw() {
    System.out.print("[withdraw] Amount: ");
    try {
      double a = Double.parseDouble(in.nextLine());
      if (acc.withdraw(a)) notify("Withdrew %.2f".formatted(a));
      else notify("Incorrect input.");
    } catch (NumberFormatException e) {
      notify("Invalid input.");
    }
  }

  private void deposit() {
    System.out.print("[deposit] Amount: ");
    try {
      double a = Double.parseDouble(in.nextLine());
      if (acc.deposit(a)) notify("Deposited %.2f".formatted(a));
      else notify("Incorrect input.");
    } catch (NumberFormatException e) {
      notify("Invalid input.");
    }
  }

  private void clear() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public static void main(String[] args) {
    var rng = new Random();
    var sc = new Scanner(System.in);
    int otp = 1000 + rng.nextInt(9000);
    int inOtp = -1;

    while (otp != inOtp) {
      System.out.printf("Verify OTP: %d\n>>>> ", otp);
      try {
        inOtp = Integer.parseInt(sc.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Incorrect OTP code\n\t\tTry Again");
        continue;
      }
      if (otp == inOtp) new atm_simulator_5(0.9).run();
      else {
        System.out.println("Incorrect OTP");
        otp = 1000 + rng.nextInt(9000);
      }
    }
    sc.close();
  }
}
