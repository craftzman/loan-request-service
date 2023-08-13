package org.zot.chai.loanrequest.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoanRequestRepository extends CrudRepository<LoanRequest, Long> {

    @Query("SELECT COALESCE(sum(l.amount),0) from LoanRequest l where l.customerId = :id")
    Double sumLoanAmount(@Param("id") Long customerId);

    @Query("SELECT l.customerFullName from LoanRequest l where l.customerId = :id")
    Optional<String> findFullNameById(@Param("id") Long customerId);
}
