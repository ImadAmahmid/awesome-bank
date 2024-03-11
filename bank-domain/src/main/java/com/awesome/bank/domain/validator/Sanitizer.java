package com.awesome.bank.domain.validator;

public interface Sanitizer<T> {

    T sanitize(T object);
}
