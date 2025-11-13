import java.util.*;
class bank_account {
  private double $balance;
  public bank_account(double _balance) {
    $balance = _balance;
  }
  public double get_balance() {
    return $balance;
  }
  public boolean verify_withdraw(double _amount) {
    if (_amount > $balance || _amount <= 0) return false;
    $balance -= _amount;
    return true;
  }
  public boolean verify_deposit(double _amount) {
    if (_amount <= 0) return false;
    $balance += _amount;
    return true;
  }
}

class atm_simulator_14 {
  final private Scanner $get_input = new Scanner(System.in);
  final private bank_account $user;
  String $message = "";
  public atm_simulator_14(double _balance) {
    $user = new bank_account(_balance);
  }
  public void run() {
    var $power_on = true;
    while($power_on) {
      cls();
      menu();
      String $choice = $get_input.nextLine().trim();
      switch($choice) {
        case "1" -> helper_withdraw();
        case "2" -> helper_deposit();
        case "3" -> {
          System.out.println("Power off");
          $power_on = false;
        }
        default -> $message = "Invalid Input";
      }
    }
  }
  public void menu() {
    System.out.printf("""
      %s
      balance: %.2f
      %s
      1 Withdraw
      2 Deposit
      3 Exit
      >>>>>  
    """, new Date(), $user.get_balance(), $message);
  }
  public void loading() {
    int length = 50;
    for (int i = 0; i <= length; i++) {
      int percent = (i*100)/length;
      String bar = "=".repeat(i)+" ".repeat(length - i);
      System.out.printf("\r\t\t[[%s]] %d%%", bar, percent);
      try { Thread.sleep(30); } catch(InterruptedException err) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
  public static void main(String[] args) {
    new atm_simulator_14(0.0).run();
  }
  private void cls() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void helper_withdraw() {
      loading();
    System.out.print("\n\t\t\t[withdraw] Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      loading();
      if ($user.verify_withdraw($amount)) $message = "Withdraw successfull.";
      else $message = "Not enough balance.";
    } catch(NumberFormatException err) {
      $message = "Invalid Transaction";
    }
  }
  private void helper_deposit() {
      loading();
    System.out.print("\n\t\t\t[deposit] Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      loading();
      if ($user.verify_deposit($amount)) $message = "deposit successfull.";
      else $message = "You are currently poor.";
    } catch(NumberFormatException err) {
      $message = "Invalid Transaction";
    }
  }
}
