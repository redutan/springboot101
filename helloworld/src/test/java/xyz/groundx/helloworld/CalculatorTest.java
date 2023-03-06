package xyz.groundx.helloworld;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculatorTest {
    Calculator cal;

    @BeforeEach
    void setUp() {
        cal = new Calculator();
    }

    @Test
    void plus() {
        int result = cal.calculate("plus", 1, 2);
        assertThat(result).isEqualTo(3);
    }

    @Test
    void minus() {
        int result = cal.calculate("minus", 3, 2);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void multiply() {
        int result = cal.calculate("multiply", 3, 4);
        assertThat(result).isEqualTo(12);
    }

    @Test
    void unsupportedOperator() {
        assertThatThrownBy(() -> cal.calculate("del", 0, 0))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Unsupported operator: del");
    }
}