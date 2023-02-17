package xyz.groundx.helloworld;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "who", required = false) String who) {
        if (who == null) {
            return "Hello, World!";
        }
        return "Hello, %s!".formatted(who);
    }

    @GetMapping("/hello/{someone}")
    public String helloToSomeone(@PathVariable String someone) {
        return "Hello, %s!".formatted(someone);
    }
}