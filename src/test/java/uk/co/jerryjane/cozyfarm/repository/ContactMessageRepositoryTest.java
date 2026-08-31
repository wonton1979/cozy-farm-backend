package uk.co.jerryjane.cozyfarm.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import uk.co.jerryjane.cozyfarm.model.ContactMessageStatus;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class ContactMessageRepositoryTest {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Data should be saved to database when input is valid")
    void shouldPersistContactMessage() {


        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setVisitorName("Jerry");
        contactMessage.setVisitorEmail("jerry@example.com");
        contactMessage.setVisitorMessage(
                "Hello from Cozy Farm, this is a valid contact message."
        );

        ContactMessage savedMessage =
                contactMessageRepository.saveAndFlush(contactMessage);

        Long savedId = savedMessage.getId();

        entityManager.clear();

        ContactMessage foundMessage =
                contactMessageRepository.findById(savedId)
                        .orElseThrow();

        // Assert
        assertNotNull(foundMessage.getId());

        assertEquals(
                "Jerry",
                foundMessage.getVisitorName()
        );

        assertEquals(
                "jerry@example.com",
                foundMessage.getVisitorEmail()
        );

        assertEquals(
                "Hello from Cozy Farm, this is a valid contact message.",
                foundMessage.getVisitorMessage()
        );

        assertEquals(
                ContactMessageStatus.NEW,
                foundMessage.getStatus()
        );

        assertNotNull(foundMessage.getCreatedAt());

        assertNull(foundMessage.getReplyMessage());
        assertNull(foundMessage.getRepliedAt());
    }

    @Test
    @DisplayName("Existing status should not be overwritten when data is saved")
    void shouldKeepExistingStatusWhenPersisted() {

        // Arrange
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setVisitorName("Jerry");
        contactMessage.setVisitorEmail("jerry@example.com");
        contactMessage.setVisitorMessage(
                "Hello from Cozy Farm, this is a valid contact message."
        );
        contactMessage.setStatus(ContactMessageStatus.REPLIED);

        // Act
        ContactMessage savedMessage =
                contactMessageRepository.saveAndFlush(contactMessage);

        Long savedId = savedMessage.getId();

        entityManager.clear();

        ContactMessage foundMessage =
                contactMessageRepository.findById(savedId)
                        .orElseThrow();

        // Assert
        assertEquals(
                ContactMessageStatus.REPLIED,
                foundMessage.getStatus()
        );
    }
}
