package com.passwordmanager.password_manager.repository;

import com.passwordmanager.password_manager.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Inherits methods like save(), findById(), findAll(), deleteById(), etc.
    //Custom methods
    User findByUsername(String name);
    User findByEmail(String email);
}
