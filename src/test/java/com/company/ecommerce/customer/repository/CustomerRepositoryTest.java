package com.company.ecommerce.customer.repository;

import com.company.ecommerce.customer.entity.Address;
import com.company.ecommerce.customer.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class CustomerRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    private Address address1;
    private Address address2;
    private Address address3;
    private Customer customer1;
    private Customer customer2;
    private Customer customer3;

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

        address3 = new Address();
        address3.setStreet("857 Massachusetts Street");
        address3.setCity("New York");
        address3.setState("Hollywood");
        address3.setZipCode("15079");
        address3.setCountry("USA");

        customer1 = new Customer();
        customer1.setId("66aeee840271a2600f91d799");
        customer1.setFirstName("Peter");
        customer1.setLastName("Larson");
        customer1.setEmail("plarson@gmail.com");
        customer1.setPhoneNumber("+51 962329330");
        customer1.setAddress(address1);

        customer2 = new Customer();
        customer2.setId("66aeee840271a2600f91d79a");
        customer2.setFirstName("Robert");
        customer2.setLastName("Brown");
        customer2.setEmail("rbrown@gmail.com");
        customer2.setPhoneNumber("+51 964429333");
        customer2.setAddress(address2);

        customer3 = new Customer();
        customer3.setId("66aeee845291a2632f91d95a");
        customer3.setFirstName("Nelson");
        customer3.setLastName("Patrick");
        customer3.setEmail("npatrick@gmail.com");
        customer3.setPhoneNumber("+51 955429113");
        customer3.setAddress(address3);

        mongoTemplate.save(customer1);
        mongoTemplate.save(customer2);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("customers");
    }

    @Test
    @DisplayName("Given two customers in database when find all then return the customers")
    void givenTwoCustomersInDatabase_whenFindAll_thenReturnTheCustomers() {
        List<Customer> foundCustomers = customerRepository.findAll();

        assertThat(foundCustomers)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @DisplayName("Given one customer in database when find by id then return one customer")
    void givenOneCustomerInDatabase_whenFindById_thenReturnOneCustomer() {
        Optional<Customer> foundCustomerOptional = customerRepository.findById(customer1.getId());

        assertThat(foundCustomerOptional)
                .isPresent();
        assertThat(foundCustomerOptional.get())
                .isNotNull()
                .isEqualTo(customer1);
    }

    @Test
    @DisplayName("Given one new customer when save then store one new customer")
    void givenOneNewCustomer_whenSave_thenStoreOneNewCustomer() {
        Customer savedCustomer = customerRepository.save(customer3);

        assertThat(savedCustomer)
                .isNotNull()
                .isEqualTo(customer3);
    }

    @Test
    @DisplayName("Given one customer in database to edit when save then store customer modified")
    void givenOneCustomerInDatabaseToEdit_whenSave_thenStoreCustomerModified() {
        customer2.setEmail("rbrown1980@gmail.com");

        Customer updatedCustomer = customerRepository.save(customer2);

        assertThat(updatedCustomer)
                .isNotNull();
        assertThat(updatedCustomer.getEmail())
                .isEqualTo(customer2.getEmail());
    }

    @Test
    @DisplayName("Given one customer in database when delete by id then remove customer")
    void givenOneCustomerInDatabase_whenDeleteById_thenDeleteCustomerRecordInDatabase() {
        customerRepository.deleteById(customer2.getId());

        Optional<Customer> foundCustomerOptional = customerRepository.findById(customer2.getId());

        assertThat(foundCustomerOptional)
                .isEmpty();
    }

}
