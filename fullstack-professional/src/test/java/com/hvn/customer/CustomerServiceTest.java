package com.hvn.customer;

import com.hvn.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomer() {
        underTest.getAllCustomer();

        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        int id = 1;
        Customer customer = new Customer(id, "hn", "hn@gmail.com",26);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void cantGetCustomer() {
        int id = 1;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with Id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        String email = "envi@gmail.com";

        when(customerDao.existCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("envi", "envi@gmail.com",26);

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);


        verify(customerDao).addOrUpdateCustomer(customerArgumentCaptor.capture());

        Customer captor = customerArgumentCaptor.getValue();
        assertThat(captor).isEqualTo(CustomerMapper.mapToEntity(request));
    }

    @Test
    void deleteCustomerById() {
    }

    @Test
    void updateCustomer() {
    }
}