import java.util.*;
import java.util.random.RandomGenerator;

sealed interface transaction_result permits transaction_result.success, transaction_result.failure {
  record success(String $message) implements transaction_result {}
  record failure(String $reason) implements transaction_result {}
}
record bank_account(double $balance) {
  public bank_account {
    if ($balance < 0) {
      throw new IllegalArgumentException("Balance cannot be negative");
    }
  }
  
  public transaction_result validate_withdraw(double _amount) {
    return switch(_amount) {
      case double $amt when $amt <= 0 -> 
        new transaction_result.failure("Amount must be positive");
      case double $amt when $amt > $balance -> 
        new transaction_result.failure("Insufficient funds");
      default -> 
        new transaction_result.success("Withdrawal approved");
    };
  }
  public bank_account withdraw(double _amount) {
    if (_amount > 0 && _amount <= $balance) {
      return new bank_account($balance - _amount);
    }
    return this;
  }
  public transaction_result validate_deposit(double _amount) {
    return _amount <= 0 
      ? new transaction_result.failure("Amount must be positive")
      : new transaction_result.success("Deposit approved");
  }
  public bank_account deposit(double _amount) {
    return _amount > 0 
      ? new bank_account($balance + _amount) 
      : this;
  }
}

class atm {
  private final Scanner $get_input = new Scanner(System.in);
  private bank_account $user;
  private String $message = "";
  // Use modern RandomGenerator (Java 17+)
  private static final RandomGenerator $rng = RandomGenerator.getDefault();
  public atm(double _balance) {
    $user = new bank_account(_balance);
  }
  public boolean validate_otp() {
    int $otp_gen = 1000 + $rng.nextInt(9000); // Fixed range: 1000-9999
    try {
      System.out.printf(">> %d%n\t\tInput OTP: ", $otp_gen);
      int $otp_input = Integer.parseInt($get_input.nextLine());
      return $otp_input == $otp_gen;
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
        // Enhanced switch with pattern matching (Java 21)
        $power_on = switch($choice) {
          case "1", "withdraw" -> {
            handle_withdraw();
            yield true;
          }
          case "2", "deposit" -> {
            handle_deposit();
            yield true;
          }
          case "3", "e", "exit" -> {
            System.out.println("Goodbye.");
            yield false;
          }
          case "4", "r", "refresh" -> {
            loading();
            $message = "refreshed";
            yield true;
          }
          case null, default -> {
            $message = "Write the numbers or menu name.";
            yield true;
          }
        };
      }
    } else {
      System.out.println("Login Failed.");
    }
  }
  public void menu() {
    // Text blocks with better formatting (Java 15+)
    System.out.printf("""
      %s
      current balance: %.2f
      \t\t[%s]
      1 withdraw\t2 deposit
      _____________________
      3 exit    \t4 refresh
      """, new Date(), $user.$balance(), $message);
  }
  
  private void loading() {
    int $buffer_size = 50;
    for (int $i = 0; $i <= $buffer_size; $i++) {
      int $percent = ($i * 100) / $buffer_size;
      String $bar = "=".repeat($i) + ".".repeat($buffer_size - $i);
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
      var $result = $user.validate_withdraw($amount);
      
      // Pattern matching for switch (Java 21)
      $message = switch($result) {
        case transaction_result.success($msg) -> {
          $user = $user.withdraw($amount);
          yield "withdraw success!";
        }
        case transaction_result.failure($reason) -> 
          "withdraw failed: " + $reason;
      };
    } catch(NumberFormatException err) {
      $message = "Command not found";
    }
  }
  
  private void handle_deposit() {
    System.out.print("[deposit] amount: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      var $result = $user.validate_deposit($amount);
      
      $message = switch($result) {
        case transaction_result.success($msg) -> {
          $user = $user.deposit($amount);
          yield "deposit success!";
        }
        case transaction_result.failure($reason) -> 
          "deposit failed: " + $reason;
      };
    } catch(NumberFormatException err) {
      $message = "Command not found";
    }
  }
  
  private void cls() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
}

class atm_simulator_20 {
  public static void main(String[] args) {
    var $tokyo_bank = new atm(0.0);
    $tokyo_bank.run();
  }
}
