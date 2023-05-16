package com.hvn.supportpotal.controller;

import com.hvn.supportpotal.configuration.exception.EmailExistException;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.hvn.supportpotal.constant.SecurityConstant.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/", "/user"})
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) throws EmailExistException, UsernameExistException {
        User newUser = userService.register(userDto);
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDto userDto) throws EmailExistException, UsernameExistException {
        authenticate(userDto.getUsername(), userDto.getPassword());
        User loginUser = userService.findUserByUsername(userDto.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
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
