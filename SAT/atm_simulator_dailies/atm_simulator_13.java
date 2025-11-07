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
    if($amount > $balance || $amount <= 0) return false;
    $balance -= $amount;
    return true;
  }
  public boolean __verify_deposit__(double $amount) {
    if($amount <= 0) return false;
    $balance += $amount;
    return true;
  }
}

class atm_simulator_13 {
  private Scanner $get_input = new Scanner(System.in);
  private bank_account $user;
  String $flash_message = "";
  public atm_simulator_13(double $initial_balance) {
    $user = new bank_account($initial_balance);
  }

  void __loading__() {
    int total = 50;
    for (var i = 0; i <= total;i++) {
      var percent = (i*100)/total;
      var bar = "=".repeat(i)+" ".repeat(total - i);
      System.out.printf("\r\t[%s] %d%%", bar, percent);
      try {
        Thread.sleep(50);
      } catch (InterruptedException err) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
  private void __clear_screen__() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  public void __show_menu__() {
    System.out.printf("""
      %s
      bal: %.2f
      %s
      1 withdraw
      2 deposit
      3. exit

      >> : 
    """, new Date(), $user.__view_balance__(), $flash_message);
  }
  public void __run__() {
    var $power_on = true;
    while($power_on) {
      __clear_screen__();
      __show_menu__();
      var $choice = $get_input.nextLine().trim();
      switch ($choice) {
        case "1" -> __handle_withdraw__();
        case "2" -> __handle_deposit__();
        case "3" -> {
          System.out.println("Power off");
          $power_on = false;
        }
        default -> $flash_message = "Error: invalid command";
      }
    }
  }
  private void __handle_deposit__() {
    System.out.print("[DEPOSIT] Enter amount: ");
    try {
      var $amount = Double.parseDouble($get_input.nextLine());
      __loading__();
      if ($user.__verify_deposit__($amount)) $flash_message = "Depositted successfully.";
      else $flash_message = "Invalid Transaction. Try again.";
    } catch (NumberFormatException err) {
      $flash_message = "Invalid Transaction";
    }
  }
  private void __handle_withdraw__() {
    System.out.print("[WITHDRAW] Enter amount: ");
    try {
      var $amount = Double.parseDouble($get_input.nextLine());
      __loading__();
      if ($user.__verify_withdraw__($amount)) $flash_message = "Withdrew successful.";
      else $flash_message = "Not enough balance.";
    } catch (NumberFormatException err) {
      $flash_message = "Invalid Transaction";
    }
  }
  public static void main(String[] args) {
    new atm_simulator_13(0.0).__run__();
  }

}

