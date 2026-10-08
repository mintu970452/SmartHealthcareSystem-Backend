
package com.example.Healthcare.Service;

import org.springframework.stereotype.Service;

import com.example.Healthcare.Entity.Appointment;
import com.example.Healthcare.Entity.Consultation;
import com.example.Healthcare.Repository.AppointmentRepository;
import com.example.Healthcare.Repository.ConsultationRepository;

import java.util.List;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;

    public ConsultationService(
            ConsultationRepository consultationRepository,
            AppointmentRepository appointmentRepository) {

        this.consultationRepository = consultationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Consultation addConsultation(
            Consultation consultation,
            String doctorEmail) {

        if (consultation.getAppointment() == null ||
                consultation.getAppointment().getId() == null) {
            return null;
        }

        Appointment appointment =
                appointmentRepository.findById(
                        consultation.getAppointment().getId()
                ).orElse(null);

        if (appointment == null) {
            return null;
        }

        if (appointment.getDoctor() == null) {
            return null;
        }

        if (!appointment.getDoctor().getEmail().equals(doctorEmail)) {
            return null;
        }

        Consultation existing =
                consultationRepository
                        .findByAppointment(appointment)
                        .orElse(null);

        if (existing != null) {
            existing.setDiagnosis(consultation.getDiagnosis());
            existing.setPrescription(consultation.getPrescription());
            existing.setNotes(consultation.getNotes());
            existing.setDecision(consultation.getDecision());
            existing.setSpecialist(consultation.getSpecialist());

            return consultationRepository.save(existing);
        }

        consultation.setAppointment(appointment);

        return consultationRepository.save(consultation);
    }

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public List<Consultation> getConsultationsByPatient(
            String patientEmail) {

        return consultationRepository
                .findByAppointmentPatientEmail(patientEmail);
    }

    public List<Consultation> getConsultationsByDoctor(
            String doctorEmail) {

        return consultationRepository.findAll().stream()
                .filter(consultation -> consultation.getAppointment() != null
                        && consultation.getAppointment().getDoctor() != null
                        && doctorEmail.equals(
                                consultation.getAppointment().getDoctor().getEmail()))
                .toList();
    }

    public Consultation updateConsultation(
            Long id,
            Consultation consultation,
            String doctorEmail) {

        Consultation existing =
                consultationRepository.findById(id).orElse(null);

        if (existing == null ||
                existing.getAppointment() == null ||
                existing.getAppointment().getDoctor() == null) {
            return null;
        }

        if (!existing.getAppointment()
                .getDoctor()
                .getEmail()
                .equals(doctorEmail)) {
            return null;
        }

        existing.setDiagnosis(consultation.getDiagnosis());
        existing.setPrescription(consultation.getPrescription());
        existing.setNotes(consultation.getNotes());
        existing.setDecision(consultation.getDecision());
        existing.setSpecialist(consultation.getSpecialist());

        return consultationRepository.save(existing);
    }

    public boolean deleteConsultation(
            Long id,
            String doctorEmail) {

        Consultation consultation =
                consultationRepository.findById(id).orElse(null);

        if (consultation == null ||
                consultation.getAppointment() == null ||
                consultation.getAppointment().getDoctor() == null) {
            return false;
        }

        if (!consultation.getAppointment()
                .getDoctor()
                .getEmail()
                .equals(doctorEmail)) {
            return false;
        }

        consultationRepository.delete(consultation);

        return true;
    }
}

