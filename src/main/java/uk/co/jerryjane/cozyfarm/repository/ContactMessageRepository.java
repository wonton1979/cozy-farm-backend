package uk.co.jerryjane.cozyfarm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.jerryjane.cozyfarm.model.ContactMessage;
import java.util.Optional;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByRepliedAtIsNullOrderByCreatedAtDesc(Pageable pageable);
    Optional<ContactMessage> findContactMessageByRepliedAtIsNullAndId(Long id);
    Optional<ContactMessage> findContactMessageByRepliedAtIsNotNullAndId(Long id);
    Page<ContactMessage> findAllByRepliedAtIsNotNullOrderByRepliedAtDesc(Pageable pageable);
}
