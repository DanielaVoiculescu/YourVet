package com.example.yourvet.model;

import java.util.ArrayList;

public class Patient extends User{
    private ArrayList<String> pets;

    public Patient(String lastname, String firstname, String username, String email, String password, String phoneNr, String photoUrl,String id, ArrayList<String> pets) {
        super(lastname, firstname, username, email, password, phoneNr, photoUrl,id);
        this.pets = pets;
    }

    public Patient(String lastname, String firstname, String username, String email, String password, String phoneNr, String photoUrl,String id) {
        super(lastname, firstname, username, email, password, phoneNr, photoUrl,id);
        this.pets=null;
    }

    public ArrayList<String> getPets() {
        return pets;
    }

    public void setPets(ArrayList<String> pets) {
        this.pets = pets;
    }

}
