import java.util.Scanner;
import java.util.Random;
import java.util.Date;

class bank_account {
  private double $balance;
  public bank_account(double _balance) {
    $balance = _balance;
  }
  public double get_balance() {
    return $balance;
  }
  public boolean audit_withdraw(double _amount) {
    if(_amount > $balance || _amount < 0) return false;
    $balance -= _amount;
    return true;
  }
  public boolean audit_deposit(double _amount) {
    if(_amount <= $balance) return false;
    $balance += _amount;
    return true;
  }
}

class atm_simulator_15 {
  final Scanner $get_input = new Scanner(System.in);
  final bank_account $user;
  String $message = "";
  public atm_simulator_15(double _amount) {
    $user = new bank_account(_amount);
  }

  public void run() {
    var $power_on = true;
    while($power_on) {
      cls();
      menu();
      String $choice = $get_input.nextLine().trim();
      switch ($choice) {
        case "1" -> withdraw_helper();
        case "2" -> deposit_helper();
        case "3" -> {
          System.out.println("SHUT DOWN");
          $power_on = false;
        }
        default -> $message = "Invalid input";
      }
    }
  }
  public void menu() {
    System.out.printf("""
      current balance: %.2f\t\t%s
      1 withdraw
      2 deposit
      3 exit
      [ %s ]
      >>>> 
    """, $user.get_balance(), new Date(), $message);
  }
  void __loading__() {
    int l = 50;
    for (int i = 0; i <= l; i++) {
      int percent = (i*100)/l;
      String bar = "=".repeat(i)+" ".repeat(l - i);
      System.out.printf("\r\t\t[[%s]] %d%%", bar, percent);
      try {
        Thread.sleep(15);
      } catch(InterruptedException err) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
  private void cls() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void withdraw_helper() {
    System.out.print("[withdraw] amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      __loading__();
      if($user.audit_withdraw($amount)) $message = "withdrew success!";
      else $message = "Transaction failed.";
    } catch(NumberFormatException err) {
      $message = "Invalid Transaction Try Again.";
    }
  }
  private void deposit_helper() {
    System.out.print("[deposit] amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      __loading__();
      if($user.audit_deposit($amount)) $message = "deposit success!";
      else $message = "Transaction failed.";
    } catch(NumberFormatException err) {
      $message = "Invalid Transaction Try Again.";
    }
  }
  public static void main(String[] args) {
    new atm_simulator_15(0.0).run();
  }
}


