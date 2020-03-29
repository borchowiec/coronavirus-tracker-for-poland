package com.borchowiec.coronavirustrackerforpoland.model;

import java.time.LocalDate;

public class History {
    private LocalDate date;
    private int confirmed;
    private int newConfirmed;
    private int deaths;
    private int newDeaths;
    private int recovered;
    private int newRecovered;

    public History() {
    }

    public History(LocalDate date, int confirmed, int newConfirmed, int deaths, int newDeaths, int recovered, int newRecovered) {
        this.date = date;
        this.confirmed = confirmed;
        this.newConfirmed = newConfirmed;
        this.deaths = deaths;
        this.newDeaths = newDeaths;
        this.recovered = recovered;
        this.newRecovered = newRecovered;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(int newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(int newRecovered) {
        this.newRecovered = newRecovered;
    }
}
