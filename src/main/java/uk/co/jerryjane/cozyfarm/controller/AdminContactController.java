package uk.co.jerryjane.cozyfarm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.co.jerryjane.cozyfarm.dto.AdminCredentialsRequest;
import uk.co.jerryjane.cozyfarm.dto.ContactResponse;
import uk.co.jerryjane.cozyfarm.dto.ReplyMessageRequest;
import uk.co.jerryjane.cozyfarm.service.AdminService;
import uk.co.jerryjane.cozyfarm.service.ContactMessageService;

import java.util.Map;

@Controller
@RequestMapping("/api/admin")
public class AdminContactController {
    private final ContactMessageService contactMessageService;
    private final AdminService adminService;
    public AdminContactController(ContactMessageService contactMessageService, AdminService adminService)
    {
        this.contactMessageService = contactMessageService;
        this.adminService = adminService;
    }

    @PostMapping("/reply/{messageId}")
    public ResponseEntity<Map<String,String>> replyMessage(
            @PathVariable Long messageId,
            @RequestBody @Valid ReplyMessageRequest replyMessageRequest,
            HttpServletRequest request
    ){
        Long userId = (Long) request.getAttribute("authUserId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok().body(contactMessageService.sendReplyEmail(messageId,replyMessageRequest));
    }

    @GetMapping("/messages/unreplied")
    public ResponseEntity<Map<String, Page<ContactResponse>>> getAllUnrepliedContacts(
            @PageableDefault(size = 5,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authUserId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Page<ContactResponse> contactResponses = contactMessageService.findUnrepliedMessage(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("unrepliedMessages",contactResponses));
    }

    @GetMapping("/messages/replied")
    public ResponseEntity<Map<String, Page<ContactResponse>>> getAllRepliedContacts(
            @PageableDefault(size = 5,sort = "repliedAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authUserId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Page<ContactResponse> contactResponses = contactMessageService.findRepliedMessage(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("repliedMessages",contactResponses));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Map<String,String>> removeMessage(
            @PathVariable Long messageId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("authUserId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok().body(adminService.deleteMessage(messageId));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid AdminCredentialsRequest credentialsRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.adminLogin(credentialsRequest));
    }
}
