import java.util.*;

class bank_account {
  private double $balance;
  public bank_account(double _balance) {
  }
  public double get_balance() {
    return $balance;
  }
  public boolean validate_withdraw(double _amount) {
    if (_amount > $balance || _amount <= 0) return false;
    $balance -= _amount;
    return true;
  }
  public boolean validate_deposit(double _amount) {
    if (_amount <= 0) return false;
    $balance += _amount;
    return true;
  }
}

class atm_simulator_18 {
  final Scanner $get_input = new Scanner(System.in);
  final bank_account $user;
  String $message = "";
  public atm_simulator_18(double _bal) {
    $user = new bank_account(_bal);
  }
  public void menu() {
    System.out.printf("""
      1 w
      2 d
      3 e
      %s
    """, $message);
  }
  public void loading() {
    int l = 50;
    for (int i = 0; i <= l;i++) {
      int percent = (i*100)/l;
      String bar = "=".repeat(i)+".".repeat(l - i);
      System.out.printf("\r\t\t[%s] %d%%", bar,percent);
    }
    try {
      Thread.sleep(10);
    } catch(InterruptedException err) {
      Thread.currentThread().interrupt();
      return;
    }
  }
  public void run() {
    var $power_on = true;
    while($power_on) {
      cls();
      menu();
      String $choice = $get_input.nextLine().trim();
      switch($choice) {
        case "1", "w" -> handle_withdraw();
        case "2", "d" -> handle_deposit();
        case "3" -> {
          loading();
          System.out.println("System shut down");
          $power_on = false;
        }
      }
    }
  }
  private void cls() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void handle_withdraw() {
    System.out.print("amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      loading();
      if ($user.validate_withdraw($amount)) $message = "w success";
      else $message = "w failed";
    } catch(NumberFormatException err) {
      $message = "wrong";
    }
  }
  private void handle_deposit() {
    System.out.print("amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      loading();
      if ($user.validate_deposit($amount)) $message = "d success";
      else $message = "d failed";
    } catch(NumberFormatException err) {
      $message = "wrong";
    }
  }
  public static void main(String[] args) {
    new atm_simulator_18(0.0).run();
  }
}
