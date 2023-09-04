package com.hvn.customer;

public record CustomerRegistrationRequest(
    String name,
    String email,
    Integer age
){ }
