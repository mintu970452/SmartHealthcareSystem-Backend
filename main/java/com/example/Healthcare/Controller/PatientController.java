package com.example.Healthcare.Controller;

import com.example.Healthcare.Entity.Patient;
import com.example.Healthcare.Service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Get all patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // Add patient
    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    // Update patient
    @PutMapping("/{id}")
    public Patient updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {

        return patientService.updatePatient(id, patient);
    }

    // Delete patient
    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {

        boolean deleted = patientService.deletePatient(id);

        if (!deleted) {
            return "Patient not found";
        }

        return "Patient deleted successfully";
    }
}