package com.howtodoinjava.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginResponseDTO {
    private String accessToken;
    private UUID id;
    private String name;
    private String email;
    private final String tokenType = "Bearer";
}
