package org.zot.chai.loanrequest.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zot.chai.loanrequest.service.LoanRequestService;

@RestController
public class LoanRequestController {
    Logger logger = LoggerFactory.getLogger(LoanRequestController.class);

    private final LoanRequestService service;
    LoanRequestController(LoanRequestService service){
        this.service = service;
    }

    @GetMapping("/")
    public String entryPoint(){
        return "Loan Service Landing page";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/loans")
    public LoanRequestDto createLoanRequest(@RequestBody LoanRequestDto loanRequestDto){
        logger.info("createLoanRequest => {}", loanRequestDto);
        return service.createLoanRequest(loanRequestDto);
    }

    @GetMapping("/v1/loans/{customerId}/total-loan")
    public LoanSummaryDto summeryLoan(@PathVariable Long customerId){
        logger.info("summeryLoan => customerId := {}", customerId);
        return service.sumTotalLoan(customerId);
    }

    @GetMapping("/v1/loans/customers/{customerId}/full-name")
    public String findCustomerFullNameById(@PathVariable Long customerId){
        logger.info("findCustomerFullNameById => customerId := {}", customerId);
        return service.findCustomerFullNameById(customerId);
    }
}
