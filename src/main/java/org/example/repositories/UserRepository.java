package org.example.repositories;

import org.example.entities.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean isUserHasOverdue(String id);

    Collection<User> findAll();
}
