package org.zot.chai.loanrequest.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoanRequestDtoTest {

    @ParameterizedTest
    @DisplayName("Should accept all valid values")
    @ValueSource(doubles = {500, 12000.50})
    void shouldAcceptAllValidValues(Double amount) {
        LoanRequestDto loan = new LoanRequestDto(1L,"test-name", List.of(amount));
        assertThat(loan.amount().equals(amount));
    }

    @ParameterizedTest
    @DisplayName("Should not accept invalid loan amount")
    @ValueSource(doubles = {-500, 0, 499, 12000.51, Double.MAX_VALUE, Double.MIN_VALUE})
    void shouldNotAcceptInvalidAmount(Double amount){
        assertThat(assertThrows(IllegalArgumentException.class,
                () -> new LoanRequestDto(1L,"test-name", List.of(amount)))
                .getMessage().contains("Loan amount must be between '500' to '12000.50'"));
    }

    @ParameterizedTest
    @DisplayName("Should not accept invalid customer id")
    @ValueSource(longs = {-10, 0})
    void shouldNotAcceptNegativeCustomerId(Long id) {
        assertThat(assertThrows(IllegalArgumentException.class,
                () -> new LoanRequestDto(id,"test-name", List.of(600D)))
                .getMessage().contains("Customer id must be positive number"));
    }
}
