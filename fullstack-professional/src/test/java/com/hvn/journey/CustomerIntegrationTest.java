package com.hvn.journey;


import com.hvn.customer.Customer;
import com.hvn.customer.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient testClient;

    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisCustomer() {
        String name = "envi26";
        String email = "envi-" + UUID.randomUUID() + "@nguyenhv.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email,26);

        //Send a post request
        testClient.post()
                .uri(CUSTOMER_URI)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get All Customer

        List<Customer> allCustomer = testClient.get()
                .uri(CUSTOMER_URI)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(name, email,26);

        assertThat(allCustomer)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);


        var id = allCustomer.stream()
                .filter(c -> c.getEmail().equals(expected.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expected.setId(id);

        testClient.get()
                .uri(CUSTOMER_URI +"/{id}", id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expected);
    }

    @Test
    void canDeleteCustomer() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "envi2", "envi2@gmail.com",26);

        //Send a post request
        testClient.post()
                .uri(CUSTOMER_URI)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get All Customer

        List<Customer> allCustomer = testClient.get()
                .uri(CUSTOMER_URI)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();




        var id = allCustomer.stream()
                .filter(c -> c.getEmail().equals(request.email()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //delete customer
        testClient.delete()
                .uri(CUSTOMER_URI +"/{id}", id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();


        //Select Customer
        testClient.get()
                .uri(CUSTOMER_URI +"/{id}", id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
