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
    if ($amount > $balance || $amount < 0) return false;
    $balance -= $amount;
    return true;
  }
  public boolean __verify_deposit__(double $amount) {
    if ($amount <= 0) return false;
    $balance += $amount;
    return true;
  }
}

class atm_simulator_12 {
  private Scanner $get_input = new Scanner(System.in);
  private bank_account $user;
  String $flash_message = "";
  public atm_simulator_12(double $initial_balance) {
    $user = new bank_account($initial_balance);
  }
  public void __show_menu__() {
    System.out.printf("""
      \t\t____________________        _____________________________________________________
      \t\t|                  |  |  |  |                                                   |
      \t\t|%s        | \\___/       |     B A N K  S Y S T E M -------------------------|
      \t\t|-------------------________|___________________________________________________|
      \t\t|                           |                                                   |
      \t\t|                           |  C U R R E N T  B A L A N C E: \r$%.2f            |
      \t\t|-------------------________|___________________________________________________|
      \t\t|-------------------________|___________________________________________________|
      \t\t|________________________________________________^^^^^^_________________________|
      \t\t|                                                                               |
      \t\t|                             [1] WITHDRAW                                      |
      \t\t|                                                                               |
      \t\t|                             [2] DEPOSIT                                       |
      \t\t|                                                                               |
      \t\t|                             [3] EXIT                                          |
      \t\t|                                                                               |
      \t\t|                                                |%s|                         |
      \t\t|________________________________________________^^^^^^_________________________|
    """, new Date(), $user.__view_balance__(), $flash_message);
  }
  void __loading_animation__() {
    int total = 50;

    System.out.println("\t"+"_".repeat(60));
    System.out.println("\t"+"_".repeat(54)+"[?][-]");
    for (int i = 0; i <= total; i++) {
        int percent = (i * 100) / total;
        String bar = "=".repeat(i) + " ".repeat(total - i);
        System.out.printf("\r\t| [%s] %d%%", bar, percent);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // good practice
            System.out.println("\nLoading interrupted!");
            return;
        }
    }

    System.out.println("\nDone!");
  }
  private void __run__() {
    boolean $power_on = true;
    while ($power_on) {
      __clear_screen__();
      __show_menu__();
      System.out.print("\t\t>>>>");
      String $choice$ = $get_input.nextLine().trim();
      switch ($choice$) {
        case "1" -> __handle_withdraw__();
        case "2" -> __handle_deposit__();
        case "3" -> {
          $flash_message = "Power Off Successfully";
          $power_on = false;
        }
        default -> $flash_message = "%s is Invalid Command".formatted($choice$);
      }
    }
  }
  private void __handle_withdraw__() {
    System.out.print("\t\t[withdraw]: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.__verify_withdraw__($amount)) {
        __loading_animation__();
        $flash_message = "\t>>>>%.2f withdrew successfully".formatted($amount);
      }
      else $flash_message = "%.2f is exceeded to the current balance: %.2f".formatted($amount, $user.__view_balance__());
    } catch (NumberFormatException err) {
      $flash_message = "Invalid Transaction. Try Again.";
    }
  }
  private void __handle_deposit__() {
    System.out.print("\t\t[deposit]: ");
    try {
      double $amount = Double.parseDouble($get_input.nextLine());
      if ($user.__verify_deposit__($amount)) {
        __loading_animation__();
        $flash_message = "\t>>>>%.2f depositted successfully".formatted($amount);
      }
      else $flash_message = "Transaction cannot be process.";
    } catch (NumberFormatException err) {
      $flash_message = "Invalid Transaction. Try Again.";
    }
  }
  private void __clear_screen__() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  public static void main(String[] args) {
    new atm_simulator_12(0.0).__run__();
  }
}
