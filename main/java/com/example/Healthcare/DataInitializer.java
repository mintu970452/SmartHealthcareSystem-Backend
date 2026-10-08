package com.example.Healthcare;

import com.example.Healthcare.Entity.Doctor;
import com.example.Healthcare.Entity.User;
import com.example.Healthcare.Repository.DoctorRepository;
import com.example.Healthcare.Repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeDoctors(
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            User doctor1 = userRepository
                    .findByEmail("suresh1@gmail.com")
                    .orElse(null);

            if (doctor1 != null && "DOCTOR".equals(doctor1.getRole())) {
                doctor1.setEmail("doctor1@healthcare.com");
                userRepository.save(doctor1);
            }

            Doctor doctor1Record = doctorRepository
                    .findAll()
                    .stream()
                    .filter(doctor ->
                            "suresh1@gmail.com".equals(doctor.getEmail())
                            || "doctor1@healthcare.com".equals(doctor.getEmail()))
                    .findFirst()
                    .orElse(null);

            if (doctor1Record != null) {
                doctor1Record.setEmail("doctor1@healthcare.com");
                doctor1Record.setName("Doctor 1");
                doctor1Record.setSpecialization("General Medicine");
                doctor1Record.setShiftStart("09:00");
                doctor1Record.setShiftEnd("13:00");
                doctorRepository.save(doctor1Record);
            }

            if (userRepository
                    .findByEmail("doctor2@healthcare.com")
                    .isEmpty()) {

                User user = new User();
                user.setName("Doctor 2");
                user.setEmail("doctor2@healthcare.com");
                user.setPassword(
                        passwordEncoder.encode("Doctor@123"));
                user.setRole("DOCTOR");

                userRepository.save(user);

                Doctor doctor = new Doctor();
                doctor.setName("Doctor 2");
                doctor.setEmail("doctor2@healthcare.com");
                doctor.setPhone("9000000002");
                doctor.setSpecialization("General Medicine");
                doctor.setShiftStart("13:00");
                doctor.setShiftEnd("17:00");

                doctorRepository.save(doctor);
            }

            if (userRepository
                    .findByEmail("doctor3@healthcare.com")
                    .isEmpty()) {

                User user = new User();
                user.setName("Doctor 3");
                user.setEmail("doctor3@healthcare.com");
                user.setPassword(
                        passwordEncoder.encode("Doctor@123"));
                user.setRole("DOCTOR");

                userRepository.save(user);

                Doctor doctor = new Doctor();
                doctor.setName("Doctor 3");
                doctor.setEmail("doctor3@healthcare.com");
                doctor.setPhone("9000000003");
                doctor.setSpecialization("General Medicine");
                doctor.setShiftStart("17:00");
                doctor.setShiftEnd("21:00");

                doctorRepository.save(doctor);
            }
        };
    }
}