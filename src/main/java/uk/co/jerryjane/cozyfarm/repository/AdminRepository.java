package uk.co.jerryjane.cozyfarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.jerryjane.cozyfarm.model.Admin;

import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Boolean existsByEmail(String email);
    Optional<Admin> findByEmail(String email);
}
