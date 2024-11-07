package se.sakerhet.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import se.sakerhet.server.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}


