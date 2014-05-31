package com.possebom.mymedicines.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Medicine {
    private long id;
    private String brandName;
    private String drug;
    private String laboratory;
    private String concentration;
    private String form;
    private int month;
    private int year;
    private String barcode;
    private String country;
    private boolean inServer = false;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getValidity() {
        final Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, getYear());
        date.set(Calendar.MONTH, getMonth());
        date.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy", Locale.ENGLISH);
        String validity = dateFormat.format(date.getTime());

        return validity;
    }

    @Override
    public String toString() {
        return getBrandName();
    }

    public boolean isInServer() {
        return inServer;
    }

    public void setInServer(boolean inServer) {
        this.inServer = inServer;
    }
}
