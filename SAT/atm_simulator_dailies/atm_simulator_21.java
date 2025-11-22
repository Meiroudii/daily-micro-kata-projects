import java.util.*;

class bank_acc {
  private double $balance;

  public bank_acc(double $initial_balance) {
    return $balance = $initial_balance;
  }
  public double get_bal() {
    return $balance;
  }
  public boolean verify_withdraw(double $amount) {
    if ($amount > $balance || $amount <= 0) return false;
    $balance -= $amount;
    return true;
  }
  public boolean verify_deposit(double $amount) {
    if ($amount <= 0) return false;
    $balance += $amount;
    return true;
  }
}

class atm {
  final Scanner $get_input = new Scanner(System.in);
  final bank_acc $guest;
  String $sys_msg = "";
  public atm(double $initial_balance) {
    $guest = new bank_acc($initial_balance);
  }
}

class atm_simulator_21 {
  public static void main(String[] args) {
  }
}
