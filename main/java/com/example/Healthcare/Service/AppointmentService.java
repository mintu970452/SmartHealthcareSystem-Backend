package com.example.Healthcare.Service;

import org.springframework.stereotype.Service;

import com.example.Healthcare.Entity.Appointment;
import com.example.Healthcare.Entity.Patient;
import com.example.Healthcare.Entity.Doctor;
import com.example.Healthcare.Repository.AppointmentRepository;
import com.example.Healthcare.Repository.PatientRepository;
import com.example.Healthcare.Repository.DoctorRepository;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment addAppointment(
            Appointment appointment,
            String email) {

        Patient patient =
                patientRepository.findByEmail(email).orElse(null);

        if (patient == null) {
            return null;
        }

        if (appointment.getDoctor() == null ||
                appointment.getDoctor().getId() == null) {
            return null;
        }

        Long doctorId = appointment.getDoctor().getId();

        Doctor doctor =
                doctorRepository.findById(doctorId).orElse(null);

        if (doctor == null) {
            return null;
        }

        if (!isValidDate(appointment.getAppointmentDate())) {
            return null;
        }

        if (!isValidTime(appointment.getAppointmentTime())) {
            return null;
        }

        if (!isWithinDoctorShift(
                appointment.getAppointmentTime(),
                doctor.getShiftStart(),
                doctor.getShiftEnd())) {
            return null;
        }

        if (!isThirtyMinuteSlot(
                appointment.getAppointmentTime())) {
            return null;
        }

        boolean alreadyBooked =
                appointmentRepository.isSlotBooked(
                        doctorId,
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime()
                );

        if (alreadyBooked) {
            return null;
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        if (appointment.getStatus() == null ||
                appointment.getStatus().isBlank()) {
            appointment.setStatus("Booked");
        }

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(String email) {
        return appointmentRepository.findByPatientEmail(email);
    }

    public List<Appointment> getAppointmentsByDoctor(String email) {
        return appointmentRepository.findByDoctorEmail(email);
    }

    public Appointment updateAppointment(
            Long id,
            Appointment appointment) {

        Appointment existingAppointment =
                appointmentRepository.findById(id).orElse(null);

        if (existingAppointment == null) {
            return null;
        }

        existingAppointment.setPatient(appointment.getPatient());
        existingAppointment.setDoctor(appointment.getDoctor());
        existingAppointment.setAppointmentDate(
                appointment.getAppointmentDate());
        existingAppointment.setAppointmentTime(
                appointment.getAppointmentTime());
        existingAppointment.setStatus(appointment.getStatus());

        return appointmentRepository.save(existingAppointment);
    }

    public Appointment updatePatientAppointment(
            Long id,
            Appointment appointment,
            String patientEmail) {

        Appointment existingAppointment =
                appointmentRepository.findById(id).orElse(null);

        if (existingAppointment == null) {
            return null;
        }

        if (existingAppointment.getPatient() == null) {
            return null;
        }

        if (!existingAppointment.getPatient()
                .getEmail()
                .equals(patientEmail)) {
            return null;
        }

        if (appointment.getDoctor() == null ||
                appointment.getDoctor().getId() == null) {
            return null;
        }

        Doctor doctor =
                doctorRepository.findById(
                        appointment.getDoctor().getId()
                ).orElse(null);

        if (doctor == null) {
            return null;
        }

        if (!isValidDate(appointment.getAppointmentDate())) {
            return null;
        }

        if (!isValidTime(appointment.getAppointmentTime())) {
            return null;
        }

        if (!isWithinDoctorShift(
                appointment.getAppointmentTime(),
                doctor.getShiftStart(),
                doctor.getShiftEnd())) {
            return null;
        }

        if (!isThirtyMinuteSlot(
                appointment.getAppointmentTime())) {
            return null;
        }

        boolean alreadyBooked =
                appointmentRepository.isSlotBooked(
                        doctor.getId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime()
                );

        if (alreadyBooked &&
                !(existingAppointment.getDoctor().getId()
                        .equals(doctor.getId())
                        && existingAppointment
                                .getAppointmentDate()
                                .equals(appointment.getAppointmentDate())
                        && existingAppointment
                                .getAppointmentTime()
                                .equals(appointment.getAppointmentTime()))) {
            return null;
        }

        existingAppointment.setDoctor(doctor);

        existingAppointment.setAppointmentDate(
                appointment.getAppointmentDate());

        existingAppointment.setAppointmentTime(
                appointment.getAppointmentTime());

        return appointmentRepository.save(existingAppointment);
    }

    public boolean cancelAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id).orElse(null);

        if (appointment == null) {
            return false;
        }

        appointment.setStatus("Cancelled");
        appointmentRepository.save(appointment);

        return true;
    }

    public boolean cancelPatientAppointment(
            Long id,
            String patientEmail) {

        Appointment appointment =
                appointmentRepository.findById(id).orElse(null);

        if (appointment == null) {
            return false;
        }

        if (appointment.getPatient() == null) {
            return false;
        }

        if (!appointment.getPatient()
                .getEmail()
                .equals(patientEmail)) {
            return false;
        }

        appointment.setStatus("Cancelled");
        appointmentRepository.save(appointment);

        return true;
    }

    public boolean updateStatus(Long id, String status) {

        Appointment appointment =
                appointmentRepository.findById(id).orElse(null);

        if (appointment == null) {
            return false;
        }

        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        return true;
    }

    public boolean updateDoctorStatus(
            Long id,
            String status,
            String doctorEmail) {

        Appointment appointment =
                appointmentRepository.findById(id).orElse(null);

        if (appointment == null) {
            return false;
        }

        if (appointment.getDoctor() == null) {
            return false;
        }

        if (!appointment.getDoctor()
                .getEmail()
                .equals(doctorEmail)) {
            return false;
        }

        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        return true;
    }

    private boolean isValidDate(String date) {

        if (date == null || date.isBlank()) {
            return false;
        }

        try {
            java.time.LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {

        if (time == null || time.isBlank()) {
            return false;
        }

        try {
            java.time.LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isThirtyMinuteSlot(String time) {

        java.time.LocalTime appointmentTime =
                java.time.LocalTime.parse(time);

        return appointmentTime.getMinute() == 0 ||
                appointmentTime.getMinute() == 30;
    }

    private boolean isWithinDoctorShift(
            String appointmentTime,
            String shiftStart,
            String shiftEnd) {

        if (shiftStart == null ||
                shiftEnd == null ||
                shiftStart.isBlank() ||
                shiftEnd.isBlank()) {
            return false;
        }

        java.time.LocalTime appointment =
                java.time.LocalTime.parse(appointmentTime);

        java.time.LocalTime start =
                java.time.LocalTime.parse(shiftStart);

        java.time.LocalTime end =
                java.time.LocalTime.parse(shiftEnd);

        return !appointment.isBefore(start)
                && appointment.isBefore(end);
    }
}