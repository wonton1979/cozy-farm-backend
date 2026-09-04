package uk.co.jerryjane.cozyfarm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;
import uk.co.jerryjane.cozyfarm.dto.ContactResponse;
import uk.co.jerryjane.cozyfarm.service.ContactMessageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class VisitorContactController {

    private final ContactMessageService contactMessageService;

    public VisitorContactController(ContactMessageService contactMessageService) {

        this.contactMessageService = contactMessageService;
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> saveAndSendContactMessage(
            @RequestBody @Valid ContactRequest contactRequest
    ) {

        return ResponseEntity.ok().body(contactMessageService.saveAndSendContactMessage(contactRequest));
    }
}
