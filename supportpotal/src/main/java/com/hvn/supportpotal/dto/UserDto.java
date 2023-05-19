package com.hvn.supportpotal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean isActive;
    private boolean isNotLocked;
    private MultipartFile profileImage;
    private String currentUsername;
}
