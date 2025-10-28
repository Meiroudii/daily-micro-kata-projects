interface factorial_calculator {
  long calculate(int n);
}
class recursive_factorial_calculator implements factorial_calculator {
  @Override
  public long calculate(int n) {
    if (n <= 1) return 1;
    return n * calculate(n - 1);
  }
}
class factorial_calculator_factory {
  public static factorial_calculator create_calculator() {
    return new recursive_factorial_calculator();
  }
}
class factorial_service {
  private final factorial_calculator calculator;
  public factorial_service(factorial_calculator calculator) {
    this.calculator = calculator;
  }
  public long compute_factorial(int n) {
    return calculator.calculate(n);
  }
}
public class factorial_2_1 {
  public static void main(String[] args) {
    factorial_calculator calculator = factorial_calculator_factory.create_calculator();
    factorial_service service = new factorial_service(calculator);
    long result = service.compute_factorial(5);
    System.out.printf("""
      \t\t\t\t_________________________________
      \t\t\t\t|                               |
      \t\t\t\t|             %d              |
      \t\t\t\t|_______________________________|
    """,result);
  }
}
