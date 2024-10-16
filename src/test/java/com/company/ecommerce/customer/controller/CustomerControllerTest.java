package com.company.ecommerce.customer.controller;

import com.company.ecommerce.customer.dto.AddressRequest;
import com.company.ecommerce.customer.dto.AddressResponse;
import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;
import com.company.ecommerce.customer.exception.ResourceNotFoundException;
import com.company.ecommerce.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private static ObjectMapper objectMapper;
    private static HttpHeaders headers;

    private AddressRequest addressRequest;
    private CustomerRequest customerRequest;
    private CustomerRequest customerBadRequest;
    private AddressResponse addressResponse;
    private CustomerResponse customerResponse1;
    private CustomerResponse customerResponse2;
    private List<CustomerResponse> customerResponses;

    @BeforeAll
    static void setUpAll() {
        objectMapper = new ObjectMapper();

        headers = new HttpHeaders();
        headers.add("X-Api-Version", "v1");
        headers.add("Accept-Language", "en");
    }

    @BeforeEach
    void setUp() {
        addressRequest = new AddressRequest(
                "218 Newbury Street",
                "Chicago",
                "Illinois",
                "16088",
                "USA"
        );

        customerRequest = new CustomerRequest(
                "Nelson",
                "Patrick",
                "npatrick@gmail.com",
                "+51 962329330",
                addressRequest
        );

        customerBadRequest = new CustomerRequest(
                "Nelson",
                "Patrick",
                "npatrick_gmail.com",
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

        customerResponse1 = new CustomerResponse(
                "66aeee840271a2600f91d799",
                "Peter",
                "Larson",
                "plarson@gmail.com",
                "+51 954587651",
                addressResponse
        );

        customerResponse2 = new CustomerResponse(
                "66aeee840271a2600f91d79a",
                "Robert",
                "Brown",
                "rbrown@gmail.com",
                "+51 963587193",
                addressResponse
        );

        customerResponses = List.of(customerResponse1, customerResponse2);
    }

    @Test
    @DisplayName("Given two customers in database when find all customers then return JSON customers")
    void givenTwoCustomersInDatabase_whenFindAllCustomers_thenReturnJSONCustomers() throws Exception {
        when(customerService.findAllCustomers()).thenReturn(customerResponses);

        mockMvc.perform(get("/api/customers")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .headers(headers))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$[0].firstName").value("Peter"))
                .andExpect(jsonPath("$[0].address.city").value("California"))
                .andExpect(jsonPath("$[1].id").value("66aeee840271a2600f91d79a"))
                .andExpect(jsonPath("$[1].firstName").value("Robert"))
                .andExpect(jsonPath("$[1].address.city").value("California"));

        verify(customerService).findAllCustomers();
    }

    @Test
    @DisplayName("Given an user no authenticated when find all customers then return status UNAUTHORIZED")
    void givenAnUserNoAuthenticated_whenFindAllCustomers_thenReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .with(SecurityMockMvcRequestPostProcessors.anonymous())
                        .headers(headers))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given an user is not granted with role customer when find all customers then return status FORBIDDEN")
    void givenAnUserIsNotGrantedWithRoleCustomer_whenFindAllCustomers_thenReturnStatusForbidden() throws Exception {
        when(customerService.findAllCustomers()).thenThrow(AccessDeniedException.class);

        mockMvc.perform(get("/api/customers")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER")))
                        .headers(headers))
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given an unexpected error when find all customers then return status INTERNAL SERVER ERROR")
    void givenAnUnexpectedError_whenFindAllCustomers_thenReturnStatusInternalServerError() throws Exception {
        when(customerService.findAllCustomers()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/customers")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .headers(headers))
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(status().isInternalServerError());

        verify(customerService).findAllCustomers();
    }

    @Test
    @DisplayName("Given one customer in database when find customer by id then return JSON customer")
    void givenOneCustomerInDatabase_whenFindCustomerById_thenReturnJSONCustomer() throws Exception {
        when(customerService.findCustomerById(anyString())).thenReturn(customerResponse1);

        mockMvc.perform(get("/api/customers/{id}", "66aeee840271a2600f91d799")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .headers(headers))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$.firstName").value("Peter"))
                .andExpect(jsonPath("$.address.city").value("California"));

        verify(customerService).findCustomerById(anyString());
    }

    @Test
    @DisplayName("Given one customer is not in database when find customer by id then return status NOT FOUND")
    void givenOneCustomerIsNotInDatabase_whenFindCustomerById_thenReturnStatusNotFound() throws Exception {
        when(customerService.findCustomerById(anyString())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/customers/{id}", "66aeee845291a2632f91d95a")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .headers(headers))
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound());

        verify(customerService).findCustomerById(anyString());
    }

    @Test
    @DisplayName("Given one new customer when create customer then return JSON created customer")
    void givenOneNewCustomer_whenCreateCustomer_thenReturnJSONCreatedCustomer() throws Exception {
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(customerResponse1);

        mockMvc.perform(post("/api/customers")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .contentType(APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$.firstName").value("Peter"))
                .andExpect(jsonPath("$.address.city").value("California"));

        verify(customerService).createCustomer(any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer with incorrect data when create customer then return status BAD REQUEST")
    void givenOneCustomerWithIncorrectData_whenCreateCustomer_thenReturnStatusBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .contentType(APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerBadRequest)))
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer with email existing when create customer then return status CONFLICT")
    void givenOneCustomerWithEmailExisting_whenCreateCustomer_thenReturnStatusConflict() throws Exception {
        when(customerService.createCustomer(any(CustomerRequest.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/api/customers")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .contentType(APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(status().isConflict());

        verify(customerService).createCustomer(any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer in database when update customer by id then return JSON updated customer")
    void givenOneCustomerInDatabase_whenUpdateCustomerById_thenReturnJSONUpdatedCustomer() throws Exception {
        when(customerService.updateCustomerById(anyString(), any(CustomerRequest.class))).thenReturn(customerResponse1);

        mockMvc.perform(put("/api/customers/{id}", "66aeee840271a2600f91d799")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .contentType(APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$.firstName").value("Peter"))
                .andExpect(jsonPath("$.address.city").value("California"));

        verify(customerService).updateCustomerById(anyString(), any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer is not in database when update customer by id then return status NOT FOUND")
    void givenOneCustomerIsNotInDatabase_whenUpdateCustomerById_thenReturnStatusNotFound() throws Exception {
        when(customerService.updateCustomerById(anyString(), any(CustomerRequest.class)))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/api/customers/{id}", "66aeee845291a2632f91d95a")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .contentType(APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound());

        verify(customerService).updateCustomerById(anyString(), any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer in database when delete customer by id then return status NO CONTENT")
    void givenOneCustomerInDatabase_whenDeleteCustomerById_thenReturnStatusOKAndJSONDeletedMessage() throws Exception {
        doNothing().when(customerService).deleteCustomerById(anyString());

        mockMvc.perform(delete("/api/customers/{id}", "66aeee840271a2600f91d799")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .headers(headers))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(anyString());
    }

    @Test
    @DisplayName("Given one customer is not in database when delete customer by id then return status NOT FOUND")
    void givenOneCustomerIsNotInDatabase_whenDeleteCustomerById_thenReturnStatusNotFound() throws Exception {
        doThrow(ResourceNotFoundException.class).when(customerService).deleteCustomerById(anyString());

        mockMvc.perform(delete("/api/customers/{id}", "66aeee845291a2632f91d95a")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("CUSTOMER MANAGER")))
                        .headers(headers))
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound());

        verify(customerService).deleteCustomerById(anyString());
    }

}
