package com.hvn.supportpotal.controller;

import com.hvn.supportpotal.configuration.exception.EmailExistException;
import com.hvn.supportpotal.configuration.exception.EmailNotFoundException;
import com.hvn.supportpotal.configuration.exception.UsernameExistException;
import com.hvn.supportpotal.dto.UserDto;
import com.hvn.supportpotal.model.User;
import com.hvn.supportpotal.model.UserPrincipal;
import com.hvn.supportpotal.service.UserService;
import com.hvn.supportpotal.utils.ExceptionHandling;
import com.hvn.supportpotal.utils.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.hvn.supportpotal.constant.CommonConstant.*;
import static com.hvn.supportpotal.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/", "/user"})
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto)
            throws EmailExistException, UsernameExistException, MessagingException {
        User newUser = userService.register(userDto);
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDto userDto)  {
        authenticate(userDto.getUsername(), userDto.getPassword());
        User loginUser = userService.findUserByUsername(userDto.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<User> addOrUpdateUser(@RequestBody UserDto userDto)
            throws EmailExistException, UsernameExistException, IOException {
        User user = userService.addOrUpdateUser(userDto);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> search(@RequestBody UserDto userDto) {
        List<User> users = userService.findAll(userDto);
        return new ResponseEntity<>(users, OK);
    }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<User> resetPassword(@PathVariable("email") String email)
            throws EmailNotFoundException, MessagingException {
         userService.resetPassword(email);
        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(OK).build();
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestBody UserDto userDto)
            throws EmailExistException, IOException, UsernameExistException {
        User user = userService.updateProfileImage(userDto.getUsername(), userDto.getProfileImage());
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chuck = new  byte[1024];
            while ((bytesRead = inputStream.read(chuck)) > 0) {
                byteArrayOutputStream.write(chuck, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
