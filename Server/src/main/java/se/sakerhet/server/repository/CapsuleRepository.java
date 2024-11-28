package se.sakerhet.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import se.sakerhet.server.entity.Capsule;
import se.sakerhet.server.entity.User;

import java.util.List;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    List<Capsule> findByUser(User user);
    // Optional<User> findById(String email);

}