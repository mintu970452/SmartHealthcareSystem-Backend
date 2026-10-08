package com.example.Healthcare.Controller;

import com.example.Healthcare.Entity.DoctorAvailability;
import com.example.Healthcare.Service.DoctorAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;

    public DoctorAvailabilityController(
            DoctorAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // Get all availability
    @GetMapping
    public List<DoctorAvailability> getAllAvailability() {
        return availabilityService.getAllAvailability();
    }

    // Add availability
    @PostMapping
    public DoctorAvailability addAvailability(
            @RequestBody DoctorAvailability availability) {

        return availabilityService.addAvailability(availability);
    }

    // Update availability
    @PutMapping("/{id}")
    public DoctorAvailability updateAvailability(
            @PathVariable Long id,
            @RequestBody DoctorAvailability availability) {

        return availabilityService.updateAvailability(id, availability);
    }

    // Delete availability
    @DeleteMapping("/{id}")
    public String deleteAvailability(@PathVariable Long id) {

        boolean deleted = availabilityService.deleteAvailability(id);

        if (!deleted) {
            return "Availability not found";
        }

        return "Availability deleted successfully";
    }
}