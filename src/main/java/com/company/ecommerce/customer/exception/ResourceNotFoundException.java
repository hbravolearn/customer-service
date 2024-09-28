package com.company.ecommerce.customer.exception;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(MessageSource messageSource, String key, String id, Locale locale) {
        super(messageSource.getMessage(key, new Object[] { id }, locale));
    }

}
