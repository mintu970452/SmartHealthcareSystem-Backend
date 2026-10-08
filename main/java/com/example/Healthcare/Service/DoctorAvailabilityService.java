package com.example.Healthcare.Service;

import org.springframework.stereotype.Service;
import com.example.Healthcare.Entity.DoctorAvailability;
import com.example.Healthcare.Repository.DoctorAvailabilityRepository;

import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;

    public DoctorAvailabilityService(
            DoctorAvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    // Add availability
    public DoctorAvailability addAvailability(
            DoctorAvailability availability) {

        return availabilityRepository.save(availability);
    }

    // Get all availability
    public List<DoctorAvailability> getAllAvailability() {

        return availabilityRepository.findAll();
    }

    // Update availability
    public DoctorAvailability updateAvailability(
            Long id,
            DoctorAvailability availability) {

        DoctorAvailability existing =
                availabilityRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setDoctor(availability.getDoctor());
        existing.setAvailableDate(availability.getAvailableDate());
        existing.setStartTime(availability.getStartTime());
        existing.setEndTime(availability.getEndTime());

        return availabilityRepository.save(existing);
    }

    // Delete availability
    public boolean deleteAvailability(Long id) {

        if (!availabilityRepository.existsById(id)) {
            return false;
        }

        availabilityRepository.deleteById(id);
        return true;
    }
}