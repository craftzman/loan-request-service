package org.zot.chai.loanrequest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomerIdNotFoundException extends ResponseStatusException {
    public CustomerIdNotFoundException(){
        super(HttpStatus.BAD_REQUEST, "Customer Id not found");
    }
}
