package com.example.yourvet.model;


import androidx.annotation.Keep;

@Keep
public class Pet {
    private String id;
    private String name;
    private String ownerId;
    private String breed;
    private String species;
    private Date birthdate;
    private String photoUrl;
    private String sex;

    public Pet() {
    }

    public Pet(String id, String name, String ownerId, String breed, String species, Date birthdate, String photoUrl, String sex) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.breed = breed;
        this.species = species;
        this.birthdate = birthdate;
        this.photoUrl = photoUrl;
        this.sex = sex;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
