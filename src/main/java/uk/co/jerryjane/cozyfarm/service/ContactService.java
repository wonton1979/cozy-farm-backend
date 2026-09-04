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
    private final EmailTemplateService emailTemplateService;

    public ContactService(SesClient sesClient, EmailTemplateService emailTemplateService) {
        this.sesClient = sesClient;
        this.emailTemplateService = emailTemplateService;
    }

    public Map<String,String> handleContact(Map<String,Object> sendEmailRequest) {
        String htmlContent = null;
        String destinationEmail = null;
        String emailTitle = "A Message from Cozy Farm";
        String replyToEmailAddresses = "guanyejun@hotmail.com";
        if(sendEmailRequest.containsKey("messageType") && sendEmailRequest.get("messageType").equals("send")) {
            ContactRequest visitorMessageDetails = (ContactRequest) sendEmailRequest.get("data");
            htmlContent = emailTemplateService.buildContactReceiveEmail(visitorMessageDetails);
            destinationEmail = "guanyejun@hotmail.com";
            emailTitle = "New Cozy Farm contact message";
            replyToEmailAddresses = visitorMessageDetails.getVisitorEmail();
        }

        if(sendEmailRequest.containsKey("messageType") && sendEmailRequest.get("messageType").equals("reply")){
            ContactRequest hostsMessageDetails = (ContactRequest) sendEmailRequest.get("data");
            htmlContent = emailTemplateService.buildContactReplyEmail(hostsMessageDetails);
            destinationEmail = hostsMessageDetails.getVisitorEmail();
        }

        Destination destination = Destination.builder().toAddresses(destinationEmail).build();
        Content subject = Content.builder().data(emailTitle).build();

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
                .replyToAddresses(replyToEmailAddresses)
                .destination(destination)
                .message(message)
                .build();

        try {
            sesClient.sendEmail(request);
        }  catch (SesException e) {
            logger.error(
                    "Failed to send SES email for message type {}",
                    sendEmailRequest.get("messageType"),
                    e
            );

            if ("reply".equals(sendEmailRequest.get("messageType"))) {
                throw e;
            }
        }
        return Map.of("message", "Message Send Successfully");
    }
}
