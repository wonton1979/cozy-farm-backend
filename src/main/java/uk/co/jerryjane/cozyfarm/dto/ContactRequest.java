package uk.co.jerryjane.cozyfarm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactRequest {

    @NotBlank
    @Size(max = 50)
    private String visitorName;

    @NotBlank
    @Email
    @Size(max = 100)
    private String visitorEmail;

    @NotBlank
    @Size(min = 30, max = 1000)
    private String message;

    public ContactRequest(String visitorName, String visitorEmail, String message) {
        this.visitorName = visitorName;
        this.visitorEmail = visitorEmail;
        this.message = message;
    }
}
