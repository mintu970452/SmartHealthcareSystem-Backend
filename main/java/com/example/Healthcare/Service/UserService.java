
package com.example.Healthcare.Service;

import com.example.Healthcare.Entity.User;
import com.example.Healthcare.Entity.Patient;
import com.example.Healthcare.Repository.UserRepository;
import com.example.Healthcare.Repository.PatientRepository;
import com.example.Healthcare.DTO.LoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        if ("PATIENT".equalsIgnoreCase(user.getRole())) {

            Patient patient = new Patient();

            patient.setName(user.getName());
            patient.setEmail(user.getEmail());
            patient.setPhone("");

            patientRepository.save(patient);
        }

        return savedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User loginUser(LoginRequest loginRequest) {

        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {
            return null;
        }

        return user;
    }
}

