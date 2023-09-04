package com.hvn.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerId);
    void addOrUpdateCustomer(Customer customer);
    boolean existCustomerWithEmail(String email);
    boolean existCustomerWithId(Integer id);
    void deleteCustomerById(Integer id);
}
