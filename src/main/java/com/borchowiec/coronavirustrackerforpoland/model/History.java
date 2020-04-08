package com.borchowiec.coronavirustrackerforpoland.model;

import java.time.LocalDate;
import java.util.Objects;

public class History {
    private LocalDate date;
    private int confirmed;
    private int newConfirmed;
    private int deaths;
    private int newDeaths;
    private int recovered;
    private int newRecovered;
    private int activeCases;

    public History() {
    }

    public History(LocalDate date, int confirmed, int newConfirmed, int deaths, int newDeaths, int recovered, int newRecovered, int activeCases) {
        this.date = date;
        this.confirmed = confirmed;
        this.newConfirmed = newConfirmed;
        this.deaths = deaths;
        this.newDeaths = newDeaths;
        this.recovered = recovered;
        this.newRecovered = newRecovered;
        this.activeCases = activeCases;
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

    public int getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(int activeCases) {
        this.activeCases = activeCases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return confirmed == history.confirmed &&
                newConfirmed == history.newConfirmed &&
                deaths == history.deaths &&
                newDeaths == history.newDeaths &&
                recovered == history.recovered &&
                newRecovered == history.newRecovered &&
                activeCases == history.activeCases &&
                Objects.equals(date, history.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, confirmed, newConfirmed, deaths, newDeaths, recovered, newRecovered, activeCases);
    }

    @Override
    public String toString() {
        return "History{" +
                "date=" + date +
                ", confirmed=" + confirmed +
                ", newConfirmed=" + newConfirmed +
                ", deaths=" + deaths +
                ", newDeaths=" + newDeaths +
                ", recovered=" + recovered +
                ", newRecovered=" + newRecovered +
                ", activeCases=" + activeCases +
                '}';
    }
}
