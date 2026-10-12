package org.capstone.repository;

import org.capstone.domain.User;
import org.capstone.domain.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository implements CrudRepository<User, Long> {

    private final List<User> database;
    private final AtomicLong idSequence;

    public UserRepository() {
        this.database = new ArrayList<>();
        this.idSequence = new AtomicLong(1000L);
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return database.stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(database);
    }

    public List<User> findAllByRole(Role role) {
        return database.stream()
                .filter(user -> role.equals(user.getRole()))
                .toList();
    }

    @Override
    public User save(User user) {
        if (user == null) {
            return null;
        }

        if (user.getId() == null) {
            user.setId(idSequence.getAndIncrement());
        } else {
            delete(user);
        }

        database.add(user);
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
        Long id = user.getId();
        if (id == null) {
            throw new IllegalArgumentException("Id of user is null");
        }
        if (findById(id) == null){
            throw  new RuntimeException("User not found");
        }

        database.removeIf(u -> Objects.equals(u.getId(), id));
    }
}
