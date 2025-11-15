import java.util.*;
import java.util.random.RandomGenerator;
import java.time.*;
import java.time.format.*;

// Sealed class hierarchy with aggressive styling
sealed interface transaction_result permits transaction_result.success, transaction_result.failure {
  record success(String $message, String $emoji) implements transaction_result {}
  record failure(String $reason, String $emoji) implements transaction_result {}
}

// Enhanced account with transaction history
record bank_account(double $balance, List<String> $history) {
  
  public bank_account(double $balance) {
    this($balance, new ArrayList<>());
  }
  
  public bank_account {
    if ($balance < 0) {
      throw new IllegalArgumentException("⚠️ Balance cannot be negative");
    }
    $history = new ArrayList<>($history); // Defensive copy
  }
  
  public transaction_result validate_withdraw(double _amount) {
    if (_amount <= 0) {
      return new transaction_result.failure("Amount must be positive", "❌");
    }
    if (_amount > $balance) {
      return new transaction_result.failure("Insufficient funds", "🚫");
    }
    return new transaction_result.success("Withdrawal approved", "💸");
  }
  
  public bank_account withdraw(double _amount) {
    if (_amount > 0 && _amount <= $balance) {
      var $new_history = new ArrayList<>($history);
      $new_history.add(String.format("💸 WITHDRAW: -$%.2f", _amount));
      return new bank_account($balance - _amount, $new_history);
    }
    return this;
  }
  
  public transaction_result validate_deposit(double _amount) {
    if (_amount <= 0) {
      return new transaction_result.failure("Amount must be positive", "❌");
    }
    return new transaction_result.success("Deposit approved", "💰");
  }
  
  public bank_account deposit(double _amount) {
    if (_amount > 0) {
      var $new_history = new ArrayList<>($history);
      $new_history.add(String.format("💰 DEPOSIT: +$%.2f", _amount));
      return new bank_account($balance + _amount, $new_history);
    }
    return this;
  }
}

class atm {
  private final Scanner $get_input = new Scanner(System.in);
  private bank_account $user;
  private String $message = "";
  private int $login_attempts = 0;
  private static final RandomGenerator $rng = RandomGenerator.getDefault();
  
  // ASCII Art bank name
  private static final String $BANK_LOGO = """
    ╔═══════════════════════════════════════╗
    ║  ████████╗ ██████╗ ██╗  ██╗██╗   ██╗ ║
    ║  ╚══██╔══╝██╔═══██╗██║ ██╔╝╚██╗ ██╔╝ ║
    ║     ██║   ██║   ██║█████╔╝  ╚████╔╝  ║
    ║     ██║   ██║   ██║██╔═██╗   ╚██╔╝   ║
    ║     ██║   ╚██████╔╝██║  ██╗   ██║    ║
    ║     ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ║
    ║           TOKYO MEGABANK ATM          ║
    ╚═══════════════════════════════════════╝
    """;
  
  public atm(double _balance) {
    $user = new bank_account(_balance);
  }
  
  public boolean validate_otp() {
    System.out.println($BANK_LOGO);
    System.out.println("🔐 BIOMETRIC AUTHENTICATION REQUIRED 🔐\n");
    
    int $otp_gen = 1000 + $rng.nextInt(9000);
    try {
      System.out.print("⚡ GENERATING QUANTUM OTP");
      for (int i = 0; i < 3; i++) {
        Thread.sleep(300);
        System.out.print(".");
      }
      System.out.printf("%n%n>> 🔢 YOUR OTP: [ %d ]%n", $otp_gen);
      System.out.print("⌨️  ENTER OTP: ");
      
      int $otp_input = Integer.parseInt($get_input.nextLine());
      if ($otp_input == $otp_gen) {
        System.out.println("\n✅ ACCESS GRANTED ✅");
        Thread.sleep(500);
        return true;
      } else {
        $login_attempts++;
        System.out.printf("❌ ACCESS DENIED (Attempt %d/3)%n", $login_attempts);
        return false;
      }
    } catch(NumberFormatException err) {
      System.out.println("⚠️  INVALID INPUT FORMAT");
      return false;
    } catch(InterruptedException err) {
      Thread.currentThread().interrupt();
      return false;
    }
  }
  
  public void run() {
    if (validate_otp()) {
      var $power_on = true;
      while($power_on) {
        loading_matrix();
        cls();
        menu();
        String $choice = $get_input.nextLine();
        
        $power_on = switch($choice) {
          case "1", "withdraw", "w" -> {
            handle_withdraw();
            yield true;
          }
          case "2", "deposit", "d" -> {
            handle_deposit();
            yield true;
          }
          case "3", "e", "exit", "quit", "q" -> {
            exit_animation();
            yield false;
          }
          case "4", "r", "refresh" -> {
            loading_matrix();
            $message = "🔄 REFRESHED";
            yield true;
          }
          case "5", "h", "history" -> {
            show_history();
            yield true;
          }
          case null, default -> {
            $message = "⚠️  INVALID COMMAND - USE NUMBERS OR MENU NAMES";
            yield true;
          }
        };
      }
    } else {
      System.out.println("🔒 LOGIN FAILED - CARD RETAINED 🔒");
    }
  }
  
