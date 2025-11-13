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
    if ($amount < 0) return false;
    $balance += $amount;
    return true;
  }
}

class atm_simulator_10 {
  final private Scanner $get_input = new Scanner(System.in);
  final private bank_account $user;
  String $flash_message = "";
  public atm_simulator_10(double $initial_balance) {
    $user = new bank_account($initial_balance);
  }
  public void __show_menu__() {
    System.out.printf("""
      \t\tCurrent Balance: $%.2f\t\t%s
      \t1. Withdraw
      \t2. Deposit
      \t3. Exit
      \t\t\t> > >[ %s ]< < <
    """, $user.__view_balance__(), new Date(), $flash_message);
  }
  private void __handle_withdraw__() {
    System.out.print("\t\t[Withdraw] Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.__verify_withdraw__($amount)) $flash_message = "%.2f withdrew successfully.".formatted($amount);
      else $flash_message = "%.2f exceeds the current balance\nTransaction Failed.".formatted($amount);
    } catch (NumberFormatException err) {
      $flash_message = "Transaction Failed.\nTry again.";
    }
  }
  private void __handle_deposit__() {
    System.out.print("\t\t[Deposit] Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.__verify_deposit__($amount)) $flash_message = "%.2f depositted successfully.".formatted($amount);
      else $flash_message = "%.2f is invalid.\nTry again.".formatted($amount);
    } catch (NumberFormatException err) {
      $flash_message = "Transaction Failed.\nTry again.";
    }
  }
  public void __run__() {
    var $power_on = true;
    while ($power_on) {
      __clear_screen__();
      __show_menu__();
      System.out.print("\t\t\t>>>> ");
      String $choice = $get_input.nextLine().trim();
      switch($choice) {
        case "1" -> __handle_withdraw__();
        case "2" -> __handle_deposit__();
        case "3" -> {
          System.out.println("\t\tPOWER OFF");
          $power_on = false;
        }
      }
    }
  }
  private void __clear_screen__() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  public static void main(String[] args) {
    new atm_simulator_10(0.0).__run__();
  }
}
