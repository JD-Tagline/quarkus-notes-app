package com.quarkus.notes.repository;

import com.quarkus.notes.model.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    private Map<String, User> userMap = new HashMap<>();

    public UserRepository() {
        User user = new User();
        user.setUsername("user@gmail.com");
        user.setPassword("password");
        userMap.put(user.getUsername(), user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMap.get(username));
    }
}
