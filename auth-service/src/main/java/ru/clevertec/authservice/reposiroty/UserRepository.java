package ru.clevertec.authservice.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.authservice.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
