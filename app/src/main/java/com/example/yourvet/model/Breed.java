package com.example.yourvet.model;

import java.util.UUID;

public class Breed {

    private String id;
    private String species;
    private String name;

    public Breed( String species, String name) {
        this.id = UUID.randomUUID().toString();
        this.species = species;
        this.name = name;
    }

    public Breed() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
