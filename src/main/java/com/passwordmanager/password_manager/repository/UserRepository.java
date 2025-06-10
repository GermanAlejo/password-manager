package com.passwordmanager.password_manager.repository;

import com.passwordmanager.password_manager.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Inherits methods like save(), findById(), findAll(), deleteById(), etc.
    //Custom methods
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
