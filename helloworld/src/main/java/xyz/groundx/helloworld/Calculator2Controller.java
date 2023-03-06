package xyz.groundx.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator2")
public class Calculator2Controller {
    private final Calculator calculator;

    public Calculator2Controller(Calculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping("/{operator}")
    public String operation(@PathVariable String operator, int a, int b) {
        String symbol = calculator.getSymbol(operator);
        int result = calculator.calculate(operator, a, b);
        return "%d %s %d = %d".formatted(a, symbol, b, result);
    }
}
