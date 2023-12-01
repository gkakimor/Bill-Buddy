package com.example.bill_buddy_v3.model;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class Bill {
    private int id;
    private Type type;
    private double amount;
    private String payee;
    private Date due_date;
    private Frequency frequency;
    private boolean payed;
    private Date payment_date;
    private int user_id;

    public Bill() {


    }



    public Bill(String payee, String type, Date date, String amount, String frequency) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public String getFormattedAmount() {
        // Format the amount as currency
        Locale locale = new Locale("en", "US"); // Adjust the locale based on your requirements
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }
}
