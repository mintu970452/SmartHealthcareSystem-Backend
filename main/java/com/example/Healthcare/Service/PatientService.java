package com.example.Healthcare.Service;

import org.springframework.stereotype.Service;
import com.example.Healthcare.Entity.Patient;
import com.example.Healthcare.Repository.PatientRepository;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Add patient
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Update patient
    public Patient updatePatient(Long id, Patient patient) {

        Patient existingPatient = patientRepository.findById(id).orElse(null);

        if (existingPatient == null) {
            return null;
        }

        existingPatient.setName(patient.getName());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setPhone(patient.getPhone());

        return patientRepository.save(existingPatient);
    }

    // Delete patient
    public boolean deletePatient(Long id) {

        if (!patientRepository.existsById(id)) {
            return false;
        }

        patientRepository.deleteById(id);
        return true;
    }
}