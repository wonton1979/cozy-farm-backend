package uk.co.jerryjane.cozyfarm.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailTemplateService {

    public String buildContactReceiveEmail(ContactRequest contactRequest) {
        String template = loadTemplate("templates/email/contact-receive.html");

        return template
                .replace("{{visitorName}}", contactRequest.getVisitorName())
                .replace("{{visitorEmail}}", contactRequest.getVisitorEmail())
                .replace("{{visitorMessage}}", contactRequest.getMessage());
    }

    private String loadTemplate(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);

            return new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load email template: " + path,
                    e
            );
        }
    }
}