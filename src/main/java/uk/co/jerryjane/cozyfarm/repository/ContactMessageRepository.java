package uk.co.jerryjane.cozyfarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByRepliedAtIsNullOrderByCreatedAtDesc();
}
