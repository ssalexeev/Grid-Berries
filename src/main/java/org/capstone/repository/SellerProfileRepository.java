package org.capstone.repository;

import org.capstone.domain.SellerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

public class SellerProfileRepository implements CrudRepository<SellerProfile, Long> {

    private final List<SellerProfile> database;
    private final AtomicLong idSequence;

    public SellerProfileRepository() {
        this.database = new ArrayList<>();
        this.idSequence = new AtomicLong(1000L);
    }

    @Override
    public Optional<SellerProfile> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return database.stream()
                .filter(seller -> Objects.equals(seller.getId(), id))
                .findFirst();
    }

    public Optional<SellerProfile> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }

        return database.stream()
                .filter(seller -> seller.getUser() != null && Objects.equals(seller.getUser().getId(), userId))
                .findFirst();
    }

    @Override
    public List<SellerProfile> findAll() {
        return new ArrayList<>(database);
    }

    @Override
    public SellerProfile save(SellerProfile seller) {
        if (seller == null) {
            return null;
        }

        if (seller.getId() == null) {
            seller.setId(idSequence.getAndIncrement());
        } else {
            delete(seller);
        }

        database.add(seller);
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
        if (findById(id).isEmpty()) {
            throw new RuntimeException("Seller profile not found");
        }

        database.removeIf(s -> Objects.equals(s.getId(), id));
    }
}