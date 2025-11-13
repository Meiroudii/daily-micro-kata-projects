import java.util.*;
class bank_account {
  private double $balance;
  public bank_account(double _balance) {
    $balance = _balance;
  }
  public double get_balance() {
    return $balance;
  }
  public boolean withdraw_audit(double _amount) {
    if(_amount > $balance || _amount <= 0) return false;
    $balance -= _amount;
    return true;
  }
  public boolean deposit_audit(double _amount) {
    if(_amount <= 0) return false;
    $balance += _amount;
    return true;
  }
}

class atm_simulator_16 {
  final Scanner $get_input = new Scanner(System.in);
  final bank_account $user;
  String $message = "";
  public atm_simulator_16(double _amount) {
    $user = new bank_account(_amount);
  }
  public void loading() {
    int l = 50;
    for (int i = 0; i <= l; i++) {
      int percent = (i*100)/l;
      String bar = "=".repeat(i)+" ".repeat(l - i);
      System.out.printf("\r\t\t[%s] %d%%", bar, percent);
      try {
        Thread.sleep(10);
      } catch(InterruptedException err) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
  public void menu() {
    System.out.printf("""
      current balance: %.2f
      \t\t%s
      1 withdraw
      2 deposit
      3 exit
      \t\t\t[%s]
      >>>>>> 
    """,$user.get_balance(), new Date(), $message);
  }
  public void run() {
    var $power_on = true;
    while($power_on) {
      cls();
      menu();
      String $choice = $get_input.nextLine().trim(); // Don't forget to slap a nextLine to this bastard
      switch($choice) {
        case "1" -> withdraw();
        case "2" -> deposit();
        case "3" -> {
          loading();
          cls();
          System.out.println("SHUT DOWN");
          $power_on = false;
        }
        default -> $message = "Invalid command";
      }
    }
  }
  private void cls() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void withdraw() {
    System.out.print("[withdraw] amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.withdraw_audit($amount)) $message = "Success.";
      else $message = "Not enough balance.";
    } catch(NumberFormatException err) {
      $message = "Transaction Failed.";
    }
  }
  private void deposit() {
    System.out.print("[deposit] amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      loading();
      if ($user.deposit_audit($amount)) $message = "Success.";
      else $message = "Invalid Transaction";
    } catch(NumberFormatException err) {
      $message = "Transaction Failed.";
    }
  }
  public static void main(String[] args) {
    Scanner $_ = new Scanner(System.in);
    Random $rng = new Random();
    var $generate_otp = 1000 + $rng.nextInt(9999);
    System.out.printf("\t\t\tEnter correct OTP\n\t\t\tVERIFY [%d]:\n\t\t\t>>> ",$generate_otp);
    try {
      var $otp_confirmation = Integer.parseInt($_.next().trim());
      if ($otp_confirmation == $generate_otp) new atm_simulator_16(0.0).run();
      else { System.out.println("Incorrect OTP"); }
    } catch(NumberFormatException err) {
      System.out.println("Incorrect OTP");
    }
    $_.close();
  }
}
