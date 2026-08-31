package uk.co.jerryjane.cozyfarm.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ContactMessageTest {

    @Test
    void shouldSetCreatedAtBeforePersisting() {
        ContactMessage contactMessage = new ContactMessage();

        contactMessage.prePersist();

        assertNotNull(contactMessage.getCreatedAt());
    }

    @Test
    void shouldSetStatusToNewWhenStatusIsNull() {
        ContactMessage contactMessage = new ContactMessage();

        contactMessage.prePersist();

        assertEquals(
                ContactMessageStatus.NEW,
                contactMessage.getStatus()
        );
    }

    @Test
    void shouldKeepExistingStatusWhenStatusIsNotNull() {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setStatus(ContactMessageStatus.REPLIED);

        contactMessage.prePersist();

        assertEquals(
                ContactMessageStatus.REPLIED,
                contactMessage.getStatus()
        );
    }

}