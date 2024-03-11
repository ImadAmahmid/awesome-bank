package com.awesome.bank.domain.validator;

public interface Validator<T> {

    void validate(T object);
}
