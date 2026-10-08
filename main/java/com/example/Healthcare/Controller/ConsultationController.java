
package com.example.Healthcare.Controller;

import com.example.Healthcare.Entity.Consultation;
import com.example.Healthcare.Service.ConsultationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(
            ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping("/patient")
    public List<Consultation> getPatientConsultations(
            Principal principal) {

        return consultationService
                .getConsultationsByPatient(
                        principal.getName()
                );
    }

    @GetMapping("/doctor")
    public List<Consultation> getDoctorConsultations(
            Principal principal) {

        return consultationService
                .getConsultationsByDoctor(
                        principal.getName()
                );
    }

    @PostMapping
    public ResponseEntity<?> addConsultation(
            @RequestBody Consultation consultation,
            Principal principal) {

        Consultation saved =
                consultationService.addConsultation(
                        consultation,
                        principal.getName()
                );

        if (saved == null) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to add this consultation.");
        }

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConsultation(
            @PathVariable Long id,
            @RequestBody Consultation consultation,
            Principal principal) {

        Consultation updated =
                consultationService.updateConsultation(
                        id,
                        consultation,
                        principal.getName()
                );

        if (updated == null) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to update this consultation.");
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConsultation(
            @PathVariable Long id,
            Principal principal) {

        boolean deleted =
                consultationService.deleteConsultation(
                        id,
                        principal.getName()
                );

        if (!deleted) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to delete this consultation.");
        }

        return ResponseEntity.ok(
                "Consultation deleted successfully"
        );
    }
}

