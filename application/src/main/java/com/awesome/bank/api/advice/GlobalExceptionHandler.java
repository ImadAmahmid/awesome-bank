package com.awesome.bank.api.advice;

import com.awesome.bank.domain.exception.AccountNotFoundException;
import com.awesome.bank.domain.exception.InsufficientBalanceException;
import com.awesome.bank.domain.exception.MaximumBalanceExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    ProblemDetail handleAccountNotFoundException() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Could not find account"
        );
    }

    @ExceptionHandler(MaximumBalanceExceededException.class)
    ProblemDetail handleMaximumBalanceExceededException() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Maximum balance reached!"
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    ProblemDetail handleInsufficientBalanceException() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Your account does not have enough resources for this operation!"
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.METHOD_NOT_ALLOWED,
                ex.getMessage()
        );
    }

}