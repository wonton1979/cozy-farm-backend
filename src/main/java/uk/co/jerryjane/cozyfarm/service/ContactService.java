package uk.co.jerryjane.cozyfarm.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import uk.co.jerryjane.cozyfarm.dto.ContactRequest;


@Service
public class ContactService {

    private final SesClient sesClient;

    public ContactService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void handleContact(ContactRequest contactRequest) {

        Destination destination = Destination.builder().toAddresses("guanyejun@hotmail.com").build();
        Content subject = Content.builder().data("New Cozy Farm contact message").build();
        Content textBody = Content.builder()
                .data(
                        "Name: " + contactRequest.getVisitorName() + "\n" +
                                "Email: " + contactRequest.getVisitorEmail() + "\n\n" +
                                "Message:\n" + contactRequest.getMessage()
                )
                .build();

        Body body = Body.builder()
                .text(textBody)
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

        sesClient.sendEmail(request);

    }


}
