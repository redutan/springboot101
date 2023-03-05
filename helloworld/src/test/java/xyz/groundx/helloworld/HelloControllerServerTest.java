package xyz.groundx.helloworld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HelloControllerServerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void hello() {
        ResponseEntity<String> re = restTemplate.getForEntity("/hello", String.class);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo("Hello, World!");
    }

    @Test
    void hello_who() {
        String who = "Someone1";
        ResponseEntity<String> re = restTemplate.getForEntity("/hello?who={0}", String.class, who);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo("Hello, %s!".formatted(who));
    }

    @Test
    void helloToSomeone() {
        String who = "Someone2";
        ResponseEntity<String> re = restTemplate.getForEntity("/hello/{0}", String.class, who);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo("Hello, %s!".formatted(who));
    }
}