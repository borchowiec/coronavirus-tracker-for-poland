package com.borchowiec.coronavirustrackerforpoland.model;

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
}
