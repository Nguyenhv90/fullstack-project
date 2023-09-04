package com.hvn.customer;

import com.hvn.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainer {

    private CustomerJDBCDataAccessService serviceTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        serviceTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = Customer.builder().name("HN").email("abc@gmail.com").age(26).build();
        serviceTest.addOrUpdateCustomer(customer);
        List<Customer> actual =  serviceTest.selectAllCustomers();

        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        Customer customer = Customer.builder().name("HN").email("abc@gmail.com").age(26).build();
        serviceTest.addOrUpdateCustomer(customer);

        int id = serviceTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual =  serviceTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
                });
    }

    @Test
    void addOrUpdateCustomer() {
    }

    @Test
    void existCustomerWithEmail() {
        Customer customer = Customer.builder().name("HN").email("abc@gmail.com").age(26).build();
        serviceTest.addOrUpdateCustomer(customer);

        boolean actual = serviceTest.existCustomerWithEmail(customer.getEmail());
        assertThat(actual).isTrue();
    }

    @Test
    void existCustomerWithId() {
        Customer customer = Customer.builder().name("HN").email("abc@gmail.com").age(26).build();
        serviceTest.addOrUpdateCustomer(customer);

        int id = serviceTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = serviceTest.existCustomerWithId(id);
        assertThat(actual).isTrue();
    }

    @Test
    void deleteCustomerById() {
        Customer customer = Customer.builder().name("HN").email("abc@gmail.com").age(26).build();
        serviceTest.addOrUpdateCustomer(customer);

        int id = serviceTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual1 = serviceTest.existCustomerWithId(id);

        serviceTest.deleteCustomerById(id);
        boolean actual = serviceTest.existCustomerWithId(id);
        assertThat(actual).isFalse();
    }
}