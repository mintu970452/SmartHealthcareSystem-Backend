
package com.example.Healthcare.Controller;

import com.example.Healthcare.Entity.User;
import com.example.Healthcare.Service.UserService;
import com.example.Healthcare.Service.JwtService;
import com.example.Healthcare.DTO.LoginRequest;
import com.example.Healthcare.DTO.UserResponse;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {

        user.setRole("PATIENT");

        User savedUser = userService.registerUser(user);

        if (savedUser == null) {
            return "Email already registered";
        }

        return "User registered successfully";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {

        User user = userService.loginUser(loginRequest);

        if (user == null) {
            return "Invalid email or password";
        }

        return jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );
    }

    @GetMapping("/me")
    public UserResponse getMyProfile(Principal principal) {

        User user =
                userService.getUserByEmail(principal.getName());

        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .collect(Collectors.toList());
    }
}

