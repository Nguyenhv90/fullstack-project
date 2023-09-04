package com.hvn.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
@RequiredArgsConstructor
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate
                .query(sql, customerRowMapper, customerId).stream().findFirst();
    }

    @Override
    public void addOrUpdateCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age) 
                VALUES (?, ?, ?);
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("jdbc.update = "+ result);
    }

    @Override
    public boolean existCustomerWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count >0;
    }

    @Override
    public boolean existCustomerWithId(Integer id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count >0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
