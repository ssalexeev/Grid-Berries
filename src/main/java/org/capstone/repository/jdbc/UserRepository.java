package org.capstone.repository.jdbc;

import org.capstone.config.database.DatabaseConnection;
import org.capstone.domain.User;

import java.sql.Connection;
import java.util.Optional;

public class UserRepository implements JdbcRepository<User,Long>{
    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public <S extends User> User save(S entity) {
        return null;
    }

    @Override
    public void delete(User entity) {

    }
}
