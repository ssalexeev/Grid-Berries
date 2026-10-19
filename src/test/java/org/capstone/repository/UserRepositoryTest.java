package org.capstone.repository;


import org.capstone.domain.User;
import org.capstone.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;


class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
    }

    // ==========================================
    // findById Tests
    // ==========================================
    @Nested
    class FindByIdTests {

        @Test
        void findById_WhenIdIsNull_ReturnsEmptyOptional() {
            Optional<User> result = repository.findById(null);

            assertTrue(result.isEmpty());
        }

        @Test
        void findById_WhenUserExists_ReturnsOptionalWithUser() {
            User user = User.builder()
                    .email("alex@example.com")
                    .passwordHash("hashed_password_123")
                    .role(Role.BUYER)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            User saved = repository.save(user);

            Optional<User> result = repository.findById(saved.getId());

            assertTrue(result.isPresent());
            assertEquals(saved.getId(), result.get().getId());
            assertEquals("alex@example.com", result.get().getEmail());
            assertEquals("hashed_password_123", result.get().getPasswordHash());
            assertEquals(Role.BUYER, result.get().getRole());
            assertTrue(result.get().getIsActive());
        }

        @Test
        void findById_WhenUserDoesNotExist_ReturnsEmptyOptional() {
            Optional<User> result = repository.findById(999L);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByEmailTests {

        @Test
        void findByEmail_WhenEmailIsNullOrEmptyOrBlank_ReturnsEmptyOptional() {
            String invalidEmail = ";ffd";
            Optional<User> result = repository.findByEmail(invalidEmail);

            assertTrue(result.isEmpty());
        }

        @Test
        void findByEmail_WhenUserExists_ReturnsUserIgnoringCaseAndSpaces() {
            User user = User.builder()
                    .email("Alex@Example.com")
                    .passwordHash("secret123")
                    .role(Role.SELLER)
                    .build();

            repository.save(user);

            Optional<User> result = repository.findByEmail("  alex@example.com  ");

            assertTrue(result.isPresent());
            assertEquals("Alex@Example.com", result.get().getEmail());
            assertEquals(Role.SELLER, result.get().getRole());
        }

        @Test
        void findByEmail_WhenEmailDoesNotExist_ReturnsEmptyOptional() {
            Optional<User> result = repository.findByEmail("notfound@example.com");

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class SaveAndUpdateTests {

        @Test
        void save_WhenUserIsNull_ThrowsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> repository.save(null)
            );

            assertEquals("User cannot be null", ex.getMessage());
        }

        @Test
        void save_WhenNewUser_AssignsIdAndIndexesEmail() {
            User newUser = User.builder()
                    .email("sam@example.com")
                    .passwordHash("pass456")
                    .role(Role.BUYER)
                    .isActive(true)
                    .build();

            User savedUser = repository.save(newUser);

            assertNotNull(savedUser.getId());
            assertEquals(1000L, savedUser.getId());

            assertTrue(repository.findById(1000L).isPresent());
            assertTrue(repository.findByEmail("sam@example.com").isPresent());
        }

        @Test
        void update_WhenUserOrIdIsNull_ThrowsIllegalArgumentException() {
            User userWithoutId = User.builder()
                    .email("noid@example.com")
                    .build();

            assertThrows(IllegalArgumentException.class, () -> repository.update(null));
            assertThrows(IllegalArgumentException.class, () -> repository.update(userWithoutId));
        }

        @Test
        void update_WhenValidUser_UpdatesSuccessfully() {
            User user = User.builder()
                    .email("original@example.com")
                    .role(Role.BUYER)
                    .isActive(true)
                    .build();

            User saved = repository.save(user);

            saved.setRole(Role.ADMIN);
            saved.setIsActive(false);

            User updated = repository.update(saved);

            assertEquals(Role.ADMIN, updated.getRole());
            assertFalse(updated.getIsActive());
            assertEquals(Role.ADMIN, repository.findById(saved.getId()).get().getRole());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void delete_WhenUserIsNull_ThrowsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> repository.delete(null)
            );

            assertEquals("User cannot be null", ex.getMessage());
        }

        @Test
        void delete_WhenUserHasNullIdOrNotInDatabase_ThrowsRuntimeException() {
            User userWithoutId = User.builder().email("noid@example.com").build();
            User userNotInDb = User.builder().id(9999L).email("ghost@example.com").build();

            assertThrows(RuntimeException.class, () -> repository.delete(userWithoutId));
            assertThrows(RuntimeException.class, () -> repository.delete(userNotInDb));
        }

        @Test
        void delete_WhenUserExists_RemovesFromDatabaseAndEmailIndex() {
            User user = User.builder()
                    .email("alex@example.com")
                    .passwordHash("hash")
                    .build();

            User savedUser = repository.save(user);

            repository.delete(savedUser);

            assertTrue(repository.findById(savedUser.getId()).isEmpty());
            assertTrue(repository.findByEmail("alex@example.com").isEmpty());
        }
    }

    @Nested
    class QueryAllTests {

        @Test
        void findAll_ReturnsAllSavedUsers() {
            repository.save(User.builder().email("u1@example.com").build());
            repository.save(User.builder().email("u2@example.com").build());

            List<User> allUsers = repository.findAll();

            assertEquals(2, allUsers.size());
        }

        @Test
        void findAllByRole_WhenRoleIsNull_ReturnsEmptyList() {
            List<User> result = repository.findAllByRole(null);

            assertTrue(result.isEmpty());
        }

        @Test
        void findAllByRole_ReturnsOnlyMatchingUsers() {
            User admin1 = User.builder().email("a1@example.com").role(Role.ADMIN).build();
            User admin2 = User.builder().email("a2@example.com").role(Role.ADMIN).build();
            User customer = User.builder().email("c1@example.com").role(Role.BUYER).build();

            repository.save(admin1);
            repository.save(admin2);
            repository.save(customer);

            List<User> admins = repository.findAllByRole(Role.ADMIN);

            assertEquals(2, admins.size());
            assertTrue(admins.stream().allMatch(u -> Role.ADMIN.equals(u.getRole())));
        }
    }
}