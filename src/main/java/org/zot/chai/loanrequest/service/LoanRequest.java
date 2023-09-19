package org.zot.chai.loanrequest.service;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Loan", indexes = @Index(columnList = "customer_id"))
public class LoanRequest {

    public LoanRequest() {
    }

    public LoanRequest(LoanRequestBuilder builder) {
        this.amount = builder.amount;
        this.customerId = builder.customerId;
        this.customerFullName = builder.customerFullName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 500)
    @DecimalMax(value = "12000.50")
    private Double amount;

    @Positive
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_full_name")
    private String customerFullName;

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public static class LoanRequestBuilder {
        public LoanRequestBuilder(Double amount, Long customerId, String customerFullName) {
            this.amount = amount;
            this.customerId = customerId;
            this.customerFullName = customerFullName;
        }

        private final Double amount;
        private final Long customerId;
        private final String customerFullName;

        public LoanRequest build() {
            return new LoanRequest(this);
        }
    }
}
