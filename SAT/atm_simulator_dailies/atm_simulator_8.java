import java.util.*;

class bank_account {
  private double $balance;
  public bank_account(double $initial_balance) {
    $balance = $initial_balance;
  }
  public double __view_balance__() {
    return $balance;
  }
  public boolean __verify_withdraw__(double $amount) {
    if ($amount > $balance || $amount < 0) return false;
    $balance -= $amount;
    return true;
  }
  public boolean __verify_deposit__(double $amount) {
    if ($amount <= 0) return false;
    $balance += $amount;
    return true;
  }
}

class atm_simulator_8 {
  final private Scanner $get_input = new Scanner(System.in);
  private bank_account $guest_user;
  String $flash_message = "";
  public atm_simulator_8(double $initial_balance) {
    $guest_user = new bank_account($initial_balance);
  }
  public void __show_menu__() {
    System.out.printf("""
      \t\t\t\t\t%s
      current balance: $ %.2f\t\t\t %s
      1 Withdraw
      2 Deposit
      3 Exit
    """, new Date(), $guest_user.__view_balance__(), $flash_message);
  }
  private void __run__() {
    boolean $running = true;
    while ($running) {
      __clear_screen__();
      __show_menu__();
      String $choice = $get_input.nextLine().trim();
      switch($choice) {
        case "1" -> __handle_withdraw__();
        case "2" -> __handle_deposit__();
        case "3" -> {
          System.out.println("SHUTTING DOWN...");
          $running = false;
        }
        default -> $flash_message = "%s command not found".formatted($choice);
      }
    }
  }
  private void __handle_deposit__() {
    System.out.println("\t\t\t[DEPOSIT] Enter Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if($guest_user.__verify_deposit__($amount)) $flash_message = "%.2f has been deposited".formatted($amount);
      else $flash_message = "Nothing added.";
    } catch(NumberFormatException err) {
      $flash_message = "Transaction Failed.";
    }
  }
  private void __handle_withdraw__() {
    System.out.println("\t\t\t[WITHDRAW] Enter Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if($guest_user.__verify_withdraw__($amount)) $flash_message = "%.2f has been withdrew".formatted($amount);
      else $flash_message = "Invalid Transaction.";
    } catch(NumberFormatException err) {
      $flash_message = "Transaction Failed.";
    }
  }
  private void __clear_screen__() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  public static void main(String[] args) {
    new atm_simulator_8(0.0).__run__();
    System.out.println("Status: Power off");
  }
  $get_input.close();
}
