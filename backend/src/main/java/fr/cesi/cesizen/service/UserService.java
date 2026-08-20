package fr.cesi.cesizen.service;

import fr.cesi.cesizen.domain.user.User;
import fr.cesi.cesizen.domain.user.UserRepository;
import fr.cesi.cesizen.dto.user.UpdateProfileRequest;
import fr.cesi.cesizen.dto.user.UserResponse;
import fr.cesi.cesizen.exception.ResourceNotFoundException;
import fr.cesi.cesizen.mapper.UserMapper;
import fr.cesi.cesizen.security.SecurityUtils;
import fr.cesi.cesizen.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getCurrentProfile() {
        return userMapper.toResponse(getCurrentEntity());
    }

    @Transactional
    public UserResponse updateCurrentProfile(UpdateProfileRequest request) {
        User user = getCurrentEntity();
        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteCurrentAccount() {
        User user = getCurrentEntity();
        user.setActive(false);
        userRepository.save(user);
    }

    public List<UserResponse> findAllForAdmin() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    private User getCurrentEntity() {
        UserPrincipal principal = SecurityUtils.getCurrentUser();
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }
}
