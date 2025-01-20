package org.zot.chai.loanrequest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.zot.chai.loanrequest.service.LoanRequestService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoanRequestController.class)
class LoanRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private LoanRequestService service;

    @Test
    void whenAccessLandingPage_thenReturn200() throws Exception {
        mvc.perform(get("/").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Loan Service Landing page")));
    }

    @Test
    void whenValidLoanRequest_thenReturn200() throws Exception {

        var request = new LoanRequestDto(1L,"Full-name", List.of(500D));
        mvc.perform(post("/v1/loans").contentType("application/json")
                        .param("loanRequestDto","true")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenValidCustomerId_thenReturnTotalAmount() throws Exception {

        var sum = new LoanSummaryDto(1L,100d);
        given(service.sumTotalLoan(anyLong())).willReturn(sum);
        mvc.perform(get("/v1/loans/1/total-loan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sum)));
    }

    @Test
    void whenValidCustomerId_thenReturnCustomerName() throws Exception {
        given(service.findCustomerFullNameById(anyLong())).willReturn("Full-Name");
        mvc.perform(get("/v1/loans/customers/1/full-name").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Full-Name"));
    }

    //@Test
    // Record doesn't allow to create loan instance that violate validation logic
    void whenInvalidLoanAmount_thenExpectException() throws Exception {
        mvc.perform(post("/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoanRequestDto(1L,"name",List.of(50D))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertEquals("Loan amount must be between '500' to '12000.50", result.getResolvedException().getMessage()));
    }

    @Test
    void whenInvalidCustomerId_thenTotalLoan_throwException() throws Exception {
        mvc.perform(get("/v1/loans/test/total-loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(result -> assertEquals("Method parameter 'customerId': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"test\"",
                        result.getResolvedException().getMessage()));
    }

}
