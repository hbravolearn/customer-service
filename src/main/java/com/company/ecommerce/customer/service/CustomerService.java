package com.company.ecommerce.customer.service;

import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> findAllCustomers();
    CustomerResponse findCustomerById(String id);
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    CustomerResponse updateCustomerById(String id, CustomerRequest customerRequest);
    void deleteCustomerById(String id);

}
