package com.example.billbuddy.model;

public class Frequency {
    private int id;
    private String frequency;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Frequency() {
    }

    public Frequency(int id, String frequency) {
        this.id = id;
        this.frequency = frequency;
    }
}
