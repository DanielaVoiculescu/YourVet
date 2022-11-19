package com.example.yourvet.model;

public class Request {
    private String username;
    private String lastname;
    private String firstname;
    private String doctorID;

    public Request() {
    }

    public Request(String username, String lastname, String firstname, String doctorID) {
        this.username = username;
        this.lastname = lastname;
        this.firstname = firstname;
        this.doctorID = doctorID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
}
