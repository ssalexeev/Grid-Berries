package org.capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.domain.enums.Role;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private Role role;
    private Boolean isActive;
    private LocalDateTime createdAt;


    public void register() {
        // Implementation logic for registration
    }

    public boolean authenticate() {
        // Implementation logic for authentication
        return false;
    }
}
