package com.company.ecommerce.customer.service.impl;

import com.company.ecommerce.customer.dto.AddressRequest;
import com.company.ecommerce.customer.dto.AddressResponse;
import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;
import com.company.ecommerce.customer.entity.Address;
import com.company.ecommerce.customer.entity.Customer;
import com.company.ecommerce.customer.exception.ResourceNotFoundException;
import com.company.ecommerce.customer.mapper.CustomerMapper;
import com.company.ecommerce.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_NOT_FOUND_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private static CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Address address1;
    private Address address2;
    private Customer customer1;
    private Customer customer2;
    private AddressRequest addressRequest;
    private CustomerRequest customerRequest;
    private AddressResponse addressResponse;
    private CustomerResponse customerResponseExpected;
    private List<Customer> customers;

    @BeforeAll
    static void setUpAll() {
        customerMapper = Mappers.getMapper(CustomerMapper.class);
    }

    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setStreet("218 Newbury Street");
        address1.setCity("California");
        address1.setState("Florida");
        address1.setZipCode("16077");
        address1.setCountry("USA");

        address2 = new Address();
        address2.setStreet("500 Oregon Street");
        address2.setCity("Miami");
        address2.setState("Oregon");
        address2.setZipCode("18078");
        address2.setCountry("USA");

        customer1 = new Customer();
        customer1.setCustomerId("66aeee840271a2600f91d799");
        customer1.setFirstName("Peter");
        customer1.setLastName("Larson");
        customer1.setEmail("plarson@gmail.com");
        customer1.setPhoneNumber("+51 962329330");
        customer1.setAddress(address1);

        customer2 = new Customer();
        customer2.setCustomerId("66aeee840271a2600f91d79a");
        customer2.setFirstName("Robert");
        customer2.setLastName("Brown");
        customer2.setEmail("rbrown@gmail.com");
        customer2.setPhoneNumber("+51 964429333");
        customer2.setAddress(address2);

        customers = List.of(customer1, customer2);

        addressRequest = new AddressRequest(
                "218 Newbury Street",
                "California",
                "Florida",
                "16077",
                "USA"
        );

        customerRequest = new CustomerRequest(
                "Peter",
                "Larson",
                "plarson@gmail.com",
                "+51 962329330",
                addressRequest
        );

        addressResponse = new AddressResponse(
                "218 Newbury Street",
                "California",
                "Florida",
                "16077",
                "USA"
        );

        customerResponseExpected = new CustomerResponse(
                "66aeee840271a2600f91d799",
                "Peter",
                "Larson",
                "plarson@gmail.com",
                "+51 962329330",
                addressResponse
        );
    }

    @Test
    @DisplayName("Given two customers in database when find all customers then return the customers")
    void givenTwoCustomersInDatabase_whenFindAllCustomers_thenReturnTheCustomers() {
        when(customerRepository.findAll()).thenReturn(customers);

        var customerResponses = customerService.findAllCustomers();

        assertThat(customerResponses)
                .isNotEmpty()
                .hasSize(2);

        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Given one customer in database when find customer by customer id then return customer")
    void givenOneCustomerInDatabase_whenFindCustomerByCustomerId_thenReturnCustomer() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer1));

        var customerResponseDto = customerService.findCustomerByCustomerId("66aeee840271a2600f91d799");

        assertThat(customerResponseDto)
                .isNotNull()
                .isEqualTo(customerResponseExpected);

        verify(customerRepository).findById(anyString());
    }

    @Test
    @DisplayName("Given one customer is not in database when find customer by customer id then return customer not found")
    void givenOneCustomerIsNotInDatabase_whenFindCustomerByCustomerId_thenReturnCustomerNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq(CUSTOMER_NOT_FOUND_KEY), any(), any(Locale.class)))
                .thenReturn("Customer with customer id 66aeee845291a2632f91d95a not found");

        assertThatThrownBy(() -> customerService.findCustomerByCustomerId("66aeee845291a2632f91d95a"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer with customer id 66aeee845291a2632f91d95a not found");

        verify(customerRepository).findById(anyString());
        verify(messageSource).getMessage(anyString(), any(), any(Locale.class));
    }

    @Test
    @DisplayName("Given one new customer when create customer then return created customer")
    void givenOneNewCustomer_whenCreateCustomer_thenReturnCreatedCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer1);

        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);

        assertThat(customerResponse)
                .isNotNull()
                .isEqualTo(customerResponseExpected);

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Given one customer in database to edit when update customer by customer id then return updated customer")
    void givenOneCustomerInDatabaseToEdit_whenUpdateCustomerByCustomerId_thenReturnUpdatedCustomer() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer1);

        CustomerResponse customerResponse = customerService.updateCustomerByCustomerId(
                customer1.getCustomerId(), customerRequest);

        assertThat(customerResponse)
                .isNotNull()
                .isEqualTo(customerResponseExpected);

        verify(customerRepository).findById(anyString());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Given one customer is not in database when update customer by customer id then return customer not found")
    void givenOneCustomerIsNotInDatabase_whenUpdateCustomerByCustomerId_thenReturnCustomerNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq(CUSTOMER_NOT_FOUND_KEY), any(), any(Locale.class)))
                .thenReturn("Customer with customer id 66aeee845291a2632f91d95a not found");

        assertThatThrownBy(() -> customerService.updateCustomerByCustomerId(
                "66aeee845291a2632f91d95a", customerRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer with customer id 66aeee845291a2632f91d95a not found");

        verify(customerRepository).findById(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
        verify(messageSource).getMessage(anyString(), any(), any(Locale.class));
    }

    @Test
    @DisplayName("Given one customer in database when delete customer by customer id then remove customer")
    void givenOneCustomerInDatabase_whenDeleteCustomerByCustomerId_thenRemoveCustomer() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer2));

        doNothing().when(customerRepository).deleteById(anyString());

        customerService.deleteCustomerByCustomerId(customer2.getCustomerId());

        verify(customerRepository).findById(anyString());
        verify(customerRepository).deleteById(anyString());
    }

    @Test
    @DisplayName("Given one customer is not in database when delete customer by customer id then return customer not found")
    void givenOneCustomerIsNotInDatabase_whenDeleteCustomerByCustomerId_thenReturnCustomerNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq(CUSTOMER_NOT_FOUND_KEY), any(), any(Locale.class)))
                .thenReturn("Customer with customer id 66aeee840271a2600f91d79a not found");

        assertThatThrownBy(() -> customerService.deleteCustomerByCustomerId("66aeee840271a2600f91d79a"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer with customer id 66aeee840271a2600f91d79a not found");

        verify(customerRepository).findById(anyString());
        verify(customerRepository, never()).deleteById(anyString());
        verify(messageSource).getMessage(anyString(), any(), any(Locale.class));
    }

}
