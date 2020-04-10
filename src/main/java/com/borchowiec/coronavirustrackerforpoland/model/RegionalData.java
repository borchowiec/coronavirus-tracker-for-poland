package com.borchowiec.coronavirustrackerforpoland.model;

import java.util.Objects;

public class RegionalData {
    private String region;
    private int confirmed;
    private int deaths;
    private int recoveries;

    public RegionalData() {
    }

    public RegionalData(String region, int confirmed, int deaths, int recoveries) {
        this.region = region;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recoveries = recoveries;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
        RegionalData that = (RegionalData) o;
        return confirmed == that.confirmed &&
                deaths == that.deaths &&
                recoveries == that.recoveries &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, confirmed, deaths, recoveries);
    }

    @Override
    public String toString() {
        return "RegionalData{" +
                "region='" + region + '\'' +
                ", confirmed=" + confirmed +
                ", deaths=" + deaths +
                ", recoveries=" + recoveries +
                '}';
    }
}
