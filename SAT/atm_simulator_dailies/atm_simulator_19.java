import java.util.*;

class bank_account {
  public double $balance;
  public bank_account(double _balance) {
    $balance = _balance;
  }
  public double get_balance() {
    return $balance;
  }
  public boolean validate_withdraw(double _amount) {
    if(_amount > $balance || _amount <= 0) return false;
    $balance -= _amount;
    return true;
  }
  public boolean validate_deposit(double _amount) {
    if(_amount <= 0) return false;
    $balance += _amount;
    return true;
  }
}

class atm {
  private final Scanner $get_input = new Scanner(System.in);
  private final bank_account $user;
  String $message = "";
  public atm(double _balance) {
    $user = new bank_account(_balance);
  }
  public boolean validate_otp() {
    Random $rng = new Random();
    int $otp_gen = 1000+$rng.nextInt(9999);
    try {
      System.out.printf(">> %d\n\t\tInput OTP: ", $otp_gen);
      int $otp_input = Integer.parseInt($get_input.nextLine());
      if ($otp_input == $otp_gen) return true;
      else return false;
    } catch(NumberFormatException err) {
      System.out.println("Invalid Input.");
      return false;
    }
  }
  public void run() {
    if (validate_otp()) {
      var $power_on = true;
      while($power_on) {
        loading();
        cls();
        menu();
        String $choice = $get_input.nextLine();
        switch($choice) {
          case "1","withdraw" -> handle_withdraw();
          case "2","deposit" -> handle_deposit();
          case "3","e","exit" -> {
            System.out.println("Goodbye.");
            $power_on = false;
          }
          case "4","r","refresh" -> {loading(); $message = "refreshed";}
          default -> $message = "Write the numbers or menu name.";
        }
      }
    } else {
      System.out.println("Login Failed.");
    }
  }
  public void menu() {
    System.out.printf("""
      %s
      current balance: %.2f
      \t\t[%s]
      1 withdraw\t2 deposit
      _____________________
      3 exit    \t4 refresh
    """,new Date(), $user.get_balance(), $message);
  }
  private void loading() {
    int $buffer_size = 50;
    for (int $i = 0; $i <= $buffer_size; $i++) {
      int $percent = ($i*100)/$buffer_size;
      String $bar = "=".repeat($i)+".".repeat($buffer_size - $i);
      System.out.printf("\r[%s] %d%%", $bar, $percent);
      try {
        Thread.sleep(20);
      } catch(InterruptedException err) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
  private void handle_withdraw() {
    System.out.print("[withdraw] amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.validate_withdraw($amount)) $message = "withdraw success!";
      else $message = "withdraw failed";
    } catch(NumberFormatException err) {
      $message = "Command not found";
    }
  }
  private void handle_deposit() {
    System.out.print("[deposit] amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.validate_deposit($amount)) $message = "deposit success!";
      else $message = "deposit failed";
    } catch(NumberFormatException err) {
      $message = "Command not found";
    }
  }
  private void cls() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
}

class atm_simulator_19 {
  public static void main(String[] args) {
    atm $tokyo_bank = new atm(0.0);
    $tokyo_bank.run();
  }
}
