package com.hvn.supportpotal.resource;

import com.hvn.supportpotal.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserResource {

    @GetMapping("/show")
    public String showUser() {
        return "application work";
    }
}
