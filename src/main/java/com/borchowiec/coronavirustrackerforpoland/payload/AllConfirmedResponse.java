package com.borchowiec.coronavirustrackerforpoland.payload;

import java.time.LocalDate;
import java.util.Objects;

public class AllConfirmedResponse {
    private int confirmed;
    private LocalDate date;

    public AllConfirmedResponse(int confirmed, LocalDate date) {
        this.confirmed = confirmed;
        this.date = date;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllConfirmedResponse that = (AllConfirmedResponse) o;
        return confirmed == that.confirmed &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmed, date);
    }

    @Override
    public String toString() {
        return "AllConfirmedResponse{" +
                "confirmed=" + confirmed +
                ", date=" + date +
                '}';
    }
}
