package com.hvn.customer;

import com.hvn.exception.RequestValidationException;
import com.hvn.exception.ResourceNotFoundException;
import com.hvn.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {


    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomer() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with Id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        if (customerDao.existCustomerWithEmail(request.email())) {
            throw new DuplicateResourceException("Email already taken");
        }
        Customer customer = Customer.builder()
                .name(request.name())
                .email(request.email())
                .age(request.age())
                .build();
        customerDao.addOrUpdateCustomer(customer);
    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDao.existCustomerWithId(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer with Id [%s] not found".formatted(customerId)
            );
        }

        customerDao.deleteCustomerById(customerId);
    }

    public void updateCustomer(Integer id, CustomerRegistrationRequest request) {
        Customer customer = getCustomer(id);

        boolean changes = false;

        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            changes = true;
        }

        if (request.email() != null && !request.email().equals(customer.getEmail())) {

            if (customerDao.existCustomerWithEmail(request.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(request.email());
            changes = true;
        }

        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        customerDao.addOrUpdateCustomer(customer);
    }
}
