package xyz.groundx.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator3")
public class Calculator3Controller {
    @GetMapping("/{operator}")
    public String operation(@PathVariable Operator operator, int a, int b) {
        String symbol = operator.getSymbol();
        int result = operator.apply(a, b);
        return "%d %s %d = %d".formatted(a, symbol, b, result);
    }
}
