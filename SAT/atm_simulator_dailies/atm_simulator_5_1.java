import java.util.*;

class bank_account {
  private double bal;
  public bank_account(double init_bal) { bal = init_bal; }
  public double get_bal() { return bal; }
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

public class atm_simulator_5_1 {
  private final Scanner in = new Scanner(System.in);
  private final bank_account acc;
  private String notif = "";
  public atm_simulator_5_1(double init_bal) { acc = new bank_account(init_bal);}
  private void notify(String msg) { notif = msg; }
  private void menu() {
    clear();
    System.out.printf("""
      %s
      1. bal
      2. get cash
      3. give cash
      4. q
      >>> %s
    """, new Date(), notif);
  }

  public void run() {
    boolean running = true;
    while (running) {
      menu();
      String c = in.nextLine().trim();
      notif = "";
      switch(c) {
        case "1", "bal" -> notify(String.format("Current Balance: %.2f", acc.get_bal()));
        case "2", "get cash" -> withdraw();
        case "3", "give cash", "give" -> deposit();
        case "4","q" -> {
          notify("SHUTTING DOWN");
          running = false;
        }
        default -> notify("Choose using the number above. ");
      }
    }
  }

  private void withdraw() {
    System.out.print("[Withdraw] Amount: ");
    try {
      double a = Double.parseDouble(in.nextLine());
      if (acc.withdraw(a)) notify("Withdrew %.2f".formatted(a));
      else notify("Try again.");
    } catch (NumberFormatException err) {
      notify("Enter the amount.");
    }
  }
  private void deposit() {
    System.out.print("[Deposit] Amount: ");
    try {
      double a = Double.parseDouble(in.nextLine());
      if (acc.deposit(a)) notify("Deposited %.2f".formatted(a));
      else notify("Try again.");
    } catch (NumberFormatException err) {
      notify("Enter the amount: ");
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
    int in_otp = -1;

    while(otp != in_otp) {
      System.out.printf("OTP: %d\n[ENTER]: ", otp);
      try {
        in_otp = Integer.parseInt(sc.nextLine());
      } catch (NumberFormatException err)  {
        System.out.println("Incorrect OTP");
        continue;
      }
      if (otp == in_otp) new atm_simulator_5_1(0.0).run();
      else {
        System.out.println("Incorrect OTP");
        otp = 1000 + rng.nextInt(9000);
      }
    }
    sc.close();
  }
}
