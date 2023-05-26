package com.hvn.supportpotal.service.impl;

import com.hvn.supportpotal.configuration.exception.EmailExistException;
import com.hvn.supportpotal.configuration.exception.EmailNotFoundException;
import com.hvn.supportpotal.configuration.exception.UsernameExistException;
import com.hvn.supportpotal.dto.UserDto;
import com.hvn.supportpotal.enumeration.Role;
import com.hvn.supportpotal.model.User;
import com.hvn.supportpotal.model.UserPrincipal;
import com.hvn.supportpotal.repository.UserRepository;
import com.hvn.supportpotal.service.EmailService;
import com.hvn.supportpotal.service.LoginAttemptService;
import com.hvn.supportpotal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static com.hvn.supportpotal.constant.CommonConstant.*;
import static com.hvn.supportpotal.enumeration.Role.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error(USER_NOT_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);

            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user){
        if (user.isNotLocked()) {
            user.setNotLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    @Override
    public User register(UserDto userDto) throws EmailExistException, UsernameExistException, MessagingException {
        validateUser(userDto.getCurrentUsername(), userDto.getUsername(), userDto.getEmail());

        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        User user = User.builder()
                .userId(generateUserId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(encodedPassword)
                .joinDate(new Date())
                .isActive(true)
                .isNotLocked(true)
                .role(ROLE_USER.name())
                .authorities(ROLE_USER.getAuthorities())
                .profileImageUrl(getTempFileImageUrl(userDto.getUsername()))
                .build();
        userRepository.save(user);
        userDto.setPassword(password);
        String content = String.format(CONTENT_NEW_PASSWORD, userDto.getEmail(), password);
        emailService.sendNewEmail(userDto.getEmail(), content, SUBJECT_NEW_PASSWORD);
        LOGGER.info("New user password: " + password);
        return user;
    }

    private String getTempFileImageUrl(String username) {
        return TEMP_PROFILE_IMAGE_BASE_URL + username + TEMP_PROFILE_IMAGE_CAT_STYLE;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateUser(String currentUsername, String newUsername, String newEmail)
            throws UsernameExistException, EmailExistException {
        User userByUsername = findUserByUsername(newUsername);
        User userByEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UsernameExistException(USER_NOT_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }

            return currentUser;
        } else {
            if (userByUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return new User();
        }
    }

    @Override
    public List<User> findAll(UserDto userDto) {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User addOrUpdateUser(UserDto userDto)
            throws EmailExistException, UsernameExistException, IOException {

        User user = validateUser(userDto.getCurrentUsername(), userDto.getUsername(), userDto.getEmail());
        String password = generatePassword();
        Role role = userDto.getRole().isBlank() ? ROLE_USER : getRoleEnumName(userDto.getRole());
        if (userDto.getCurrentUsername().isBlank()) {
            user.setUserId(generateUserId());
            user.setProfileImageUrl(getTempFileImageUrl(userDto.getUsername()));
            user.setJoinDate(new Date());
            user.setPassword(encodePassword(password));
        }
        user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .isActive(true)
                .isNotLocked(true)
                .role(role.name())
                .authorities(role.getAuthorities())
                .build();
        userRepository.save(user);
        saveProfileImage(user, userDto.getProfileImage());
        return null;
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
        if (profileImage != null) {
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
       return ServletUriComponentsBuilder
               .fromCurrentContextPath()
               .path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + JPG_EXTENSION)
               .toUriString();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void resetPassword(String email) throws EmailNotFoundException, MessagingException {
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
            throw new EmailNotFoundException(USER_NOT_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        emailService.sendNewEmail(email, CONTENT_RESET_PASSWORD, SUBJECT_RESET_PASSWORD);
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage)
            throws EmailExistException, UsernameExistException, IOException {
        User user = validateUser(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }
}
