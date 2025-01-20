package org.zot.chai.loanrequest.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
@ExtendWith(MockitoExtension.class)
class RestResponseEntityExceptionHandlerTest {

    private final RestResponseEntityExceptionHandler handler = new RestResponseEntityExceptionHandler();

    @Test
    @DisplayName("Handle conflict for IllegalArgumentException")
    void handleConflict_illegalArgumentException_OK() {
        WebRequest request = mock(WebRequest.class);
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<Object> response = handler.handleConflict(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid argument");
    }

//    @Test
//    @DisplayName("Handle conflict for IncorrectCustomerNameException")
//    void handleConflict_incorrectCustomerNameException_OK() {
//        WebRequest request = mock(WebRequest.class);
//        IncorrectCustomerNameException exception = new IncorrectCustomerNameException();
//
//        ResponseEntity<Object> response = handler.handleConflict(exception, request);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(response.getBody()).isEqualTo("Incorrect customer name");
//    }

    @Test
    @DisplayName("Handle all uncaught exceptions")
    void handleAllUncaughtException_OK() {
        WebRequest request = mock(WebRequest.class);
        RuntimeException exception = new RuntimeException("Unexpected error");

        ResponseEntity<Object> response = handler.handleAllUncaughtException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Internal Server ErrorUnexpected error");
    }

    @Test
    @DisplayName("Handle HttpMessageNotReadableException")
    void handleHttpMessageNotReadable_OK() {
        WebRequest request = mock(WebRequest.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Message not readable");

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ProblemDetail.class);
    }
}