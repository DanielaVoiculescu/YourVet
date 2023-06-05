package com.example.yourvet.model;

public class Doctor extends User{
    private String DoctorID;
    private String specialization;
    private String description;
    public Doctor(String lastname, String firstname,String email, String password, String phoneNr, String photoUrl, String id,String DoctorID) {
        super(lastname, firstname, email, password, phoneNr, photoUrl,id);
        this.DoctorID= DoctorID;
    }

    public Doctor(String lastname, String firstname,  String email, String password, String phoneNr, String photoUrl, String id, String doctorID, String specialization, String description) {
        super(lastname, firstname,  email, password, phoneNr, photoUrl, id);
        DoctorID = doctorID;
        this.specialization = specialization;
        this.description = description;
    }

    public Doctor(String lastname, String firstname, String email, String password, String phoneNr, String photoUrl, String id, String specialization, String description) {
        super(lastname, firstname, email, password, phoneNr, photoUrl, id);
        this.specialization = specialization;
        this.description = description;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Doctor(){
    }
    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "DoctorID='" + DoctorID + '\'' +
                ", specialization='" + specialization + '\'' +
                ", description='" + description + '\'' +
                ", name='" + super.getFirstname() + '\'' +
                ", id='" + super.getId() + '\'' +
                '}';
    }

}
