package se.sakerhet.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import se.sakerhet.server.entity.Capsule;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
   // Optional<User> findById(String email);

}