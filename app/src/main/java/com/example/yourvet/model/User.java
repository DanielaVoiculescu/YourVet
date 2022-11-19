package com.example.yourvet.model;

import androidx.annotation.Keep;

@Keep
public class User {
   private String  lastname;
    private String firstname;
    private String username;
    private String email;
    private String password;
    private String phoneNr;
    private String role;
    private String doctorId;

    public User(String lastname, String firstname, String username, String email, String password, String phoneNr, String doctorId, String role) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNr = phoneNr;
        this.doctorId = doctorId;
        this.role=role;

    }

    public User() {
    }

    public User(String lastname, String firstname, String username, String email, String password, String phoneNr, String role) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNr = phoneNr;
        this.role=role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

}
