package com.example.yourvet.model;

public class Request {

    private String lastname;
    private String firstname;
    private String doctorID;
    private String userId;
    public Request() {
    }

    public Request(String lastname, String firstname, String doctorID, String userId) {

        this.lastname = lastname;
        this.firstname = firstname;
        this.doctorID = doctorID;
        this.userId=userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
