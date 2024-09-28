package com.company.ecommerce.customer.controller;

import com.company.ecommerce.customer.api.CustomerApi;
import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;
import com.company.ecommerce.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.company.ecommerce.customer.constant.CommonConstant.X_API_VERSION_V1;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/api/customers", headers = X_API_VERSION_V1)
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Operations CRUD about customer")
@SecurityRequirement(name = "SecurityToken")
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        var customerResponses = this.customerService.findAllCustomers();
        return ResponseEntity.status(OK).body(customerResponses);
    }

    @Override
    public ResponseEntity<CustomerResponse> findCustomerByCustomerId(String customerId) {
        var customerResponse = this.customerService.findCustomerByCustomerId(customerId);
        return ResponseEntity.status(OK).body(customerResponse);
    }

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        var customerResponse = this.customerService.createCustomer(customerRequest);
        return ResponseEntity.status(CREATED).body(customerResponse);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomerByCustomerId(
            String customerId, CustomerRequest customerRequest) {
        var customerResponse = this.customerService.updateCustomerByCustomerId(customerId, customerRequest);
        return ResponseEntity.status(OK).body(customerResponse);
    }

    @Override
    public ResponseEntity<Void> deleteCustomerByCustomerId(String customerId) {
        this.customerService.deleteCustomerByCustomerId(customerId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
