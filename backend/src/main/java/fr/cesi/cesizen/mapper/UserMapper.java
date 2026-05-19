package fr.cesi.cesizen.mapper;

import fr.cesi.cesizen.domain.user.User;
import fr.cesi.cesizen.dto.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.isActive(),
                user.isRgpdConsent(),
                user.getCreatedAt()
        );
    }
}
