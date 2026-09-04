package uk.co.jerryjane.cozyfarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import java.util.List;
import java.util.Optional;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByRepliedAtIsNullOrderByCreatedAtDesc();
    Optional<ContactMessage> findContactMessageByRepliedAtIsNullAndId(Long id);
    Optional<ContactMessage> findContactMessageByRepliedAtIsNotNullAndId(Long id);
}
