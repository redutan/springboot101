package xyz.groundx.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.groundx.customer.model.Customer;
import xyz.groundx.customer.model.CustomerDto;
import xyz.groundx.customer.service.CustomerService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @MockBean
    CustomerService service;

    @Test
    void register() throws Exception {
        String requestBody = """
                {
                    "firstName": "jordan",
                    "lastName": "jung",
                    "email": "jordan.jung@groundx.xyz",
                    "password": "P@ssW0rd"
                }
                """;
        CustomerDto.Register dto = om.readValue(requestBody, CustomerDto.Register.class);
        given(service.register(dto)).willReturn(new Customer(1L));

        mvc.perform(post("/customers")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(requestBody))
           .andExpect(status().isCreated())
           .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/customers/1"));

        then(service).should().register(dto);
    }

    @Test
    void all() throws Exception {
        List<CustomerDto.View> list = List.of(
                new CustomerDto.View(1L, "jordan", "jung", "1@g.com"),
                new CustomerDto.View(2L, "genesis", "kim", "2@g.com"),
                new CustomerDto.View(3L, "ground", "lee", "3@g.com"));
        given(service.getAllCustomers()).willReturn(list);

        CustomerDto.View first = list.get(0);

        mvc.perform(get("/customers")
                   .accept(MediaType.ALL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(3L))
           .andExpect(jsonPath("$[0].customerId").value(first.customerId()))
           .andExpect(jsonPath("$[0].firstName").value(first.firstName()))
           .andExpect(jsonPath("$[0].lastName").value(first.lastName()))
           .andExpect(jsonPath("$[0].email").value(first.email()))
        ;
        then(service).should().getAllCustomers();
    }

    @Test
    void detail() throws Exception {
        mvc.perform(get("/customer/1")
                   .accept(MediaType.ALL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].customerId").value(first.customerId()))
           .andExpect(jsonPath("$[0].firstName").value(first.firstName()))
           .andExpect(jsonPath("$[0].lastName").value(first.lastName()))
           .andExpect(jsonPath("$[0].email").value(first.email()))
        ;
    }
}