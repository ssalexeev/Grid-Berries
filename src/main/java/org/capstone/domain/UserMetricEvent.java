package org.capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.domain.enums.MetricEventType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMetricEvent {
    private Long id;
    private MetricEventType eventType;
    private String searchQuery;
    private LocalDateTime timestamp;

    // Optional Associations (based on context of interaction event)
    private User user;
    private Product product;
    private OrderItem orderItem;
}
