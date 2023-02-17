package xyz.groundx.helloworld;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {
    HelloController controller;

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @DisplayName("Hello, World!")
    @Test
    void hello() {
        var result = controller.hello(null);

        assertThat(result).isEqualTo("Hello, World!");
    }

    @DisplayName("Hello, Jordan!")
    @Test
    void helloWho() {
        var result = controller.hello("Jordan");

        assertThat(result).isEqualTo("Hello, Jordan!");
    }

    @DisplayName("Hello To Albert!")
    @Test
    void helloToSomeone() {
        var result = controller.helloToSomeone("Albert");

        assertThat(result).isEqualTo("Hello, Albert!");
    }
}