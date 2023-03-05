package com.example.yourvet.model;

import java.util.UUID;

public class Intervention {
    private String id;
    private String petId;
    private Date date;
    private String symptom;
    private String diagnostic;
    private String intervention;
    private String prescription;
    private String doctorId;
    public Intervention() {
    }

    public Intervention(String petId, Date date, String symptom, String diagnostic, String intervention, String prescription, String doctorId) {
        this.petId = petId;
        this.date = date;
        this.symptom = symptom;
        this.diagnostic = diagnostic;
        this.intervention = intervention;
        this.prescription = prescription;
        this.id= UUID.randomUUID().toString();
        this.doctorId=doctorId;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getIntervention() {
        return intervention;
    }

    public void setIntervention(String intervention) {
        this.intervention = intervention;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "Intervention{" +
                "id='" + id + '\'' +
                ", petId='" + petId + '\'' +
                ", date=" + date +
                ", symptom='" + symptom + '\'' +
                ", diagnostic='" + diagnostic + '\'' +
                ", intervention='" + intervention + '\'' +
                ", prescription='" + prescription + '\'' +
                ", doctorId='" + doctorId + '\'' +
                '}';
    }
}
