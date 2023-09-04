package com.hvn.customer;

public class CustomerMapper {
    public static Customer mapToEntity(CustomerRegistrationRequest request) {
        return Customer.builder()
                .name(request.name())
                .email(request.email())
                .age(request.age())
                .build();
    }
}
