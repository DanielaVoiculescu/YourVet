package com.example.yourvet.model;

import java.util.ArrayList;

public class Patient extends User{
    private ArrayList<Pet> pets;

    public Patient(String lastname, String firstname, String username, String email, String password, String phoneNr, String photoUrl, ArrayList<Pet> pets) {
        super(lastname, firstname, username, email, password, phoneNr, photoUrl);
        this.pets = pets;
    }

    public Patient(String lastname, String firstname, String username, String email, String password, String phoneNr, String photoUrl) {
        super(lastname, firstname, username, email, password, phoneNr, photoUrl);
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }

    public void setPets(ArrayList<Pet> pets) {
        this.pets = pets;
    }

}
