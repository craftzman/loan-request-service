package org.zot.chai.loanrequest.web;

import java.util.List;

public record LoanRequestDto(Long customerId, String customerFullName, List<Double> amount ) {

    public LoanRequestDto {
        amount.forEach((a)-> {
            if( a < 0 || a < 500 || a > 12000.50 ){
                throw new IllegalArgumentException(
                        "Loan amount must be between '500' to '12000.50'"
                );
            }
        });

        if(customerId < 1){
            throw new IllegalArgumentException(
                    "Customer id must be positive number"
            );
        }
    }
}
