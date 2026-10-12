package org.capstone.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> findById(ID id);

    Iterable<T> findAll();

    <S extends T> T save(S entity);

    void delete(T entity);


}
