package xyz.groundx.helloworld;

import org.springframework.stereotype.Service;

@Service
public class Calculator {
    public int calculate(String operator, int a, int b) {
        if ("plus".equals(operator)) {
            return a + b;
        } else if ("minus".equals(operator)) {
            return a - b;
        } else if ("multiply".equals(operator)) {
            return a * b;
        } else {
            throw new ArithmeticException("Unsupported operator: " + operator);
        }
    }

    public String getSymbol(String operator) {
        if ("plus".equals(operator)) {
            return "+";
        } else if ("minus".equals(operator)) {
            return "-";
        } else if ("multiply".equals(operator)) {
            return "*";
        } else {
            throw new ArithmeticException("Unsupported operator: " + operator);
        }
    }
}
