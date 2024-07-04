package com.security.practice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationSignUpRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Set<String> roles;

}
