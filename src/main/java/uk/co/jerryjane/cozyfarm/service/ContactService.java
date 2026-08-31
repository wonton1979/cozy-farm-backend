package uk.co.jerryjane.cozyfarm.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ses.model.SesException;

import java.util.Map;


@Service
public class ContactService {
    private static final Logger logger =
            LoggerFactory.getLogger(ContactService.class);
    private final SesClient sesClient;
    private final ContactMessageService contactMessageService;
    private final EmailTemplateService emailTemplateService;

    public ContactService(SesClient sesClient, ContactMessageService contactMessageService, EmailTemplateService emailTemplateService) {
        this.sesClient = sesClient;
        this.contactMessageService  = contactMessageService;
        this.emailTemplateService = emailTemplateService;
    }

    public Map<String,String> handleContact(ContactRequest contactRequest) {
        contactMessageService.saveContactMessage(contactRequest);
        String htmlContent = emailTemplateService.buildContactReceiveEmail(contactRequest);
        Destination destination = Destination.builder().toAddresses("guanyejun@hotmail.com").build();
        Content subject = Content.builder().data("New Cozy Farm contact message").build();

        Content htmlBody = Content.builder()
                .data(htmlContent)
                .build();

        Body body = Body.builder()
                .html(htmlBody)
                .build();

        Message message = Message.builder()
                .subject(subject)
                .body(body)
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source("contact@jerry-jane.co.uk")
                .replyToAddresses(contactRequest.getVisitorEmail())
                .destination(destination)
                .message(message)
                .build();

        try {
            sesClient.sendEmail(request);
        } catch (SesException e) {
            logger.error(
                    "Failed to send SES notification for contact from {}",
                    contactRequest.getVisitorEmail(),
                    e
            );
        }
        return Map.of("message", "Message received successfully");
    }
}
