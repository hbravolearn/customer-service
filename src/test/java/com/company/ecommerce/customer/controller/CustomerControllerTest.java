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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
                        .headers(headers))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$[0].firstName").value("Peter"))
                .andExpect(jsonPath("$[0].address.city").value("California"))
                .andExpect(jsonPath("$[1].customerId").value("66aeee840271a2600f91d79a"))
                .andExpect(jsonPath("$[1].firstName").value("Robert"))
                .andExpect(jsonPath("$[1].address.city").value("California"));

        verify(customerService).findAllCustomers();
    }

    @Test
    @DisplayName("Given an unexpected error when find all customers then return status INTERNAL SERVER ERROR")
    void givenAnUnexpectedError_whenFindAllCustomers_thenReturnStatusInternalServerError() throws Exception {
        when(customerService.findAllCustomers()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/customers")
                        .headers(headers))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isInternalServerError());

        verify(customerService).findAllCustomers();
    }

    @Test
    @DisplayName("Given one customer in database when find customer by customer id then return JSON customer")
    void givenOneCustomerInDatabase_whenFindCustomerByCustomerId_thenReturnJSONCustomer() throws Exception {
        when(customerService.findCustomerByCustomerId(anyString())).thenReturn(customerResponse1);

        mockMvc.perform(get("/api/customers/{customerId}", "66aeee840271a2600f91d799")
                        .headers(headers))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$.firstName").value("Peter"))
                .andExpect(jsonPath("$.address.city").value("California"));

        verify(customerService).findCustomerByCustomerId(anyString());
    }

    @Test
    @DisplayName("Given one customer is not in database when find customer by customer id then return status NOT FOUND")
    void givenOneCustomerIsNotInDatabase_whenFindCustomerByCustomerId_thenReturnStatusNotFound() throws Exception {
        when(customerService.findCustomerByCustomerId(anyString())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/customers/{customerId}", "66aeee845291a2632f91d95a")
                        .headers(headers))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound());

        verify(customerService).findCustomerByCustomerId(anyString());
    }

    @Test
    @DisplayName("Given one new customer when create customer then return JSON created customer")
    void givenOneNewCustomer_whenCreateCustomer_thenReturnJSONCreatedCustomer() throws Exception {
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(customerResponse1);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$.firstName").value("Peter"))
                .andExpect(jsonPath("$.address.city").value("California"));

        verify(customerService).createCustomer(any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer with incorrect data when create customer then return status BAD REQUEST")
    void givenOneCustomerWithIncorrectData_whenCreateCustomer_thenReturnStatusBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerBadRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer in database when update customer by customer id then return JSON updated customer")
    void givenOneCustomerInDatabase_whenUpdateCustomerByCustomerId_thenReturnJSONUpdatedCustomer() throws Exception {
        when(customerService.updateCustomerByCustomerId(anyString(), any(CustomerRequest.class))).thenReturn(customerResponse1);

        mockMvc.perform(put("/api/customers/{customerId}", "66aeee840271a2600f91d799")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("66aeee840271a2600f91d799"))
                .andExpect(jsonPath("$.firstName").value("Peter"))
                .andExpect(jsonPath("$.address.city").value("California"));

        verify(customerService).updateCustomerByCustomerId(anyString(), any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer is not in database when update customer by customer id then return status NOT FOUND")
    void givenOneCustomerIsNotInDatabase_whenUpdateCustomerByCustomerId_thenReturnStatusNotFound() throws Exception {
        when(customerService.updateCustomerByCustomerId(anyString(), any(CustomerRequest.class)))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/api/customers/{customerId}", "66aeee845291a2632f91d95a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound());

        verify(customerService).updateCustomerByCustomerId(anyString(), any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Given one customer in database when delete customer by customer id then return status NO CONTENT")
    void givenOneCustomerInDatabase_whenDeleteCustomerById_thenReturnStatusOKAndJSONDeletedMessage() throws Exception {
        doNothing().when(customerService).deleteCustomerByCustomerId(anyString());

        mockMvc.perform(delete("/api/customers/{customerId}", "66aeee840271a2600f91d799")
                        .headers(headers))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerByCustomerId(anyString());
    }

    @Test
    @DisplayName("Given one customer is not in database when delete customer by customer id then return status NOT FOUND")
    void givenOneCustomerIsNotInDatabase_whenDeleteCustomerByCustomerId_thenReturnStatusNotFound() throws Exception {
        doThrow(ResourceNotFoundException.class).when(customerService).deleteCustomerByCustomerId(anyString());

        mockMvc.perform(delete("/api/customers/{customerId}", "66aeee845291a2632f91d95a")
                        .headers(headers))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound());

        verify(customerService).deleteCustomerByCustomerId(anyString());
    }

}
