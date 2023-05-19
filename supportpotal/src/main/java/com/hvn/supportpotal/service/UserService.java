package com.hvn.supportpotal.service;

import com.hvn.supportpotal.configuration.exception.EmailExistException;
import com.hvn.supportpotal.configuration.exception.EmailNotFoundException;
import com.hvn.supportpotal.configuration.exception.UsernameExistException;
import com.hvn.supportpotal.dto.UserDto;
import com.hvn.supportpotal.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(UserDto userDto) throws EmailExistException, UsernameExistException, MessagingException;
    List<User> findAll(UserDto userDto);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User addOrUpdateUser (UserDto userDto) throws EmailExistException, UsernameExistException, IOException;
    void deleteUser(long id);
    void resetPassword(String email) throws EmailNotFoundException, MessagingException;
    User updateProfileImage(String username, MultipartFile profileImage) throws EmailExistException, UsernameExistException, IOException;
}
