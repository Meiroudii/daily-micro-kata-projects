import java.util.*;

class ExprNode {
    String value;
    ExprNode left, right;

    ExprNode(String value) {
        this.value = value;
        this.left = this.right = null;
    }
}

public class binary_tree_2 {

    static List<String> infixToPostfix(String expr) {
        List<String> output = new ArrayList<>();
        Stack<Character> stack = new Stack<>();

        StringBuilder number = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            } else {
                if (number.length() > 0) {
                    output.add(number.toString());
                    number.setLength(0);
                }

                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        output.add(String.valueOf(stack.pop()));
                    }
                    stack.pop(); // rm "("
                } else if (isOperator(c)) {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                        output.add(String.valueOf(stack.pop()));
                    }
                    stack.push(c);
                }
            }
        }

        if (number.length() > 0) output.add(number.toString());

        while (!stack.isEmpty()) {
            output.add(String.valueOf(stack.pop()));
        }

        return output;
    }

    static ExprNode buildTree(List<String> postfix) {
        Stack<ExprNode> stack = new Stack<>();

        for (String token : postfix) {
            if (!isOperator(token.charAt(0)) || token.length() > 1) { 
                stack.push(new ExprNode(token));
            } else {
                ExprNode node = new ExprNode(token);
                node.right = stack.pop();
                node.left = stack.pop();
                stack.push(node);
            }
        }
        return stack.peek();
    }

    static double evaluate(ExprNode node) {
        if (node == null) return 0;

        if (node.left == null && node.right == null)
            return Double.parseDouble(node.value);

        double left = evaluate(node.left);
        double right = evaluate(node.right);

        switch (node.value) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/": return left / right;
        }
        return 0;
    }

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    static int precedence(char c) {
        switch (c) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a mathematical expression: ");
        String expr = sc.nextLine().replaceAll("\\s+", "");

        List<String> postfix = infixToPostfix(expr);
        ExprNode root = buildTree(postfix);
        double result = evaluate(root);

        System.out.println("Result: " + result);
        sc.close();
    }
}
