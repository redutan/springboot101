package xyz.groundx.gxstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {
    @Test
    public void products(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/products")
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(6))
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].img").value("img/strings.png"))
           .andExpect(jsonPath("$[0].small_img").value("img/img-small/strings.png"))
           .andExpect(jsonPath("$[0].imgalt").value("string"))
           .andExpect(jsonPath("$[0].price").value(100.00f))
           .andExpect(jsonPath("$[0].promotion").value(nullValue()))
           .andExpect(jsonPath("$[0].desc").value(""))
           .andExpect(jsonPath("$[0].productname").value("Strings"));
    }
}
