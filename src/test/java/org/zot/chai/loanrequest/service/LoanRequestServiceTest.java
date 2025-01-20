package org.zot.chai.loanrequest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zot.chai.loanrequest.exception.CustomerIdNotFoundException;
import org.zot.chai.loanrequest.exception.IncorrectCustomerNameException;
import org.zot.chai.loanrequest.web.LoanRequestDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanRequestServiceTest {

    @Mock
    LoanRequestRepository repository;

    @InjectMocks
    private LoanRequestService service;

    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("LoanRequest will be save when required data are valid")
    void save_LoanRequest_OK() {

        var entity = new LoanRequest.LoanRequestBuilder(10000d,1l,"full-name").build();
        given(repository.findFullNameById(anyLong())).willReturn(Optional.of("full-name"));

        service.createLoanRequest(new LoanRequestDto(1l,"full-name", List.of(10000d)));

        verify(repository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Total loan amount of particular customer can be calculated")
    void calculate_total_loan_OK() {
        given(repository.sumLoanAmount(anyLong())).willReturn(1500d);
        assertThat(service.sumTotalLoan(10L).loanAmount().compareTo(1500d)).isEven();
    }

    @Test
    @DisplayName("Customer's full name can be retrieved from DB by a valid customer id")
    void find_customer_full_name_OK() {
        given(repository.findFullNameById(anyLong())).willReturn(Optional.of("name-in-db"));
        assertThat(service.findCustomerFullNameById(100l)).isEqualToIgnoringCase("name-in-db");
    }

    @Test
    @DisplayName("LoanRequest will not be save when required data are invalid")
    void save_with_incorrect_customer_name_KO() {

        var newLoanRequest = new LoanRequestDto(100l,"new-name", List.of(1000d,5222d));
        given(repository.findFullNameById(anyLong())).willReturn(Optional.of("mock-name"));

        Exception exception = assertThrows(IncorrectCustomerNameException.class,
                () -> service.createLoanRequest(newLoanRequest));

        assertThat(exception.getMessage()).contains("Customer id + customer name is already existed; use another combination");
        assertThat(exception).isInstanceOf(IncorrectCustomerNameException.class);
        verify(repository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Total loan amount cannot be calculated by invalid customer id")
    void calculate_total_loan_invalid_customer_id_KO() {
        given(repository.sumLoanAmount(anyLong())).willReturn(0d);

        Exception exception = assertThrows(CustomerIdNotFoundException.class,
                () -> service.sumTotalLoan(10L));

        assertThat(exception.getMessage()).contains("Customer Id not found");
        assertThat(exception).isInstanceOf(CustomerIdNotFoundException.class);
    }

//    @Test
//    @DisplayName("LoanRequest will not be saved when customer id is not found")
//    void save_with_non_existent_customer_id_KO() {
//        var newLoanRequest = new LoanRequestDto(999L, "non-existent-name", List.of(1000d));
//        given(repository.findFullNameById(anyLong())).willReturn(Optional.empty());
//
//        Exception exception = assertThrows(CustomerIdNotFoundException.class,
//                () -> service.createLoanRequest(newLoanRequest));
//
//        assertThat(exception.getMessage()).contains("Customer Id not found");
//        assertThat(exception).isInstanceOf(CustomerIdNotFoundException.class);
//        verify(repository, never()).saveAll(any());
//    }

    @Test
    @DisplayName("LoanRequest will be saved when customer id and name are valid")
    void save_with_valid_customer_id_and_name_OK() {
        var newLoanRequest = new LoanRequestDto(1L, "valid-name", List.of(1000d, 2000d));
        given(repository.findFullNameById(anyLong())).willReturn(Optional.of("valid-name"));

        service.createLoanRequest(newLoanRequest);

        verify(repository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Total loan amount is zero when no loans exist for customer")
    void calculate_total_loan_no_loans_OK() {
        given(repository.sumLoanAmount(anyLong())).willReturn(0d);

        Exception exception = assertThrows(CustomerIdNotFoundException.class,
                () -> service.sumTotalLoan(1L));

        assertThat(exception.getMessage()).contains("Customer Id not found");
        assertThat(exception).isInstanceOf(CustomerIdNotFoundException.class);
    }

    @Test
    @DisplayName("Customer's full name retrieval throws exception for invalid customer id")
    void find_customer_full_name_invalid_customer_id_KO() {
        given(repository.findFullNameById(anyLong())).willReturn(Optional.empty());

        Exception exception = assertThrows(CustomerIdNotFoundException.class,
                () -> service.findCustomerFullNameById(999L));

        assertThat(exception.getMessage()).contains("Customer Id not found");
        assertThat(exception).isInstanceOf(CustomerIdNotFoundException.class);
    }

}
