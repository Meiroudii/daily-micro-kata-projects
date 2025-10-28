import java.util.*;
class bank_account {
  private double bal;
  public bank_account(double init_bal) { this.bal = init_bal; }
  public double get_bal() { return bal; }
  public boolean deposit(double amount) {
    return amount <= 0 ? false : (bal += amount) > 0;
  }
  public boolean withdraw(double amount) {
    return (amount <= 0 || amount > bal) ? false : (bal -= amount) >= 0;
  }
}
public class atm_simulator_3 {
  private Scanner get_input;
  private bank_account account;
  private String notif = "";
  public atm_simulator_3(double init_bal) {
    get_input = new Scanner(System.in);
    account = new bank_account(init_bal);
  }
  private void show_notif(String msg) {
    notif = msg;
  }
  public void show_menu() {
    clear_screen();
    Date today = new Date();
    System.out.printf("""
      Today: %s
      1. check cash
      2. get cash
      3. give cash
      4. quit\n>>> 
    """,today);
    if (!notif.isEmpty()) {
      System.out.println("[!] "+ notif);
    }
  }
  public void run() {
    boolean is_running = true;
    while(is_running) {
      show_menu();
      String choice = get_input.nextLine().trim();
      notif = "";
      switch (choice) {
        case "1" -> show_notif(String.format("Current Balance: %.2f", account.get_bal()));
        case "2" -> handle_withdraw();
        case "3" -> handle_deposit();
        case "4" -> {
          show_notif("SHUTTING DOWN...");
          is_running = false;
        }
        default -> show_notif("Invalid choice");
      }
    }
    get_input.close();
  }

  private void handle_withdraw() {
    try {
    System.out.println("amount: ");
    double amount = Double.parseDouble(get_input.nextLine());
    if (account.withdraw(amount)) {
    show_notif(String.format("Withdrew %.2f", amount));
    } else {
    show_notif("invalid amount");
    }
    }
    catch (NumberFormatException e) {
      show_notif("no");
    }
  }
  private void handle_deposit() {
    try {
    System.out.println("amount: ");
    double amount = Double.parseDouble(get_input.nextLine());
    if (account.deposit(amount)) {
    show_notif(String.format("Deposited %.2f", amount));
    } else {
    show_notif("invalid amount");
    }
    }
    catch (NumberFormatException e) {
      show_notif("no");
    }
  }
  private void clear_screen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public static void main(String[] args) {
    Scanner get_otp = new Scanner(System.in);
    Random rng = new Random();
    int[] otp_size = {9999, 1000};
    long otp = 0;
    int otp_input = -1;
    while (otp != otp_input) {
      otp = rng.nextInt(otp_size[0]) + otp_size[1];
      System.out.printf("""
        Match the OTP: %d\n>>> 
      """, otp);
      otp_input = get_otp.nextInt();
      get_otp.nextLine();

      if (otp == otp_input) {
        atm_simulator_3 atm = new atm_simulator_3(0.0);
        atm.run();
      } else {
        otp_size[0] += 9999999;
        otp_size[1] += 999999;
      }
    }
    get_otp.close();
  }
}
