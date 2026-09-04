package uk.co.jerryjane.cozyfarm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReplyMessageRequest(

        @NotBlank
        @Size(min=30, max=500)
        String messageToBeReply
)
{}
