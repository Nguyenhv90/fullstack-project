package com.hvn.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {

        autoCloseable = MockitoAnnotations.openMocks(this);

        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        int id = 1;

        underTest.selectCustomerById(id);
        verify(customerRepository).findById(id);
    }

    @Test
    void addOrUpdateCustomer() {
    }

    @Test
    void existCustomerWithEmail() {
    }

    @Test
    void existCustomerWithId() {
    }

    @Test
    void deleteCustomerById() {
    }
}