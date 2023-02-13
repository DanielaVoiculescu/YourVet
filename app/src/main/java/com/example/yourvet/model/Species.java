package com.example.yourvet.model;

import java.util.UUID;

public class Species {

    private String id;
    private String name;

    public Species() {
    }

    public Species(String name) {
        this.id= UUID.randomUUID().toString();
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
