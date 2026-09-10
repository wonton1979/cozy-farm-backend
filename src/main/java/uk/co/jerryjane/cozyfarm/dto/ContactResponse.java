package uk.co.jerryjane.cozyfarm.dto;

import uk.co.jerryjane.cozyfarm.model.ContactMessage;

import java.time.Instant;

public record ContactResponse (
        Long id,
        String visitorName,
        String visitorEmail,
        String visitorMessage,
        Instant createdAt,
        String replyMessage
) {
    public static ContactResponse from(ContactMessage contactMessage) {
        return new ContactResponse(
                contactMessage.getId(),
                contactMessage.getVisitorName(),
                contactMessage.getVisitorEmail(),
                contactMessage.getVisitorMessage(),
                contactMessage.getCreatedAt(),
                contactMessage.getReplyMessage()
        );
    }
}
