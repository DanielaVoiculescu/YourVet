package com.example.yourvet.model;

public class InterventionType {
    private String name;
    private int time;

    public InterventionType(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public InterventionType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
