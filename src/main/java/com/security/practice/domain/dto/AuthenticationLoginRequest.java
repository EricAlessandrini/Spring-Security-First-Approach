package com.security.practice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
