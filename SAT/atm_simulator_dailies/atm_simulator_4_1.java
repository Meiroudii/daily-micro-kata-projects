import java.util.*;
class bank_account {
  private double _bal;
  public bank_account(double init_bal) { _bal = init_bal; }
  public double get_bal() { return _bal; }
  public boolean deposit(double amount) {
    return amount <= 0 ? false : (_bal += amount) > 0;
  }
  public boolean withdraw(double amount) {
    return (amount <= 0 || amount > _bal) ? false : (_bal -= amount) >= 0;
  }
}
public class atm_simulator_4_1 {
  private Scanner get_input;
  private bank_account account;
  private String notif_msg = "";
  public atm_simulator_4_1(double init_bal) {
    get_input = new Scanner(System.in);
    account = new bank_account(init_bal);
  }
  private void show_notif(String msg) { notif_msg = msg; }
  public void show_menu() {
    clear_screen();
    Date today = new Date();
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
        \t\tSelect option: """, today, notif_msg);
  }
  public void run() {
    boolean is_running = true;
    while(is_running) {
      show_menu();
      String choice = get_input.nextLine().trim();
      notif_msg = "";
      switch(choice) {
        case "1" -> show_notif(String.format("Current Balance: %.2f", account.get_bal()));
        case "2" -> handle_withdraw();
        case "3" -> handle_deposit();
        case "4" -> {
          show_notif("SHUTTING DOWN");
          is_running = false;
        }
        default ->  show_notif("Invalid choice.");
      } 
    }
    get_input.close();
  }
  private void handle_withdraw() {
    try {
      System.out.println("[withdraw] Amount: ");
      double amount = Double.parseDouble(get_input.nextLine());
      if(account.withdraw(amount)) {
        show_notif(String.format("Withdrew %.2f", amount));
      } else {
        show_notif("Incorrect input");
      }
    } catch (NumberFormatException err) {
      show_notif("Invalid input.");
    }
  }
  private void handle_deposit() {
    try {
      System.out.println("[deposit] Amount: ");
      double amount = Double.parseDouble(get_input.nextLine());
      if(account.deposit(amount)) {
        show_notif(String.format("Deposited %.2f", amount));
      } else {
        show_notif("Incorrect input");
      }
    } catch (NumberFormatException err) {
      show_notif("Invalid input.");
    }
  }
  private void clear_screen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  public static void main(String[] args) {
    Scanner get_otp = new Scanner(System.in);
    Random rng = new Random();
    int otp = 1000 + rng.nextInt(9000);
    int otp_input = -1;
    while (otp != otp_input) {
      System.out.printf("Verify OTP: %d\n>>>> ", otp);
      try {
        otp_input = Integer.parseInt(get_otp.nextLine());
      } catch (NumberFormatException err) {
        System.out.println("Incorrect OTP code\n\t\tTry Again");
        continue;
      }
      if (otp == otp_input) {
        atm_simulator_4_1 atm = new atm_simulator_4_1(0.9);
        atm.run();
      } else {
        System.out.println("Incorrecdt OTP");
        otp = 1000 + rng.nextInt(9000);
      }
    }
    get_otp.close();
  }
}
