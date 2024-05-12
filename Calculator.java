import java.util.ArrayDeque;
import java.util.Deque;

public class Calculator {

    // Calculating the result of a medial expression
    public int calcExpresion(String expression) {
        expression = expression.replaceAll("\\s", ""); // Remove Spaces

        Deque<Integer> operandStack = new ArrayDeque<>(); // Stack to store operands
        Deque<Character> operatorStack = new ArrayDeque<>(); // Stack to store operators

        StringBuilder numBuilder = new StringBuilder(); // Used to store multi-digit numbers

        // Loop through each character in the expression
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                numBuilder.append(c); // Adding Characters to Multi-Digit Strings
            } else if (isOperator(c)) { // Check if character is an operator
                if (numBuilder.length() > 0) {
                    operandStack.push(Integer.parseInt(numBuilder.toString())); // Converts a multi-digit string to an integer and pushes it onto the operand stack.
                    numBuilder.setLength(0); // Clear multi-digit strings
                }
                // Perform calculations based on operator precedence
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(c)) {
                    calculate(operandStack, operatorStack);
                }
                operatorStack.push(c); // Push current operator onto the operator stack
            }
        }

        if (numBuilder.length() > 0) {
            operandStack.push(Integer.parseInt(numBuilder.toString())); // Convert the remaining multi-digit string to an integer and push it into the operand stack
        }

        // Perform remaining calculations in the stacks
        while (!operatorStack.isEmpty()) {
            calculate(operandStack, operatorStack);
        }

        return operandStack.pop(); // Return the final result
    }

    // Check if a character is an operator
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Determine the precedence of an operator
    private int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1; // Addition and subtraction have lower precedence
        } else {
            return 2; // Multiplication and division have higher precedence
        }
    }

    // Perform calculation based on operator
    private void calculate(Deque<Integer> operandStack, Deque<Character> operatorStack) {
        char operator = operatorStack.pop(); // Pop operator from the operator stack
        int operand2 = operandStack.pop(); // Pop second operand from the operand stack
        int operand1 = operandStack.pop(); // Pop first operand from the operand stack
        int result;
        // Perform arithmetic operation based on the operator
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 != 0) {
                    result = operand1 / operand2;
                } else {
                    throw new ArithmeticException("The divisor cannot be 0"); // Division by zero error
                }
                break;
            default:
                throw new RuntimeException("invalid operator"); // Throw exception for invalid operator
        }
        operandStack.push(result); // Push the result back to the operand stack
    }
}
