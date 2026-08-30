package uk.co.jerryjane.cozyfarm.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;
import uk.co.jerryjane.cozyfarm.service.ContactService;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public String submitContact(@RequestBody @Valid ContactRequest contactRequest) {
        contactService.handleContact(contactRequest);
        return "Contact message received";
    }
}
