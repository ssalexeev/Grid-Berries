package org.capstone.repository;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.capstone.domain.SellerProfile;
import org.capstone.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerProfileRepositoryTest {

    @Mock
    private Map<Long, SellerProfile> database;

    private SellerProfileRepository repository;

    @BeforeEach
    void setUp() {
        repository = new SellerProfileRepository(database);
    }


    @Nested
    class FindByIdTests {

        @Test
        void findById_WhenIdIsNull_ReturnsEmptyOptional() {
            Optional<SellerProfile> result = repository.findById(null);

            assertTrue(result.isEmpty());
            verifyNoInteractions(database);
        }

        @Test
        void findById_WhenSellerExists_ReturnsOptionalWithSeller() {
            Long sellerId = 1000L;
            SellerProfile mockSeller = new SellerProfile();
            mockSeller.setId(sellerId);

            when(database.get(sellerId)).thenReturn(mockSeller);

            Optional<SellerProfile> result = repository.findById(sellerId);

            assertTrue(result.isPresent());
            assertEquals(sellerId, result.get().getId());
            verify(database, times(1)).get(sellerId);
        }

        @Test
        void findById_WhenSellerDoesNotExist_ReturnsEmptyOptional() {
            Long sellerId = 9999L;
            when(database.get(sellerId)).thenReturn(null);

            Optional<SellerProfile> result = repository.findById(sellerId);

            assertTrue(result.isEmpty());
            verify(database, times(1)).get(sellerId);
        }
    }

    @Nested
    class FindByUserIdTests {

        @Test
        void findByUserId_WhenUserIdIsNull_ReturnsEmptyOptional() {
            Optional<SellerProfile> result = repository.findByUserId(null);

            assertTrue(result.isEmpty());
            verifyNoInteractions(database);
        }

        @Test
        void findByUserId_WhenMatchingUserExists_ReturnsSellerProfile() {
            Long userId = 42L;

            User user = new User();
            user.setId(userId);

            SellerProfile seller = new SellerProfile();
            seller.setId(1000L);
            seller.setUser(user);

            when(database.values()).thenReturn(List.of(seller));

            Optional<SellerProfile> result = repository.findByUserId(userId);

            assertTrue(result.isPresent());
            assertEquals(1000L, result.get().getId());
            assertEquals(userId, result.get().getUser().getId());
            verify(database, times(1)).values();
        }

        @Test
        void findByUserId_WhenUserNotMatchingOrNullUser_ReturnsEmptyOptional() {
            User user = new User();
            user.setId(42L);

            SellerProfile sellerWithUser = new SellerProfile();
            sellerWithUser.setUser(user);

            SellerProfile sellerWithoutUser = new SellerProfile(); // user is null

            when(database.values()).thenReturn(List.of(sellerWithUser, sellerWithoutUser));

            Optional<SellerProfile> result = repository.findByUserId(999L);

            assertTrue(result.isEmpty());
            verify(database, times(1)).values();
        }
    }

    @Nested
    class FindAllTests {

        @Test
        void findAll_ReturnsAllSellersFromDatabase() {
            SellerProfile seller1 = new SellerProfile();
            SellerProfile seller2 = new SellerProfile();

            when(database.values()).thenReturn(List.of(seller1, seller2));

            List<SellerProfile> result = repository.findAll();

            assertEquals(2, result.size());
            verify(database, times(1)).values();
        }
    }

    @Nested
    class SaveAndUpdateTests {

        @Test
        void save_WhenSellerIsNull_ThrowsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> repository.save(null)
            );

            assertEquals("Seller is null", ex.getMessage());
            verifyNoInteractions(database);
        }

        @Test
        void save_WhenNewSellerHasNoId_AssignsGeneratedIdAndSaves() {
            SellerProfile newSeller = new SellerProfile(); // id is null

            SellerProfile savedSeller = repository.save(newSeller);

            assertNotNull(savedSeller.getId());
            assertEquals(1000L, savedSeller.getId()); // Initial sequence value is 1000L
            verify(database, times(1)).put(1000L, newSeller);
            verify(database, never()).containsKey(any());
        }

        @Test
        void save_WhenExistingSellerHasId_DeletesAndSaves() {
            Long existingId = 1050L;
            SellerProfile seller = new SellerProfile();
            seller.setId(existingId);

            when(database.containsKey(existingId)).thenReturn(true);

            SellerProfile updatedSeller = repository.save(seller);

            assertEquals(existingId, updatedSeller.getId());
            verify(database, times(1)).containsKey(existingId);
            verify(database, times(1)).remove(existingId);
            verify(database, times(1)).put(existingId, seller);
        }

        @Test
        void update_WhenSellerOrIdIsNull_ThrowsIllegalArgumentException() {
            SellerProfile sellerWithoutId = new SellerProfile();

            assertThrows(IllegalArgumentException.class, () -> repository.update(null));
            assertThrows(IllegalArgumentException.class, () -> repository.update(sellerWithoutId));

            verifyNoInteractions(database);
        }

        @Test
        void update_WhenValidSeller_DelegatesToSave() {
            Long existingId = 1000L;
            SellerProfile seller = new SellerProfile();
            seller.setId(existingId);

            when(database.containsKey(existingId)).thenReturn(true);

            SellerProfile result = repository.update(seller);

            assertEquals(existingId, result.getId());
            verify(database, times(1)).containsKey(existingId);
            verify(database, times(1)).remove(existingId);
            verify(database, times(1)).put(existingId, seller);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void delete_WhenSellerIsNull_ThrowsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> repository.delete(null)
            );

            assertEquals("SellerProfile is null", ex.getMessage());
        }

        @Test
        void delete_WhenSellerIdIsNull_ThrowsIllegalArgumentException() {
            SellerProfile seller = new SellerProfile(); // id is null

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> repository.delete(seller)
            );

            assertEquals("Id of SellerProfile is null", ex.getMessage());
        }

        @Test
        void delete_WhenIdNotInDatabase_ThrowsRuntimeException() {
            Long sellerId = 2000L;
            SellerProfile seller = new SellerProfile();
            seller.setId(sellerId);

            when(database.containsKey(sellerId)).thenReturn(false);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> repository.delete(seller)
            );

            assertEquals("Seller profile not found", ex.getMessage());
            verify(database, times(1)).containsKey(sellerId);
            verify(database, never()).remove(any());
        }

        @Test
        void delete_WhenSellerExists_RemovesFromDatabase() {
            Long sellerId = 1000L;
            SellerProfile seller = new SellerProfile();
            seller.setId(sellerId);

            when(database.containsKey(sellerId)).thenReturn(true);

            assertDoesNotThrow(() -> repository.delete(seller));

            verify(database, times(1)).containsKey(sellerId);
            verify(database, times(1)).remove(sellerId);
        }
    }
}
