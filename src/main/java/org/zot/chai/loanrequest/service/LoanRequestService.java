package org.zot.chai.loanrequest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zot.chai.loanrequest.exception.CustomerIdNotFoundException;
import org.zot.chai.loanrequest.exception.IncorrectCustomerNameException;
import org.zot.chai.loanrequest.web.LoanRequestDto;
import org.zot.chai.loanrequest.web.LoanSummaryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
/**
 * Service class should be named as LoanServiceImpl and implements LoanService interface
 * Omit Interface and create just concrete LoanService for simplicity
 */
public class LoanRequestService {

    Logger logger = LoggerFactory.getLogger(LoanRequestService.class);
    private final LoanRequestRepository repository;

    LoanRequestService(LoanRequestRepository repository) {
        this.repository = repository;
    }

    public LoanRequestDto createLoanRequest(LoanRequestDto dto) {

        // Find customer from id before checking full name
        repository.findFullNameById(dto.customerId()).ifPresent((name) -> {
            // Different customer full name with the same id is forbidden
            if(!name.equalsIgnoreCase(dto.customerFullName())) {
                logger.error("createLoanRequest => customer id := " + dto.customerId() + " customer name := " + dto.customerFullName());
                throw new IncorrectCustomerNameException();
            }
        });
        // Unwind each amount into separate LoanRequest record and save.
        repository.saveAll(unwind.apply(dto));
        logger.info("createLoanRequest => customer id := " + dto.customerId() +
                " Loan amount := " + dto.amount());
        return dto;
    }

    public LoanSummaryDto sumTotalLoan(Long customerId) {
        var sum = repository.sumLoanAmount(customerId);
        if(sum == 0){
            logger.error("sumTotalLoan => customer id := " + customerId + " not found!");
            throw new CustomerIdNotFoundException();
        }
        logger.info("sumTotalLoan => customer id := " + customerId + " has total loan := " + sum);
        return new LoanSummaryDto(customerId, sum);
    }

    public String findCustomerFullNameById(Long id){
        return repository.findFullNameById(id).orElseThrow(CustomerIdNotFoundException::new);
    }


    /**
     * Convert List of amount in LoanRequest object into separate object with
     * amount
     * customerId
     * customerFullName
     */
    private Function<LoanRequestDto, List<LoanRequest>> unwind = (lrd) -> {
        var loanRequests = new ArrayList<LoanRequest>();

        lrd.amount().forEach((a) -> loanRequests.add(
                new LoanRequest.LoanRequestBuilder(a, lrd.customerId(), lrd.customerFullName())
                        .build()));
        return loanRequests;
    };
}
