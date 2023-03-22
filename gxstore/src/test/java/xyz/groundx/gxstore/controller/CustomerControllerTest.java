package xyz.groundx.gxstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.groundx.gxstore.exception.CustomerNotFoundException;
import xyz.groundx.gxstore.model.Customer;
import xyz.groundx.gxstore.model.CustomerDto;
import xyz.groundx.gxstore.service.CustomerService;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/customers/1"))
        ;
        then(service).should().register(dto);
    }

    @Test
    void register_invalidParametersTo400() throws Exception {
        String requestBody = """
                {
                    "firstName": "jordan",
                    "lastName": "jung",
                    "email": "account @address.com",
                    "password": "P@ssW0r"
                }
                """;

        mvc.perform(post("/customers")
                   .locale(Locale.KOREAN)   // Accept-Language: ko, ko_KR
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(requestBody))
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.type").value("about:blank"))
           .andExpect(jsonPath("$.title").value("Bad Request"))
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.instance").value("/customers"))
           .andExpect(jsonPath("$.fieldErrors.length()").value(2))
           .andExpect(jsonPath("$.fieldErrors[?(@.field=='email')].defaultMessage").value("올바른 형식의 이메일 주소여야 합니다"))
           .andExpect(jsonPath("$.fieldErrors[?(@.field=='password')].defaultMessage").value("\"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$\"와 일치해야 합니다"))
        ;
        then(service).should(never()).register(any());
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
        CustomerDto.View detail = new CustomerDto.View(23423L, "jordan", "jung", "jordan.jung@groundx.xyz");
        given(service.getCustomer(detail.customerId())).willReturn(detail);

        mvc.perform(get("/customers/{0}", 23423L)
                   .accept(MediaType.ALL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.customerId").value(detail.customerId()))
           .andExpect(jsonPath("$.firstName").value(detail.firstName()))
           .andExpect(jsonPath("$.lastName").value(detail.lastName()))
           .andExpect(jsonPath("$.email").value(detail.email()))
        ;
        then(service).should().getCustomer(detail.customerId());
    }

    @Disabled
    @Test
    void detail_nullToNotFound() throws Exception {
        Long notExistsId = -1L;
        given(service.getCustomer(notExistsId)).willReturn(null);

        mvc.perform(get("/customers/{0}", notExistsId)
                   .accept(MediaType.ALL))
           .andExpect(status().isNotFound())
        ;
        then(service).should().getCustomer(notExistsId);
    }

    @Test
    void detail_throwExceptionToNotFound() throws Exception {
        Long notExistsId = -1L;
        given(service.getCustomer(notExistsId)).willThrow(new CustomerNotFoundException(notExistsId));

        mvc.perform(get("/customers/{0}", notExistsId)
                   .accept(MediaType.ALL))
           .andDo(print())
           .andExpect(status().isNotFound())
           .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE))
           .andExpect(jsonPath("$.type").value("about:blank"))
           .andExpect(jsonPath("$.title").value("Not Found"))
           .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
           .andExpect(jsonPath("$.detail").value("Not found customer: -1"))
           .andExpect(jsonPath("$.instance").value("/customers/-1"))
        ;
        then(service).should().getCustomer(notExistsId);
    }

    @Test
    void modify() throws Exception {
        Long customerId = 34202032L;
        String requestBody = """
                {
                    "firstName": "ian",
                    "lastName": "han"
                }
                """;
        CustomerDto.Modify command = om.readValue(requestBody, CustomerDto.Modify.class);
        CustomerDto.View detail = new CustomerDto.View(customerId, "ian", "han", "ian.han@groundx.xyz");
        given(service.modifyCustomer(customerId, command)).willReturn(detail);

        mvc.perform(put("/customers/{0}", customerId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(requestBody))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.customerId").value(detail.customerId()))
           .andExpect(jsonPath("$.firstName").value(detail.firstName()))
           .andExpect(jsonPath("$.lastName").value(detail.lastName()))
           .andExpect(jsonPath("$.email").value(detail.email()))
        ;
        then(service).should().modifyCustomer(customerId, command);
    }

    @Test
    void modify_throwNotFoundExceptionToNotFound() throws Exception {
        Long notExistsId = -1L;
        String requestBody = """
                {
                    "firstName": "notFound",
                    "lastName": "unknown"
                }
                """;
        CustomerDto.Modify command = om.readValue(requestBody, CustomerDto.Modify.class);
        given(service.modifyCustomer(notExistsId, command)).willThrow(new CustomerNotFoundException(notExistsId));

        mvc.perform(put("/customers/{0}", notExistsId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(requestBody))
           .andExpect(status().isNotFound())
           .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE))
           .andExpect(jsonPath("$.type").value("about:blank"))
           .andExpect(jsonPath("$.title").value("Not Found"))
           .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
           .andExpect(jsonPath("$.detail").value("Not found customer: -1"))
           .andExpect(jsonPath("$.instance").value("/customers/-1"))
        ;
        then(service).should().modifyCustomer(notExistsId, command);
    }

    @Test
    void modify_invalidParametersTo400() throws Exception {
        Long customerId = 583775004L;
        String requestBody = """
                {
                    "firstName": null,
                    "lastName": ""
                }
                """;

        mvc.perform(put("/customers/{0}", customerId)
                   .locale(Locale.JAPAN)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(requestBody))
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.type").value("about:blank"))
           .andExpect(jsonPath("$.title").value("Bad Request"))
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.instance").value("/customers/%s".formatted(customerId)))
           .andExpect(jsonPath("$.fieldErrors.length()").value(1))
           .andExpect(jsonPath("$.fieldErrors[0].defaultMessage").value("名前を入力してください。"))
        ;
        then(service).should(never()).modifyCustomer(any(), any());
    }

    @Test
    void deleteApi() throws Exception {
        Long customerId = 4943905405L;

        mvc.perform(delete("/customers/{0}", customerId))
           .andExpect(status().isNoContent())
        ;
        then(service).should().deleteCustomer(customerId);
    }
}