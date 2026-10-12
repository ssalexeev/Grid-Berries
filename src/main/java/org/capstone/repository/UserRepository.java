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
    private final AtomicLong idSequence;

    public UserRepository() {
        this.database = new ConcurrentHashMap<>();
        this.idSequence = new AtomicLong(1000L);
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    public List<User> findAllByRole(Role role) {
        if (role == null) {
            return List.of();
        }
        return database.values().stream()
                .filter(user -> role.equals(user.getRole()))
                .toList();
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getId() == null) {
            user.setId(idSequence.getAndIncrement());
        } else {
            delete(user);
        }

        database.put(user.getId(), user);
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
        if (id == null) {
            throw new IllegalArgumentException("Id of user is null");
        }
        if (!database.containsKey(id)) {
            throw new RuntimeException("User not found");
        }

        database.remove(id);
    }
}