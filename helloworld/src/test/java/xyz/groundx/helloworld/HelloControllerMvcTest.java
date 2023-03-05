package xyz.groundx.helloworld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HelloController.class)
class HelloControllerMvcTest {
    @Autowired
    MockMvc mvc;

    @Test
    void hello() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }

    @Test
    void hello_who() throws Exception {
        mvc.perform(get("/hello")
                        .param("who", "Someone1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Someone1!"));
    }

    @Test
    void helloToSomeone() throws Exception {
        mvc.perform(get("/hello/Someone2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Someone2!"));
    }
}