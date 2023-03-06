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
public class Calculator3ControllerServerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void plus() {
        ResponseEntity<String> re = restTemplate.getForEntity("/calculator3/plus?a=1&b=2", String.class);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo("1 + 2 = 3");
    }

    @Test
    void minus() {
        ResponseEntity<String> re = restTemplate.getForEntity("/calculator3/minus?a=2&b=1", String.class);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo("2 - 1 = 1");
    }

    @Test
    void multiply() {
        ResponseEntity<String> re = restTemplate.getForEntity("/calculator3/multiply?a=3&b=2", String.class);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo("3 * 2 = 6");
    }
}
