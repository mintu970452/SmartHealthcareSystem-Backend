package com.example.Healthcare.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Healthcare.Entity.DoctorAvailability;

public interface DoctorAvailabilityRepository
        extends JpaRepository<DoctorAvailability, Long> {
}