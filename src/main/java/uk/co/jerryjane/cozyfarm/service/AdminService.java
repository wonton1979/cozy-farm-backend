package uk.co.jerryjane.cozyfarm.service;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.co.jerryjane.cozyfarm.dto.AdminCredentialsRequest;
import uk.co.jerryjane.cozyfarm.model.Admin;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import uk.co.jerryjane.cozyfarm.repository.AdminRepository;
import uk.co.jerryjane.cozyfarm.repository.ContactMessageRepository;
import uk.co.jerryjane.cozyfarm.security.JwtService;

import java.util.Map;

@Service
public class AdminService {

    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    private final AdminRepository adminRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final JwtService jwtService;

    public AdminService(AdminRepository adminRepository,
                        JwtService jwtService,
                        ContactMessageRepository contactMessageRepository) {
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
        this.contactMessageRepository = contactMessageRepository;
    }

    public void saveAdmin(AdminCredentialsRequest  adminCredentialsRequest) {
        boolean isAdminExist = adminRepository.existsByEmail(adminCredentialsRequest.email());
        if (isAdminExist) {
            return;
        }
        Admin admin = new Admin();
        admin.setEmail(adminCredentialsRequest.email());
        admin.setPasswordHash(enc.encode(adminCredentialsRequest.password()));
        adminRepository.save(admin);
    }

    public Map<String,String> adminLogin(AdminCredentialsRequest  adminCredentialsRequest) {
        Admin admin = adminRepository.findByEmail(adminCredentialsRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!enc.matches(adminCredentialsRequest.password(), admin.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return Map.of("token",jwtService.generateToken(admin.getId(), admin.getEmail()));
    }

    public Map<String,String> deleteMessage(Long messageId){

        ContactMessage contactMessage = contactMessageRepository.findContactMessageByRepliedAtIsNotNullAndId(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found or Not Allowed"));

        contactMessageRepository.delete(contactMessage);
        return Map.of("message", "Message Deleted Successfully");
    }
}
