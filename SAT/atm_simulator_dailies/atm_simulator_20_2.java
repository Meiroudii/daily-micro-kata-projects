import java.util.*;

class __bank_account {
  private double $balance;
  public __bank_account(double _balance) {
    $balance = _balance;
  }
  public double $__get_balance__() { return $balance; }
  public boolean $__validate_transaction__(double _amount, char _transaction_type) {
    switch(_transaction_type) {
      case 'w' -> {
        if (_amount > $balance) return false;
        $balance -= _amount;
        return true;
      }
      case 'd' -> {
        if (_amount <= 0) return false;
        $balance += _amount;
        return true;
      }
      default -> { System.out.println("Access Blocked"); return false;}
    }
  }
}

class __atm {
  private final Scanner $get = new Scanner(System.in);
  private final __bank_account $user;
  String $message = "";
  public __atm(double _balance) {
    $user = new __bank_account(_balance);
  }
  public boolean $__validate_otp__() {
    Random $rng = new Random();
    int $generate_otp = 1000+$rng.nextInt(9999);
    try {
      System.out.printf("Your OTP [%d]\n\t\t>>>", $generate_otp);
      int $otp_field = Integer.parseInt($get.nextLine());
      if ($otp_field == $generate_otp) return true;
      else return false;
    } catch(NumberFormatException err) {
      System.out.println("Invalid Input.");
      return false;
    }
  }
  public void $__show_menu__() {
    Date $today = new Date();
    System.out.printf("""
      \n\n\n\n\n
      \t\t\t_________________________________
      \t\t\t_____YOKOHAMA_BANK_______________
      \t\t\t_________________________________
      \t\t\t|--------------------------------
      \t\t\t| %s
      \t\t\t| current balance: %,.2f
      \t\t\t|                             %tr
      \t\t\t| 1 WITHDRAW        2 DEPOSIT
      \t\t\t| 3 REFRESH         4  EXIT
      \t\t\t|
      \t\t\t| %s
      \t\t\t|________________________________
      \t\t\t|
      \t\t\t| >>>>>>>>>>>[:
    """, $today,$user.$__get_balance__(), $today, $message);
  }
  public void $__run__() {
    if ($__validate_otp__()) {
      var $power = true;
      while($power) {
        $__loading__(15);
          $__cls__();
        $__show_menu__();
        var $choice = $get.nextLine().trim();
        switch($choice) {
          case "1" -> $__handle_withdraw__();
          case "2" -> $__handle_deposit__();
          case "3" -> $__loading__(15);
          case "4" -> {
            $__loading__(40);
            System.out.println("Goodbye");
            $power = false;
          }
          default -> $message = "Invalid Command";
        }
      }
    }
    else {
      System.out.println("Authentication Failed.");
    }
  }
  private void $__handle_withdraw__() {
    System.out.print("[withdraw] amount: $");
    try {
      double $amount = Double.parseDouble($get.nextLine());
      $__loading__(25);
      if ($user.$__validate_transaction__($amount, 'w')) $message = "Withdraw success!";
      else $message = "Withdraw Failed";
    } catch(NumberFormatException err) {
      $message = "Transaction Failed.";
    }
  }
  private void $__handle_deposit__() {
    System.out.print("[deposit] amount: $");
    try {
      double $amount = Double.parseDouble($get.nextLine());
      $__loading__(15);
      if ($user.$__validate_transaction__($amount, 'd')) $message = "deposit success!";
      else $message = "deposit Failed";
    } catch(NumberFormatException err) {
      $message = "Transaction Failed.";
    }
  }
  private void $__cls__() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void $__loading__(int _speed) {
    int $buf = 50;
    for (int $index = 0; $index <= $buf; $index++) {
      int $percent = ($index*100)/$buf;
      String $bar = "=".repeat($index)+"-".repeat($buf - $index);
      System.out.printf("\r\t\t[%s] %d%%", $bar, $percent);
      try {
        Thread.sleep(_speed);
      } catch(InterruptedException err) {
        Thread.currentThread().interrupt();
        return;
        }
      }
  }
}

class atm_simulator_20_2 { public static void main(String[] args) {
  __atm $yoko_bank = new __atm(0.0);$yoko_bank.$__run__();
}
}
