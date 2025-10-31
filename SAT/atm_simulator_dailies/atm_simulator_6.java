import java.util.*;

class bank_account {
  private double balance;

  public bank_account(double init_bal) {
    balance = init_bal;
  }
  public double get_bal() {
    return balance;
  }
  public boolean deposit(double amount) {
    if (amount <= 0) return false;
    balance += amount;
    return true;
  }
  public boolean withdraw(double amount) {
    if (amount > balance || amount <= 0) return false;
    balance -= amount;
    return true;
  }
}
public class atm_simulator_6 {
  private final Scanner in = new Scanner(System.in);
  private final bank_account user_1;
  private String flash_alert = "";
  public atm_simulator_6(double init_bal) {
    user_1 = new bank_account(init_bal);
  }
  private void print_tui(String msg) { flash_alert = msg; }
  private void menu() {
    clear_screen();
    System.out.printf("""
      [ %s
      [Current Balance: %.2f
      1. Withdraw
      2. Deposit
      3. Exit
      [!]: %s
    """, new Date(), user_1.get_bal(), flash_alert);
  }
  public void run() {
    boolean running = true;
    while(running) {
      menu();
      String c = in.nextLine().trim();
      flash_alert = "";
      switch (c) {
        case "1" -> handle_withdraw();
        case "2" -> handle_deposit();
        case "3" -> {
          print_tui("SHUTTING DOWN");
          running = false;
        }
        default -> print_tui(c+" command is invalid.");
      }
    }
  }
  private void handle_withdraw() {
    System.out.print("[Withdraw] Amount: ");
    try {
      double amount = Double.parseDouble(in.nextLine());
      if(user_1.withdraw(amount)) print_tui("Withdrew %.2f".formatted(amount));
      else print_tui("There's problem processing your transaction\nPlease try again.");
    } catch (NumberFormatException err) {
      print_tui("Enter the amount.");
    }
  }
  private void handle_deposit() {
    System.out.print("[Deposit] Amount: ");
    try {
      double amount = Double.parseDouble(in.nextLine());
      if(user_1.deposit(amount)) print_tui("Deposit %.2f".formatted(amount));
      else print_tui("There's problem processing your transaction\nPlease try again.");
    } catch(NumberFormatException err) {
      print_tui("Enter the amount.");
    }
  }
  private void clear_screen() {
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
      } catch (NumberFormatException err) {
        System.out.println("Incorrect OTP");
        continue;
      }
      if (otp == in_otp) new atm_simulator_6(0.0).run();
      else {
        System.out.println("Incorrect OTP");
        otp = 1000 + rng.nextInt(9000);
      }
    }
    sc.close();
  }
}
