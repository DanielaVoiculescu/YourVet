package com.example.yourvet.model;

import java.util.UUID;

public class Appointment {
    private String doctorId;
    private String intervention;
    private WorkDay workDay;
    private String date;
    private String id;
    private String owner_id;
    public Appointment(String doctorId, String intervention, String date,WorkDay workDay,String owner_id) {
        this.doctorId = doctorId;
        this.intervention = intervention;
        this.workDay=workDay;
        this.date = date;
        this.owner_id=owner_id;
        id= UUID.randomUUID().toString();

    }

    public Appointment() {
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getIntervention() {
        return intervention;
    }

    public void setIntervention(String intervention) {
        this.intervention = intervention;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WorkDay getWorkDay() {
        return workDay;
    }

    public void setWorkDay(WorkDay workDay) {
        this.workDay = workDay;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "doctorId='" + doctorId + '\'' +
                ", intervention='" + intervention + '\'' +
                ", workDay=" + workDay +
                ", date='" + date + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
