package uk.co.jerryjane.cozyfarm.dto;

import uk.co.jerryjane.cozyfarm.model.ContactMessage;

import java.time.Instant;

public record ContactResponse (
        String visitorName,
        String visitorEmail,
        String visitorMessage,
        Instant createdAt
) {
    public static ContactResponse from(ContactMessage contactMessage) {
        return new ContactResponse(
                contactMessage.getVisitorName(),
                contactMessage.getVisitorEmail(),
                contactMessage.getVisitorMessage(),
                contactMessage.getCreatedAt()
        );
    }
}
