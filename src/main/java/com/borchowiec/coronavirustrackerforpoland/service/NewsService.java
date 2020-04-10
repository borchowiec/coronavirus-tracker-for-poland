package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.model.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private List<News> news;

    /**
     * Updates {@link #news} every specific time.
     * @throws IOException
     */
    @Scheduled(fixedDelayString = "${update.regionalData.delay}")
    public void updateNews() throws IOException {
        news = getNewsFromDifferentPages();
    }

    public List<News> getNews() {
        if (news == null) {
            throw new DataNotAvailableException();
        }
        return news;
    }

    /**
     * Takes newses from different web pages. Newses are sorted by date. The newest is first.
     * @return Newses about coronavirus.
     * @throws IOException
     */
    private List<News> getNewsFromDifferentPages() throws IOException {
        List<News> result = new LinkedList<>();
        result.addAll(Objects.requireNonNull(getPzhNews()));
        result.addAll(Objects.requireNonNull(getGovNews()));
        result.sort(Comparator.comparing(News::getDate).reversed());
        return result;
    }

    /**
     * @return News from gov.pl
     * @throws IOException
     */
    private List<News> getGovNews() throws IOException {
        String url = "https://www.gov.pl/web/koronawirus/komunikaty-resortow";
        Document doc = Jsoup.connect(url).get();

        return doc.getElementsByClass("art-prev").get(0).getElementsByTag("li").stream()
                .map(element -> {
                    String title = element.getElementsByClass("title").get(0).text();
                    String href = element.getElementsByTag("a").get(0).attributes().get("href");
                    String stringDate = element.getElementsByClass("date").get(0).text();
                    LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    return new News(title, date, "https://www.gov.pl" + href);
                })
                .collect(Collectors.toList());
    }

    /**
     * @return News from pzh.gov.pl
     * @throws IOException
     */
    private List<News> getPzhNews() throws IOException {
        String url = "https://www.pzh.gov.pl/aktualnosci-dot-wirusa-sars-cov-2/";
        Document doc = Jsoup.connect(url).get();

        return doc.getElementsByClass("elementor-post__text")
                .stream()
                .map(element -> {
                    Element anchor = element.getElementsByTag("a").get(0);
                    String href = anchor.attributes().get("href");
                    String title = anchor.text();
                    String stringDate = element.getElementsByClass("elementor-post-date").get(0).text();
                    LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("d MMMM yyyy"));
                    return new News(title, date, href);
                })
                .collect(Collectors.toList());
    }
}
