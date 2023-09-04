package com.hvn.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("envi");
        when(resultSet.getString("email")).thenReturn("envi@gmail.com");
        when(resultSet.getInt("age")).thenReturn(26);

        Customer actual = customerRowMapper.mapRow(resultSet, 1);
        Customer expect = new Customer(1,"envi", "envi@gmail.com", 26);

        assertThat(actual).isEqualTo(expect);
    }
}