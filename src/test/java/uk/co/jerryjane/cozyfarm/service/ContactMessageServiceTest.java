package uk.co.jerryjane.cozyfarm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import uk.co.jerryjane.cozyfarm.repository.ContactMessageRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactMessageServiceTest {

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @InjectMocks
    private ContactMessageService contactMessageService;

    @Test
    @DisplayName("Valid contact request should be converted and saved correctly")
    void shouldSaveContactMessage() {

        // Arrange
        ContactRequest contactRequest = new ContactRequest(
                "Jerry",
                "jerry@example.com",
                "Hello from Cozy Farm, this is a valid contact message."
        );

        ArgumentCaptor<ContactMessage> captor =
                ArgumentCaptor.forClass(ContactMessage.class);

        // Act
        contactMessageService.saveAndSendContactMessage(contactRequest);

        // Assert
        verify(contactMessageRepository)
                .save(captor.capture());

        ContactMessage savedMessage =
                captor.getValue();

        assertEquals(
                "Jerry",
                savedMessage.getVisitorName()
        );

        assertEquals(
                "jerry@example.com",
                savedMessage.getVisitorEmail()
        );

        assertEquals(
                "Hello from Cozy Farm, this is a valid contact message.",
                savedMessage.getVisitorMessage()
        );
    }
}