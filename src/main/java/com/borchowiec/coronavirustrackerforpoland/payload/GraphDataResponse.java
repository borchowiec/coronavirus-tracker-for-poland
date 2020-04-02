package com.borchowiec.coronavirustrackerforpoland.payload;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Contains value over the day. Could be used as payload containing graphs data.
 */
public class GraphDataResponse {
    private int value;
    private LocalDate date;

    public GraphDataResponse(int value, LocalDate date) {
        this.value = value;
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
        GraphDataResponse that = (GraphDataResponse) o;
        return value == that.value &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, date);
    }

    @Override
    public String toString() {
        return "AllConfirmedResponse{" +
                "value=" + value +
                ", date=" + date +
                '}';
    }
}
