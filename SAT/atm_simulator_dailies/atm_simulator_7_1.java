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
    if ($amount > $balance || $amount <= 0) return false;
    $balance -= $amount;
    return true;
  }
  public boolean __verify_deposit__(double $amount) {
    if ($amount <= 0) return false;
    $balance += $amount;
    return true;
  }
}

public class atm_simulator_7_1 {
  private final Scanner $get_input = new Scanner(System.in);
  private final bank_account $user;
  String $flash_message = "";
  public atm_simulator_7_1(double $initial_balance) {
    $user = new bank_account($initial_balance);
  }
  public void __run__() {
    boolean $running = true;
    while($running) {
      __menu__();
      var $choice = $get_input.nextLine().trim();
      $flash_message = "";
      switch($choice) {
        case "1" -> __handle_withdraw__();
        case "2" -> __handle_deposit__();
        case "3" -> {
          $flash_message = "POWER OFF";
          $running = false;
        }
        default -> $flash_message = "Invalid command.";
      }
    }
  }
  private void __clear_screen__() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void __handle_withdraw__() {
    System.out.print("\t\t[Withdraw] Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if($user.__verify_withdraw__($amount)) $flash_message = "%.2f withdrew successfully.".formatted($amount);
      else $flash_message = "Transaction Failed.";
    } catch (NumberFormatException err) {
      $flash_message = "Please input numerical only";
    }
  }
  private void __handle_deposit__() {
    System.out.print("\t\t[Deposit] Amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if($user.__verify_deposit__($amount)) $flash_message = "%.2f deposit successfully.".formatted($amount);
      else $flash_message = "Transaction Failed.";
    } catch (NumberFormatException err) {
      $flash_message = "Please input numerical only";
    }
  }
  public void __menu__() {
    __clear_screen__();
    System.out.printf("""
      %s
      bal: %.2f
      1. Withdraw
      2. Deposit
      3. exit
      [!] %s
    """, new Date(), $user.__view_balance__(), $flash_message);
  }
  public static void main(String[] args) {
    Scanner $otp_reader = new Scanner(System.in);
    Random $otp_randomizer = new Random();
    int $otp_field = -1;
    int $otp_generate = $otp_randomizer.nextInt(9999)+1000;
    while($otp_field != $otp_generate) {
      System.out.printf("OTP: %d \n>>>", $otp_generate);
      try {
        $otp_field = Integer.parseInt($otp_reader.nextLine());
      } catch(NumberFormatException err) {
        System.out.println("OTP not match");
      }
      if ($otp_field == $otp_generate) new atm_simulator_7_1(0.0).__run__();
      else {
        System.out.println("OTP not match");
        $otp_generate = 1000 + $otp_randomizer.nextInt(9000);
      }
    }
    $otp_reader.close();
  }
}
