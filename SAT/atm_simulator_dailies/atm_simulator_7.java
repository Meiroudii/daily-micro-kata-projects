import java.util.*;

class user_account {
  private double balance;
  public user_account(double initial_balance) {
    balance = initial_balance;
  }
  public double get_balance() {
    return balance;
  }
  public boolean verify_withdraw(double amount) {
    if (amount > balance || amount <= 0) return false;
    balance -= amount;
    return true;
  }
  public boolean verify_deposit(double amount) {
    if (amount <= 0) return false;
    balance += amount;
    return true;
  }
}

public class atm_simulator_7 {
  private final Scanner get_input = new Scanner(System.in);
  private final user_account user;
  private String flash_alert = "";
  public atm_simulator_7(double initial_balance) {
    user = new user_account(initial_balance);
  }
  private void print_ui(String str) {
    flash_alert = str;
  }
  private void menu() {
    clear_screen();
    System.out.printf("""
      [] %s
      [Current Balance: %.2f
      1 Withdraw
      2 Deposit
      3 Exit
      4 Refresh
      [!] %s
    """, new Date(), user.get_balance(), flash_alert);
  }
  private void clear_screen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void handle_withdraw() {
    System.out.print("[Withdraw]");
    try {
      double amount = Double.parseDouble(get_input.nextLine());
      if (user.verify_withdraw(amount)) print_ui("Withdrew %.2f".formatted(amount));
      else print_ui("Please Try Again.");
    }catch (NumberFormatException err) {
    }
  }
  private void handle_deposit() {
    System.out.print("[Deposit] Amount: ");
    try {
      double amount = Double.parseDouble(get_input.nextLine());
      if (user.verify_deposit(amount)) print_ui("Deposit %.2f".formatted(amount));
      else print_ui("%.2f is an invalid input\nTry again.".formatted(amount));
    } catch (NumberFormatException err) {
      print_ui("Enter the proper amount.");
    }
  }
  public void run() {
    boolean $running = true;
    while($running) {
      menu();
      String $choice = get_input.nextLine().trim();
      flash_alert = "";
      switch ($choice) {
        case "1" -> handle_withdraw();
        case "2" -> handle_deposit();
        case "3" -> {
          print_ui("POWER OFF.");
          $running = false;
        }
        case "4" -> {
          clear_screen();
          print_ui("UI Refreshed.");
        }
        default -> print_ui("%s is not a command".formatted($choice));
      }
    }
  }
  public static void main(String[] args) {
    var rng_otp = new Random();
    var get_otp = new Scanner(System.in);
    int generate_otp = 1000 + rng_otp.nextInt(9000);
    int verify_otp = -1;

    while(generate_otp != verify_otp) {
      System.out.printf("OTP: %d\n[ENTER]: ", generate_otp);
      try {
        verify_otp = Integer.parseInt(get_otp.nextLine());
      } catch (NumberFormatException err) {
        System.out.printf("%d is not a valid input.", verify_otp);
      }
      if (generate_otp == verify_otp) new atm_simulator_7(0.0).run();
      else {
        System.out.println("Incorrect OTP, Try Again");
        generate_otp = 1000 + rng_otp.nextInt(9000);
      }
    }
    get_otp.close();
  }
}
