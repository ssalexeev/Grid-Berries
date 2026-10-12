package org.capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerProfile {
    private Long id;
    private String storeName;
    private String description;
    private String contactInfo;

    // Association: Belongs to 1 User
    private User user;

    public void updateProfile() {
        // Implementation logic for updating profile
    }
}
