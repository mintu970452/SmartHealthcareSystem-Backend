package com.example.Healthcare.Service;

import org.springframework.stereotype.Service;
import com.example.Healthcare.Entity.Doctor;
import com.example.Healthcare.Repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Add doctor
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Update doctor
    public Doctor updateDoctor(Long id, Doctor doctor) {

        Doctor existingDoctor = doctorRepository.findById(id).orElse(null);

        if (existingDoctor == null) {
            return null;
        }

        existingDoctor.setName(doctor.getName());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setPhone(doctor.getPhone());
        existingDoctor.setSpecialization(doctor.getSpecialization());

        return doctorRepository.save(existingDoctor);
    }

    // Delete doctor
    public boolean deleteDoctor(Long id) {

        if (!doctorRepository.existsById(id)) {
            return false;
        }

        doctorRepository.deleteById(id);
        return true;
    }
}