  public void menu() {
    var $formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    var $now = LocalDateTime.now().format($formatter);
    
    String $balance_display = String.format("$%.2f", $user.$balance());
    String $balance_bar = "█".repeat(Math.min((int)($user.$balance() / 10), 50));
    
    System.out.printf("""
      ╔═══════════════════════════════════════╗
      ║     🏦 TOKYO MEGABANK ATM v2.1 🏦    ║
      ╠═══════════════════════════════════════╣
      ║ 📅 %s         ║
      ╠═══════════════════════════════════════╣
      ║                                       ║
      ║  💵 CURRENT BALANCE: %15s  ║
      ║  [%s║
      ║                                       ║
      ╠═══════════════════════════════════════╣
      ║            📋 STATUS MESSAGE          ║
      ║  ➤ %-34s║
      ╠═══════════════════════════════════════╣
      ║  1️⃣  [W]ITHDRAW    2️⃣  [D]EPOSIT      ║
      ║  3️⃣  [E]XIT        4️⃣  [R]EFRESH      ║
      ║  5️⃣  [H]ISTORY                        ║
      ╚═══════════════════════════════════════╝
      
      💬 COMMAND: """, $now, $balance_display, $balance_bar, $message);
  }
  
  private void loading_matrix() {
    String[] $matrix_chars = {"0", "1", "█", "▓", "▒", "░"};
    int $width = 60;
    
    System.out.println("\n🔄 SYNCING WITH BLOCKCHAIN...\n");
    
    for (int $row = 0; $row < 8; $row++) {
      for (int $col = 0; $col < $width; $col++) {
        String $char = $matrix_chars[$rng.nextInt($matrix_chars.length)];
        System.out.print($char);
        try {
          Thread.sleep(2);
        } catch(InterruptedException err) {
          Thread.currentThread().interrupt();
          return;
        }
      }
      System.out.println();
    }
    
    System.out.println("\n✅ BLOCKCHAIN SYNC COMPLETE\n");
    try {
      Thread.sleep(500);
    } catch(InterruptedException err) {
      Thread.currentThread().interrupt();
    }
  }
  
  private void handle_withdraw() {
    System.out.print("\n💸 [WITHDRAW] ENTER AMOUNT: $");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      var $result = $user.validate_withdraw($amount);
      
      $message = switch($result) {
        case transaction_result.success success -> {
          System.out.print("\n🔄 PROCESSING");
          for (int i = 0; i < 5; i++) {
            Thread.sleep(200);
            System.out.print(".");
          }
          $user = $user.withdraw($amount);
          System.out.printf("%n%s WITHDRAWAL SUCCESSFUL! %s%n", success.$emoji(), success.$emoji());
          Thread.sleep(800);
          yield String.format("%s WITHDREW $%.2f", success.$emoji(), $amount);
        }
        case transaction_result.failure failure -> {
          System.out.printf("%n%s WITHDRAWAL FAILED: %s%n", failure.$emoji(), failure.$reason());
          try { Thread.sleep(1000); } catch(InterruptedException e) {}
          yield String.format("%s FAILED: %s", failure.$emoji(), failure.$reason());
        }
      };
    } catch(NumberFormatException err) {
      $message = "❌ INVALID AMOUNT FORMAT";
    } catch(InterruptedException err) {
      Thread.currentThread().interrupt();
      $message = "⚠️  OPERATION INTERRUPTED";
    }
  }
  
  private void handle_deposit() {
    System.out.print("\n💰 [DEPOSIT] ENTER AMOUNT: $");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      var $result = $user.validate_deposit($amount);
      
      $message = switch($result) {
        case transaction_result.success success -> {
          System.out.print("\n🔄 COUNTING BILLS");
          for (int i = 0; i < 5; i++) {
            Thread.sleep(200);
            System.out.print(".");
          }
          $user = $user.deposit($amount);
          System.out.printf("%n%s DEPOSIT SUCCESSFUL! %s%n", success.$emoji(), success.$emoji());
          Thread.sleep(800);
          yield String.format("%s DEPOSITED $%.2f", success.$emoji(), $amount);
        }
        case transaction_result.failure failure -> {
          System.out.printf("%n%s DEPOSIT FAILED: %s%n", failure.$emoji(), failure.$reason());
          try { Thread.sleep(1000); } catch(InterruptedException e) {}
          yield String.format("%s FAILED: %s", failure.$emoji(), failure.$reason());
        }
      };
    } catch(NumberFormatException err) {
      $message = "❌ INVALID AMOUNT FORMAT";
    } catch(InterruptedException err) {
      Thread.currentThread().interrupt();
      $message = "⚠️  OPERATION INTERRUPTED";
    }
  }
  
  private void show_history() {
    cls();
    System.out.println("""
      ╔═══════════════════════════════════════╗
      ║      📜 TRANSACTION HISTORY 📜        ║
      ╚═══════════════════════════════════════╝
      """);
    
    if ($user.$history().isEmpty()) {
      System.out.println("  ⚠️  NO TRANSACTIONS YET\n");
    } else {
      for (int i = 0; i < $user.$history().size(); i++) {
        System.out.printf("  %d. %s%n", i + 1, $user.$history().get(i));
      }
      System.out.println();
    }
    
    System.out.print("Press ENTER to return to menu...");
    $get_input.nextLine();
    $message = "📜 VIEWED HISTORY";
  }
  
  private void exit_animation() {
    cls();
    System.out.println("""
      
      ╔═══════════════════════════════════════╗
      ║                                       ║
      ║     👋 THANK YOU FOR BANKING WITH     ║
      ║          TOKYO MEGABANK! 👋           ║
      ║                                       ║
      ║      💳 PLEASE TAKE YOUR CARD 💳      ║
      ║                                       ║
      ╚═══════════════════════════════════════╝
      
      """);
    
    try {
      Thread.sleep(1500);
    } catch(InterruptedException err) {
      Thread.currentThread().interrupt();
    }
  }
  
  private void cls() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
}

class atm_simulator_20_1 {
  public static void main(String[] args) {
    var $tokyo_bank = new atm(500.0); // Starting with $500
    $tokyo_bank.run();
  }
}
