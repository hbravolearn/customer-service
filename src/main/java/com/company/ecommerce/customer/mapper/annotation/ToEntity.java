package com.company.ecommerce.customer.mapper.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Mapping(target = "customerId", ignore = true)
@Mapping(target = "version", ignore = true)
@Mapping(target = "createdBy", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "modifiedBy", ignore = true)
@Mapping(target = "modifiedAt", ignore = true)
public @interface ToEntity {
}
