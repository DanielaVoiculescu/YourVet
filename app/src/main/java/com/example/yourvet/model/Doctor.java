package com.example.yourvet.model;

public class Doctor extends User{
    private String id;

    public Doctor(String lastname, String firstname, String username, String email, String password, String phoneNr, String photoUrl, String id) {
        super(lastname, firstname, username, email, password, phoneNr, photoUrl);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
