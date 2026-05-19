package fr.cesi.cesizen.controller;

import fr.cesi.cesizen.dto.user.UserResponse;
import fr.cesi.cesizen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> listUsers() {
        return userService.findAllForAdmin();
    }
}
