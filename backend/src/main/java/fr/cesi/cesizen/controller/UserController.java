package fr.cesi.cesizen.controller;

import fr.cesi.cesizen.dto.user.UpdateProfileRequest;
import fr.cesi.cesizen.dto.user.UserResponse;
import fr.cesi.cesizen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserResponse getProfile() {
        return userService.getCurrentProfile();
    }

    @PutMapping
    public UserResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateCurrentProfile(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount() {
        userService.deleteCurrentAccount();
    }
}
