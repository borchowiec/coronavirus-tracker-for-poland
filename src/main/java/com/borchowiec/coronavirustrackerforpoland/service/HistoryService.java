package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.History;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {
    private List<History> historyList;
    private RestTemplate restTemplate;

    public HistoryService() {
        restTemplate = new RestTemplate();
    }

    public HistoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedDelayString = "${update.history.delay}")
    public void updateHistoryList() {
        String url = "https://covidapi.info/api/v1/country/POL";
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            historyList = parseHistoryList(response);
        }
    }

    public Optional<List<History>> getHistoryList() {
        if (historyList == null) {
            return Optional.empty();
        }
        return Optional.of(historyList);
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
    }

    public List<History> parseHistoryList(ResponseEntity<String> responseEntity) {
        return null;
    }
}
