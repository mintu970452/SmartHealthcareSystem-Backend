package com.example.Healthcare.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Healthcare.Entity.Consultation;
import com.example.Healthcare.Entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepository
        extends JpaRepository<Consultation, Long> {

    Optional<Consultation> findByAppointment(Appointment appointment);

    List<Consultation> findByAppointmentPatientEmail(String email);
}