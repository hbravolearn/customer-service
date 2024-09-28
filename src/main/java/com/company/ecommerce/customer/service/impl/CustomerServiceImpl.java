package com.company.ecommerce.customer.service.impl;

import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;
import com.company.ecommerce.customer.exception.ResourceNotFoundException;
import com.company.ecommerce.customer.mapper.CustomerMapper;
import com.company.ecommerce.customer.repository.CustomerRepository;
import com.company.ecommerce.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_NOT_FOUND_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final MessageSource messageSource;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAllCustomers() {
        var customers = customerRepository.findAll();
        log.info("Customers find successfully");
        return customers.stream()
                .map(customerMapper::mapToCustomerResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findCustomerById(String id) {
        var locale = LocaleContextHolder.getLocale();
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource, CUSTOMER_NOT_FOUND_KEY, id, locale));
        log.info("Customer with id {} find successfully", customer.getId());
        return customerMapper.mapToCustomerResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        var customer = customerMapper.mapToCustomer(customerRequest);
        var savedCustomer = customerRepository.save(customer);
        log.info("Customer with id {} created successfully", savedCustomer.getId());
        return customerMapper.mapToCustomerResponse(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerById(String id, CustomerRequest customerRequest) {
        var locale = LocaleContextHolder.getLocale();
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource, CUSTOMER_NOT_FOUND_KEY, id, locale));

        customerMapper.mergeCustomer(customerRequest, customer);
        var updatedCustomer = customerRepository.save(customer);
        log.info("Customer with id {} update successfully", updatedCustomer.getId());
        return customerMapper.mapToCustomerResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomerById(String id) {
        var locale = LocaleContextHolder.getLocale();
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource, CUSTOMER_NOT_FOUND_KEY, id, locale));

        customerRepository.deleteById(customer.getId());
        log.info("Customer with id {} deleted successfully", customer.getId());
    }

}
