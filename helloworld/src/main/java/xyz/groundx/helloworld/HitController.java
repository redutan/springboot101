package xyz.groundx.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HitController {
    private final AtomicLong count = new AtomicLong(0);

    @GetMapping("/hit")
    public synchronized long hit() {
        return count.incrementAndGet();
    }
}
