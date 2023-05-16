package com.hvn.supportpotal.service;

import com.hvn.supportpotal.configuration.exception.EmailExistException;
import com.hvn.supportpotal.configuration.exception.UsernameExistException;
import com.hvn.supportpotal.dto.UserDto;
import com.hvn.supportpotal.model.User;

import java.util.List;

public interface UserService {
    User register(UserDto userDto) throws EmailExistException, UsernameExistException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
