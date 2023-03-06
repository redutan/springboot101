package xyz.groundx.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    @GetMapping("/plus")
    public String plus(int a, int b) {
        return "%d + %d = %d".formatted(a, b, a + b);
    }

    @GetMapping("/minus")
    public String minus(int a, int b) {
        return "%d - %d = %d".formatted(a, b, a - b);
    }

    @GetMapping("multiply")
    public String multiply(int a, int b) {
        return "%d * %d = %d".formatted(a, b, a * b);
    }
}
