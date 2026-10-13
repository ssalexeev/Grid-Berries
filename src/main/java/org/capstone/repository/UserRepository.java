package org.capstone.repository;

import org.capstone.domain.User;
import org.capstone.domain.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository implements CrudRepository<User, Long> {

    private final Map<Long, User> database;

    private final Map<String, Long> emailIndex = new ConcurrentHashMap<>();

    private final AtomicLong idSequence;

    public UserRepository() {
        this.database = new ConcurrentHashMap<>();
        this.idSequence = new AtomicLong(1000L);
    }

    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        Long id = emailIndex.get(email.toLowerCase().trim());
        if (id == null) {
            return Optional.empty();
        }

        return findById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Generate ID if new user
        if (user.getId() == null) {
            user.setId(idSequence.getAndIncrement());
        } else {
            User existingUser = database.get(user.getId());
            if (existingUser != null && existingUser.getEmail() != null) {
                emailIndex.remove(existingUser.getEmail().toLowerCase().trim());
            }
        }

        // Put in primary database
        database.put(user.getId(), user);

        // Update secondary index
        if (user.getEmail() != null) {
            emailIndex.put(user.getEmail().toLowerCase().trim(), user.getId());
        }

        return user;
    }

    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or id is null");
        }
        return save(user);
    }

    @Override
    public void delete(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        Long id = user.getId();
        if (id == null || !database.containsKey(id)) {
            throw new RuntimeException("User not found");
        }

        User userToRemove = database.remove(id);
        if (userToRemove != null && userToRemove.getEmail() != null) {
            emailIndex.remove(userToRemove.getEmail().toLowerCase().trim());
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    public List<User> findAllByRole(Role role) {
        if (role == null) return List.of();
        return database.values().stream()
                .filter(u -> role.equals(u.getRole()))
                .toList();
    }
}