package uk.co.jerryjane.cozyfarm.service;

import org.springframework.stereotype.Service;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;
import uk.co.jerryjane.cozyfarm.dto.ContactResponse;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import uk.co.jerryjane.cozyfarm.repository.ContactMessageRepository;

import java.util.*;


@Service
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    public Map<String, String> saveContactMessage(ContactRequest contactRequest) {

        Map<String, String> responseMessage = new HashMap<>();

        ContactMessage contactMessage = new ContactMessage();

        contactMessage.setVisitorName(contactRequest.getVisitorName());
        contactMessage.setVisitorEmail(contactRequest.getVisitorEmail());
        contactMessage.setVisitorMessage(contactRequest.getMessage());

        contactMessageRepository.save(contactMessage);

        responseMessage.put("Save Message", "message saved successfully");

        return responseMessage;
    }

    public List<ContactResponse> findUnrepliedMessage() {
        List<ContactMessage> contactMessages = contactMessageRepository.findByRepliedAtIsNullOrderByCreatedAtDesc();
        List<ContactResponse> contactResponses = new ArrayList<>();
        for (ContactMessage contactMessage : contactMessages) {
            contactResponses.add(ContactResponse.from(contactMessage));
        }
        return contactResponses;
    }
}
