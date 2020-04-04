package com.borchowiec.coronavirustrackerforpoland.model;

import java.util.Objects;

public class CurrentData {
    private int confirmed;
    private int deaths;
    private int recoveries;

    public CurrentData(int confirmed, int deaths, int recoveries) {
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recoveries = recoveries;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecoveries() {
        return recoveries;
    }

    public void setRecoveries(int recoveries) {
        this.recoveries = recoveries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentData that = (CurrentData) o;
        return confirmed == that.confirmed &&
                deaths == that.deaths &&
                recoveries == that.recoveries;
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmed, deaths, recoveries);
    }

    @Override
    public String toString() {
        return "CurrentData{" +
                "confirmed=" + confirmed +
                ", deaths=" + deaths +
                ", recoveries=" + recoveries +
                '}';
    }
}
