package org.capstone.repository.jdbc;

import org.capstone.repository.CrudRepository;

import java.util.Optional;

public interface JdbcRepository<T,ID> extends CrudRepository<T,ID> {
}
