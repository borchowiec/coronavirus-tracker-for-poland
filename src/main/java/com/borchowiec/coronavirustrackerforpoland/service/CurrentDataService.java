package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.CurrentData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CurrentDataService {

    public CurrentData getCurrentData() throws IOException {
        String url = "https://www.worldometers.info/coronavirus/country/poland/";
        Document doc = Jsoup.connect(url).get();
        Elements wrappers = doc.body().getElementsByClass("maincounter-number");

        int confirmed = Integer.parseInt(wrappers.get(0).text().replace(",", ""));
        int deaths = Integer.parseInt(wrappers.get(1).text().replace(",", ""));
        int recoveries = Integer.parseInt(wrappers.get(2).text().replace(",", ""));

        return new CurrentData(confirmed, recoveries, deaths);
    }
}
