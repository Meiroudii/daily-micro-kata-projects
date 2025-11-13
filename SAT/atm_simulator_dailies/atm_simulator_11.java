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

class atm_simulator_11 {
  final private Scanner $get_input = new Scanner(System.in);
  final private bank_account $user;
  String $flash_message = "";
  public atm_simulator_11(double $initial_balance) {
    $user = new bank_account($initial_balance);
  } 

  public void __show_menu__() {
    System.out.printf("""
      %s
      current balance: %.2f
      \t\t [ %s ]
      1. Withdraw
      2. Deposit
      3. Exit
    """, new Date(), $user.__view_balance__(), $flash_message);
  }
  public void __run__() {
    boolean $power_on = true;
    while($power_on) {
      __clear_screen__();
      __show_menu__();
      System.out.print("\t\t>>> ");
      String $choice = $get_input.nextLine().trim();
      switch($choice) {
        case "1" -> __handle_withdraw__();
        case "2" -> __handle_deposit__();
        case "3" -> {
          System.out.print("Shut down successfully.");
          $power_on = false;
        }
        default -> $flash_message = "Invalid Command.";
      }
    }
  }
  private void __clear_screen__() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void __handle_withdraw__() {
    $flash_message = "[ W I T H D R A W ]";
    System.out.print("Enter amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.__verify_withdraw__($amount)) $flash_message = "%.2f Withdrew Successfully".formatted($amount);
      else $flash_message = "Transaction Failed. Try Again";
    } catch (NumberFormatException err) {
      $flash_message = "Invalid Transaction.";
    }
  }
  private void __handle_deposit__() {
    $flash_message = "[ D E P O S I T ]";
    System.out.print("Enter amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.__verify_deposit__($amount)) $flash_message = "%.2f Deposited Successfully".formatted($amount);
      else $flash_message = "Transaction Failed. Try Again";
    } catch (NumberFormatException err) {
      $flash_message = "Invalid Transaction.";
    }
  }
  public static void main(String[] args) {
    new atm_simulator_11(0.0).__run__();
  }
}
