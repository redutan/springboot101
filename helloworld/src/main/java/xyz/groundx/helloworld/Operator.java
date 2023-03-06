package xyz.groundx.helloworld;

import java.util.function.IntBinaryOperator;

@SuppressWarnings("unused")
public enum Operator {
    PLUS("+", Integer::sum),
    MINUS("-", (a, b) -> a - b),
    MULTIPLY("*", (a, b) -> a * b),
    DIVIDE("/", (a, b) -> a / b);

    private final String symbol;
    private final IntBinaryOperator op;

    Operator(String symbol, IntBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    public String getSymbol() {
        return symbol;
    }

    public int apply(int a, int b) {
        return op.applyAsInt(a, b);
    }
}
