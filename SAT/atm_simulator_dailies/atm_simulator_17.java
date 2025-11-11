import java.util.*;

class bank_account {
  private double $balance;
  public bank_account(double _balance) {
    $balance = _balance;
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

class atm_simulator_17 {
  private String $username;
  final Scanner $get_input = new Scanner(System.in);
  final bank_account $user;
  String $message = "";
  public atm_simulator_17(double _balance, String _username) {
    $user = new bank_account(0.0);
    $username = _username;
  }
  public String get_username() {
    return $username;
  }
  public void menu() {
    System.out.printf("""
    Hello, %s!
    %s
    \tcurrent balance: %.2f
    1 withdraw
    2 deposit
    3 exit
    ~\t\t%s\t\t~
    """,get_username(), new Date(), $user.get_balance(), $message);
  }
  public void run() {
    var $power_on = true;
    while($power_on) {
      cls();
      menu();
      String $choice = $get_input.nextLine().trim();
      switch($choice) {
        case "1" -> withdraw();
        case "2" -> deposit();
        case "3" -> {
          System.out.println("shut down");
          $power_on = false;
        }
        default -> $message = "%s is not a valid command".formatted($choice);
      }
    }
  }
  public boolean authenticate_user() {
    Random rng = new Random();
    int generate_otp = 1000+rng.nextInt(9999);
    System.out.printf("""
      \t\t\tENTER OTP [%d]
      \t\t : 
    """, generate_otp);
    int get_otp = Integer.parseInt($get_input.nextLine());
    loading();
    if (get_otp == generate_otp) return true;
    else {
      System.out.println("Verification Failed, Try Again.");
      return false;
    }
  }
  public void loading()
  {
    int l = 50;
    for (int i = 0; i <= l; i++)
    {
      int percent = (i*100)/l;
      String bar = "=".repeat(i)+" ".repeat(l - i);
      System.out.printf("\r\t\t[%s] %d%%", bar, percent);
      try
      {
        Thread.sleep(15);
      }
      catch(InterruptedException err)
      {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
  private void cls()
  {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  private void withdraw()
  {
    double $amount = 0.0;
    System.out.print("[withdraw] amount: ");
    try
    {
      $amount = Double.parseDouble($get_input.nextLine());
      loading();
      if ($user.validate_withdraw($amount)) $message = "withdraw success.";
      else $message = "withdraw failed.";
    }
    catch(NumberFormatException err)
    {
      $message = "Transaction cannot be process";
      System.out.printf("""
        \t_________________
        \t Cause of Error:
        \t You input %.2f
      """, $amount);
    }
  }
  private void deposit()
  {
    double $amount = 0.0;
    System.out.print("[deposit] amount: ");
    try
    {
      $amount = Double.parseDouble($get_input.nextLine());
      loading();
      if ($user.validate_deposit($amount)) $message = "deposit success.";
      else $message = "deposit failed.";
    }
    catch(NumberFormatException err)
    {
      $message = "Transaction cannot be process";
      System.out.printf("""
        \t_________________
        \t Cause of Error:
        \t You input %.2f
      """, $amount);
    }
  }
  public static void main(String[] args) {
    Scanner $_ = new Scanner(System.in);
    System.out.print("\t\tusername: ");
    String _username = $_.nextLine().trim();

    atm_simulator_17 tokyo_atm = new atm_simulator_17(0.0, _username);
    /*
    if (authenticate_user()) new atm_simulator_17(0.0, _username).run();
    */
    if (tokyo_atm.authenticate_user()) tokyo_atm.run();

  }
}
