package org.capstone.repository;

import org.capstone.domain.SellerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class SellerProfileRepository implements CrudRepository<SellerProfile, Long> {

    private final Map<Long, SellerProfile> database;
    private final AtomicLong idSequence;

    public SellerProfileRepository(Map<Long, SellerProfile> database) {
        this.database = database;
        this.idSequence = new AtomicLong(1000L);
    }

    @Override
    public Optional<SellerProfile> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(database.get(id));
    }

    public Optional<SellerProfile> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return database.values().stream()
                .filter(seller -> seller.getUser() != null && Objects.equals(seller.getUser().getId(), userId))
                .findFirst();
    }

    @Override
    public List<SellerProfile> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public SellerProfile save(SellerProfile seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Seller is null");
        }

        if (seller.getId() == null) {
            seller.setId(idSequence.getAndIncrement());
        } else {
            delete(seller);
        }

        database.put(seller.getId(), seller);
        return seller;
    }

    public SellerProfile update(SellerProfile seller) {
        if (seller == null || seller.getId() == null) {
            throw new IllegalArgumentException("SellerProfile or id is null");
        }
        return save(seller);
    }

    @Override
    public void delete(SellerProfile seller) {
        if (seller == null) {
            throw new IllegalArgumentException("SellerProfile is null");
        }
        Long id = seller.getId();
        if (id == null) {
            throw new IllegalArgumentException("Id of SellerProfile is null");
        }
        if (!database.containsKey(id)) {
            throw new RuntimeException("Seller profile not found");
        }

        database.remove(id);
    }
}