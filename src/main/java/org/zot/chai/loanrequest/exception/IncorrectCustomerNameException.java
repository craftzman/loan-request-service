package org.zot.chai.loanrequest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IncorrectCustomerNameException extends ResponseStatusException {
    public IncorrectCustomerNameException(){
        super(HttpStatus.BAD_REQUEST, "Customer id + customer name is already existed; use another combination");
    }
}
