package org.capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private Long id;
    private BigDecimal totalAmount;
    private LocalDateTime updatedAt;

    // Relationships
    private User user;

    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, Integer quantity) {
        // Implementation to add item to cart
    }

    public void removeItem(Long cartItemId) {
        items.removeIf(item -> item.getId().equals(cartItemId));
        calculateTotal();
    }

    public void updateQuantity(Long cartItemId, Integer newQuantity) {
        for (CartItem item : items) {
            if (item.getId().equals(cartItemId)) {
                item.setQuantity(newQuantity);
                break;
            }
        }
        calculateTotal();
    }

    public void clear() {
        items.clear();
        this.totalAmount = BigDecimal.ZERO;
    }

    public BigDecimal calculateTotal() {
        this.totalAmount = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.totalAmount;
    }
}
