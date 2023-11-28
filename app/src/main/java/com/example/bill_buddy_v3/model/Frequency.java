package com.example.bill_buddy_v3.model;

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

    @Override
    public String toString() {
        return frequency;
    }
}
