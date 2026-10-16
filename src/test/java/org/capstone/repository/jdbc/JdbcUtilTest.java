package org.capstone.repository.jdbc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUtilTest {

    // MODE=PostgreSQL enables compatibility for Postgre-style SQL syntax in H2
    private static final String H2_URL = "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

    private JdbcUtil jdbcUtil;

    // Record mapping the users table schema
    public record User(
            int id,
            String email,
            String passwordHash,
            String role,
            boolean isActive,
            Timestamp createdAt
    ) {}

    @BeforeAll
    static void setupDatabase() {
        JdbcUtil.setCredentials(H2_URL, H2_USER, H2_PASSWORD);
    }

    @BeforeEach
    void setUp() {
        jdbcUtil = new JdbcUtil();

        jdbcUtil.execute("DELETE FROM users");

        // Reset and re-seed user data before each test
        jdbcUtil.execute("""
            INSERT INTO users (id, email, password_hash, role, is_active) VALUES 
            (1, 'john.doe@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7a', 'buyer', true),
            (2, 'jane.smith@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7b', 'seller', true),
            (3, 'tech.vendor@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7c', 'seller', true),
            (4, 'admin@marketplace.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7d', 'admin', true),
            (5, 'inactive.user@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7e', 'buyer', false);
        """);
    }

    // Helper ResultSet mapper
    private User mapUser(ResultSet rs) {
        try {
            return new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("role"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to map User record", e);
        }
    }

    @Nested
    @DisplayName("findOne() Tests")
    class FindOneTests {

        @Test
        @DisplayName("Positive: Find single active user by email")
        void findOne_Success() {
            Optional<User> user = jdbcUtil.findOne(
                    "SELECT * FROM users WHERE email = ?",
                    JdbcUtilTest.this::mapUser,
                    "admin@marketplace.com"
            );

            assertTrue(user.isPresent());
            assertEquals(4, user.get().id());
            assertEquals("admin", user.get().role());
            assertTrue(user.get().isActive());
        }

        @Test
        @DisplayName("Positive: Should return Optional.empty when email does not exist")
        void findOne_NotFound() {
            Optional<User> user = jdbcUtil.findOne(
                    "SELECT * FROM users WHERE email = ?",
                    JdbcUtilTest.this::mapUser,
                    "nonexistent@example.com"
            );

            assertTrue(user.isEmpty());
        }

        @Test
        @DisplayName("Negative: Throw IllegalStateException cause when query matches multiple rows")
        void findOne_MultipleResults_ThrowsException() {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                    jdbcUtil.findOne("SELECT * FROM users WHERE role = ?", JdbcUtilTest.this::mapUser, "seller")
            );

            assertTrue(exception.getMessage().contains("Query returned more than 1 result"));
        }
    }

    @Nested
    @DisplayName("findMany() Tests")
    class FindManyTests {

        @Test
        @DisplayName("Positive: Return all active buyers")
        void findMany_Filtered() {
            List<User> buyers = jdbcUtil.findMany(
                    "SELECT * FROM users WHERE role = ? AND is_active = ?",
                    JdbcUtilTest.this::mapUser,
                    "buyer",
                    true
            );

            assertEquals(1, buyers.size());
            assertEquals("john.doe@example.com", buyers.get(0).email());
        }

        @Test
        @DisplayName("Positive: Fetch all records ordered by ID")
        void findMany_AllUsers() {
            List<User> users = jdbcUtil.findMany(
                    "SELECT * FROM users ORDER BY id ASC",
                    JdbcUtilTest.this::mapUser
            );

            assertEquals(5, users.size());
            assertEquals("john.doe@example.com", users.get(0).email());
            assertFalse(users.get(4).isActive()); // inactive.user@example.com
        }

        @Test
        @DisplayName("Positive: Return empty list when condition matches no rows")
        void findMany_NoMatches() {
            List<User> superusers = jdbcUtil.findMany(
                    "SELECT * FROM users WHERE role = ?",
                    JdbcUtilTest.this::mapUser,
                    "superuser"
            );

            assertNotNull(superusers);
            assertTrue(superusers.isEmpty());
        }
    }

    @Nested
    @DisplayName("execute() Updates & Writes")
    class ExecuteTests {

        @Test
        @DisplayName("Positive: Insert a new user record")
        void execute_InsertUser() {
            jdbcUtil.execute(
                    "INSERT INTO users (email, password_hash, role, is_active) VALUES (?, ?, ?, ?)",
                    "new.buyer@example.com", "$2a$12$hash", "buyer", true
            );

            Optional<User> user = jdbcUtil.findOne(
                    "SELECT * FROM users WHERE email = ?",
                    JdbcUtilTest.this::mapUser,
                    "new.buyer@example.com"
            );

            assertTrue(user.isPresent());
            assertEquals("buyer", user.get().role());
        }

        @Test
        @DisplayName("Positive: Deactivate user by updating is_active flag")
        void execute_UpdateUser() {
            jdbcUtil.execute("UPDATE users SET is_active = ? WHERE email = ?", false, "john.doe@example.com");

            Optional<User> user = jdbcUtil.findOne(
                    "SELECT * FROM users WHERE email = ?",
                    JdbcUtilTest.this::mapUser,
                    "john.doe@example.com"
            );

            assertTrue(user.isPresent());
            assertFalse(user.get().isActive());
        }

        @Test
        @DisplayName("Positive: Delete inactive user account")
        void execute_DeleteUser() {
            jdbcUtil.execute("DELETE FROM users WHERE is_active = ?", false);

            List<User> remaining = jdbcUtil.findMany("SELECT * FROM users", JdbcUtilTest.this::mapUser);
            assertEquals(4, remaining.size());
        }

        @Test
        @DisplayName("Negative: Reject duplicate email insertion due to UNIQUE constraint")
        void execute_DuplicateEmail_ThrowsException() {
            assertThrows(RuntimeException.class, () ->
                    jdbcUtil.execute(
                            "INSERT INTO users (email, password_hash, role) VALUES (?, ?, ?)",
                            "john.doe@example.com", "$2a$12$hash", "buyer"
                    )
            );
        }
    }

    @Nested
    @DisplayName("execute() with Consumer PreparedStatement")
    class ConsumerExecuteTests {

        @Test
        @DisplayName("Positive: Insert user using Consumer parameter setting")
        void execute_ConsumerBinding() {
            jdbcUtil.execute(
                    "INSERT INTO users (email, password_hash, role, is_active) VALUES (?, ?, ?, ?)",
                    stmt -> {
                        try {
                            stmt.setString(1, "consumer.user@example.com");
                            stmt.setString(2, "$2a$12$hashconsumer");
                            stmt.setString(3, "seller");
                            stmt.setBoolean(4, true);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            Optional<User> user = jdbcUtil.findOne(
                    "SELECT * FROM users WHERE email = ?",
                    JdbcUtilTest.this::mapUser,
                    "consumer.user@example.com"
            );

            assertTrue(user.isPresent());
            assertEquals("seller", user.get().role());
        }
    }
}