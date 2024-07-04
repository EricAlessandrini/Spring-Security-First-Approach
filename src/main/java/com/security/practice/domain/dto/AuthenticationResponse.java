package com.security.practice.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private String username;

    private String message;

    private String jwtToken;

    private boolean status;
}
