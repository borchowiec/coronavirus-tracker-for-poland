package com.borchowiec.coronavirustrackerforpoland.model;

import java.time.LocalDate;
import java.util.Objects;

public class News {
    private String title;
    private LocalDate date;
    private String url;

    public News() {
    }

    public News(String title, LocalDate date, String url) {
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(title, news.title) &&
                Objects.equals(date, news.date) &&
                Objects.equals(url, news.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date, url);
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", url='" + url + '\'' +
                '}';
    }
}
