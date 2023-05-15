package com.hvn.supportpotal.controller;

import com.hvn.supportpotal.configuration.exception.EmailExistException;
import com.hvn.supportpotal.utils.ExceptionHandling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserController extends ExceptionHandling {

    @GetMapping("/show")
    public String showUser() throws EmailExistException {
        throw new EmailExistException("This email is ready taken");
//        return "application work";
    }
}
