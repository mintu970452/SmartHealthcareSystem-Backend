package com.example.Healthcare.DTO;

public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private String symptoms;

    public AppointmentResponse(
            Long id,
            Long patientId,
            Long doctorId,
            String doctorName,
            String appointmentDate,
            String appointmentTime,
            String status,
            String symptoms) {

        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.symptoms = symptoms;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public String getSymptoms() {
        return symptoms;
    }
}