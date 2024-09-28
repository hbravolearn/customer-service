package com.company.ecommerce.customer.mapper;

import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;
import com.company.ecommerce.customer.entity.Customer;
import com.company.ecommerce.customer.mapper.annotation.ToEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @ToEntity
    Customer mapToCustomer(CustomerRequest customerRequest);

    CustomerResponse mapToCustomerResponse(Customer customer);

    @ToEntity
    void mergeCustomer(CustomerRequest customerRequest, @MappingTarget Customer customer);

}
