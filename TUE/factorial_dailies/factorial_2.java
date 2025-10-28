// Interface for calculating factorials
interface FactorialCalculator {
    long calculate(int n);
}

// Recursive implementation of the factorial calculation
class RecursiveFactorialCalculator implements FactorialCalculator {
    @Override
    public long calculate(int n) {
        if (n <= 1) return 1;
        return n * calculate(n - 1);
    }
}

// Factory class to create the appropriate calculator
class FactorialCalculatorFactory {
    public static FactorialCalculator createCalculator() {
        return new RecursiveFactorialCalculator();
    }
}

// Service that handles the calculation logic (just to add another layer)
class FactorialService {
    private final FactorialCalculator calculator;

    public FactorialService(FactorialCalculator calculator) {
        this.calculator = calculator;
    }

    public long computeFactorial(int n) {
        return calculator.calculate(n);
    }
}

// Main class that coordinates everything (because we like classes)
public class factorial_2 {
    public static void main(String[] args) {
        // Factory pattern + DI for maximum OOP suffering
        FactorialCalculator calculator = FactorialCalculatorFactory.createCalculator();
        FactorialService service = new FactorialService(calculator);

        long result = service.computeFactorial(5);
        System.out.println(result);  // Output: 120
    }
}
