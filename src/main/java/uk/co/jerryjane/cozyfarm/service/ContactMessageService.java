package uk.co.jerryjane.cozyfarm.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;
import uk.co.jerryjane.cozyfarm.dto.ContactResponse;
import uk.co.jerryjane.cozyfarm.dto.ReplyMessageRequest;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import uk.co.jerryjane.cozyfarm.model.ContactMessageStatus;
import uk.co.jerryjane.cozyfarm.repository.ContactMessageRepository;

import java.time.Instant;
import java.util.*;


@Service
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
    private final ContactService contactService;

    public ContactMessageService(ContactMessageRepository contactMessageRepository, ContactService contactService) {
        this.contactMessageRepository = contactMessageRepository;
        this.contactService = contactService;
    }

    @Transactional
    public Map<String, String> saveAndSendContactMessage(ContactRequest contactRequest) {

        ContactMessage contactMessage = new ContactMessage();

        contactMessage.setVisitorName(contactRequest.getVisitorName());
        contactMessage.setVisitorEmail(contactRequest.getVisitorEmail());
        contactMessage.setVisitorMessage(contactRequest.getMessage());
        contactMessageRepository.save(contactMessage);

        contactService.handleContact(Map.of(
                "messageType","send",
                "data",contactRequest
        ));

        return Map.of("Save Message", "message saved successfully");
    }

    public ContactMessage updateContactMessage(Long messageId,ReplyMessageRequest replyMessageRequest) {

        ContactMessage  unrepliedMessage = findUnrepliedMessageById(messageId);

        unrepliedMessage.setRepliedAt(Instant.now());
        unrepliedMessage.setReplyMessage(replyMessageRequest.messageToBeReply());
        unrepliedMessage.setStatus(ContactMessageStatus.REPLIED);

        contactMessageRepository.save(unrepliedMessage);

        return unrepliedMessage;
    }

    public List<ContactResponse> findUnrepliedMessage() {
        List<ContactMessage> contactMessages = contactMessageRepository.findByRepliedAtIsNullOrderByCreatedAtDesc();
        List<ContactResponse> contactResponses = new ArrayList<>();
        for (ContactMessage contactMessage : contactMessages) {
            contactResponses.add(ContactResponse.from(contactMessage));
        }
        return contactResponses;
    }

    public ContactMessage findUnrepliedMessageById(Long messageId) {
        return contactMessageRepository.findContactMessageByRepliedAtIsNullAndId(messageId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No message has been found !")
        );
    }

    @Transactional
    public Map<String,String> sendReplyEmail(Long messageId, ReplyMessageRequest replyMessageRequest) {
        ContactMessage unReplyMessage = updateContactMessage(messageId,replyMessageRequest);
        ContactRequest readyToReplyMessage = new ContactRequest();
        readyToReplyMessage.setVisitorName(unReplyMessage.getVisitorName());
        readyToReplyMessage.setVisitorEmail(unReplyMessage.getVisitorEmail());
        readyToReplyMessage.setMessage(replyMessageRequest.messageToBeReply());
        contactService.handleContact(
                Map.of(
                        "messageType","reply",
                        "data", readyToReplyMessage
                )
        );
        return Map.of("Reply Message", "Message has been updated and replied successfully");
    }
}
