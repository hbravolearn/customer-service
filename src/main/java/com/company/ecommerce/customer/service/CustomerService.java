package com.company.ecommerce.customer.service;

import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> findAllCustomers();
    CustomerResponse findCustomerByCustomerId(String customerId);
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    CustomerResponse updateCustomerByCustomerId(String customerId, CustomerRequest customerRequest);
    void deleteCustomerByCustomerId(String customerId);

}
