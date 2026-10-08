package com.example.Healthcare.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Healthcare.Entity.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientEmail(String email);

    List<Appointment> findByDoctorEmail(String email);

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
        AND a.appointmentDate = :appointmentDate
        AND a.appointmentTime = :appointmentTime
        AND a.status IN ('Booked', 'Confirmed')
    """)
    boolean isSlotBooked(
            @Param("doctorId") Long doctorId,
            @Param("appointmentDate") String appointmentDate,
            @Param("appointmentTime") String appointmentTime
    );
}