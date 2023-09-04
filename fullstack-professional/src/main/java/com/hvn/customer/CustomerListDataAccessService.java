package com.hvn.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    public static  final List<Customer> customers = new ArrayList<>();
    static {
        Customer customer = Customer.builder().id(1).name("HN").email("abc@gmail.com").age(26).build();
        customers.add(customer);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void addOrUpdateCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existCustomerWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existCustomerWithId(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }
}
