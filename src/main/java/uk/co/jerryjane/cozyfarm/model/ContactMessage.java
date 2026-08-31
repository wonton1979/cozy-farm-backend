package uk.co.jerryjane.cozyfarm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact_messages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String visitorName;

    @Column(nullable = false, length = 100)
    private String visitorEmail;

    @Column(nullable = false, length = 1000)
    private String visitorMessage;

    @Column(length = 1000)
    private String replyMessage;

    @Enumerated(EnumType.STRING)
    private ContactMessageStatus status;

    private Instant createdAt;
    private Instant repliedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();

        if (this.status == null) {
            this.status = ContactMessageStatus.NEW;
        }
    }
}