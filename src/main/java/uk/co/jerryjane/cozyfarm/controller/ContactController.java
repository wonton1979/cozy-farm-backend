package uk.co.jerryjane.cozyfarm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;
import uk.co.jerryjane.cozyfarm.dto.ContactResponse;
import uk.co.jerryjane.cozyfarm.service.ContactMessageService;
import uk.co.jerryjane.cozyfarm.service.ContactService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;
    private final ContactMessageService contactMessageService;

    public ContactController(ContactService contactService, ContactMessageService contactMessageService) {
        this.contactService = contactService;
        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> sendContactMessage(@RequestBody @Valid ContactRequest contactRequest) {
        Map<String,String> sendMessageResponse = contactService.handleContact(contactRequest);
        return ResponseEntity.status(HttpStatus.OK).body(sendMessageResponse);
    }

    @GetMapping
    public ResponseEntity<Map<String,List<ContactResponse>>> getAllUnrepliedContacts() {
        List<ContactResponse> contactResponses = contactMessageService.findUnrepliedMessage();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("unrepliedMessages",contactResponses));
    }

}
