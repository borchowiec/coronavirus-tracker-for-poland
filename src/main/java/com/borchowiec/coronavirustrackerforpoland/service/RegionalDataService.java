package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.RegionalData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RegionalDataService {
    private List<RegionalData> regionalData;

    @Scheduled(fixedDelayString = "${update.regionalData.delay}")
    public void getRegionalData() throws IOException {
        String url = "https://www.rynekzdrowia.pl/koronawirus/status?v=poziom";
        Document doc = Jsoup.connect(url).get();

        String jsonData = doc.head().getElementsByTag("script").get(2).childNodes().get(0)
                .toString().split("\n")[2].replace("\t\tvar values = ", "")
                .replace(";", "");

        regionalData = toRegionalData(jsonData);
    }

    public List<RegionalData> toRegionalData(String jsonData) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonData);

        return StreamSupport.stream(root.spliterator(), false)
                .filter(node -> node.has("nazwa"))
                .filter(node -> node.has("zarazeni"))
                .filter(node -> node.has("zgony"))
                .filter(node -> node.has("zdrowi"))
                .map(node -> new RegionalData(node.get("nazwa").asText(), node.get("zarazeni").asInt(),
                        node.get("zgony").asInt(), node.get("zdrowi").asInt()))
                .collect(Collectors.toList());
    }
}
