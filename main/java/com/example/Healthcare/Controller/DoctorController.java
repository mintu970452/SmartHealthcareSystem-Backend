package com.example.Healthcare.Controller;

import com.example.Healthcare.Entity.Doctor;
import com.example.Healthcare.Service.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Get all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // Add doctor
    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    // Update doctor
    @PutMapping("/{id}")
    public Doctor updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {

        return doctorService.updateDoctor(id, doctor);
    }

    // Delete doctor
    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable Long id) {

        boolean deleted = doctorService.deleteDoctor(id);

        if (!deleted) {
            return "Doctor not found";
        }

        return "Doctor deleted successfully";
    }
}