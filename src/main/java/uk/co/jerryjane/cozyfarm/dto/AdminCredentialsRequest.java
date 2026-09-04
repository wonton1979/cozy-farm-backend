package uk.co.jerryjane.cozyfarm.dto;

import jakarta.validation.constraints.*;

public record AdminCredentialsRequest(
        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        String password
) {}