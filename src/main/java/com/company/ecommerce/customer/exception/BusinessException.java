package com.company.ecommerce.customer.exception;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(MessageSource messageSource, String key, String id, Locale locale) {
        super(messageSource.getMessage(key, new Object[] { id }, locale));
    }

}
