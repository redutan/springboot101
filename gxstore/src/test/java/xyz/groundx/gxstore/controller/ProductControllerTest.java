package xyz.groundx.gxstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.groundx.gxstore.model.Product;
import xyz.groundx.gxstore.service.ProductService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    ProductService service;

    @Test
    public void products() throws Exception {
        // given
        List<Product> products = List.of(new Product(1L), new Product(2L));
        given(service.getAllProducts()).willReturn(products);

        // when
        mvc.perform(get("/products")
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(2))
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[1].id").value(2L));

        // then
        then(service).should().getAllProducts();
    }

    @Test
    public void promotions() throws Exception {
        // given
        List<Product> products = List.of(new Product(1L), new Product(2L));
        given(service.getPromotions()).willReturn(products);

        // when
        mvc.perform(get("/promotions")
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(2))
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[1].id").value(2L));

        // then
        then(service).should().getPromotions();
    }
}