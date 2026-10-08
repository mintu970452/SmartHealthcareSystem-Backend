package com.example.Healthcare.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Healthcare.Entity.Patient;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);
}