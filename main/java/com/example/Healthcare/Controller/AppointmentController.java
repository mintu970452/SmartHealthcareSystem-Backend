package com.example.Healthcare.Controller;

import com.example.Healthcare.Entity.Appointment;
import com.example.Healthcare.Service.AppointmentService;
import com.example.Healthcare.DTO.AppointmentResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<AppointmentResponse> getMyAppointments(
            Principal principal) {

        String email = principal.getName();

        return appointmentService.getAppointmentsByPatient(email)
                .stream()
                .filter(appointment ->
                        appointment.getPatient() != null &&
                        appointment.getDoctor() != null
                )
                .map(appointment -> new AppointmentResponse(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getName(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus(),
                        appointment.getSymptoms()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/doctor")
    public List<AppointmentResponse> getDoctorAppointments(
            Principal principal) {

        String email = principal.getName();

        return appointmentService.getAppointmentsByDoctor(email)
                .stream()
                .filter(appointment ->
                        appointment.getPatient() != null &&
                        appointment.getDoctor() != null
                )
                .map(appointment -> new AppointmentResponse(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getName(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus(),
                        appointment.getSymptoms()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> addAppointment(
            @RequestBody Appointment appointment,
            Principal principal) {

        Appointment savedAppointment =
                appointmentService.addAppointment(
                        appointment,
                        principal.getName()
                );

        if (savedAppointment == null) {
            return ResponseEntity
                    .badRequest()
                    .body("This appointment slot is already booked.");
        }

        return ResponseEntity.ok(savedAppointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointment,
            Principal principal) {

        Appointment updatedAppointment =
                appointmentService.updatePatientAppointment(
                        id,
                        appointment,
                        principal.getName()
                );

        if (updatedAppointment == null) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to update this appointment.");
        }

        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelAppointment(
            @PathVariable Long id,
            Principal principal) {

        boolean cancelled =
                appointmentService.cancelPatientAppointment(
                        id,
                        principal.getName()
                );

        if (!cancelled) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to cancel this appointment.");
        }

        return ResponseEntity.ok(
                "Appointment cancelled successfully"
        );
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<String> confirmAppointment(
            @PathVariable Long id,
            Principal principal) {

        boolean updated =
                appointmentService.updateDoctorStatus(
                        id,
                        "Confirmed",
                        principal.getName()
                );

        if (!updated) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to update this appointment.");
        }

        return ResponseEntity.ok(
                "Appointment confirmed successfully"
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointmentByDoctor(
            @PathVariable Long id,
            Principal principal) {

        boolean updated =
                appointmentService.updateDoctorStatus(
                        id,
                        "Cancelled",
                        principal.getName()
                );

        if (!updated) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to update this appointment.");
        }

        return ResponseEntity.ok(
                "Appointment cancelled successfully"
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeAppointment(
            @PathVariable Long id,
            Principal principal) {

        boolean updated =
                appointmentService.updateDoctorStatus(
                        id,
                        "Completed",
                        principal.getName()
                );

        if (!updated) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to update this appointment.");
        }

        return ResponseEntity.ok(
                "Appointment completed successfully"
        );
    }
}