package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.History;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

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

    /**
     * Downloads coronavirus data from external api and updates {@link #historyList}
     * @throws JsonProcessingException When the response data is not in the expected format.
     */
    @Scheduled(fixedDelayString = "${update.history.delay}")
    public void updateHistoryList() throws JsonProcessingException {
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

    /**
     * Parses response, that contains coronavirus data from external api, to list containing {@link History} objects.
     * @param response Coronavirus api response.
     * @return List with {@link History} objects.
     * @throws JsonProcessingException When the response data is not in the expected format.
     */
    public List<History> parseHistoryList(ResponseEntity<String> response) throws JsonProcessingException {
        // getting "result" object from json. This object contains data that we want to get.
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(Objects.requireNonNull(response.getBody()));
        JsonNode elements = root.get("result");
        List<History> result = new LinkedList<>();

        // creating and adding new "History" objects to result list
        elements.fields().forEachRemaining(entry -> {
            JsonNode node = entry.getValue();
            if (node.has("confirmed") // check if node contains all mandatory fields
                    && node.get("confirmed").asInt() > 0 // skip elements that has 0 confirmed cases. It trims final list.
                    && node.has("deaths")
                    && node.has("recovered")) {
                History history = new History();
                history.setDate(LocalDate.parse(entry.getKey())); // setting data. "New" fields will be set later.
                history.setConfirmed(node.get("confirmed").asInt());
                history.setDeaths(node.get("deaths").asInt());
                history.setRecovered(node.get("recovered").asInt());
                result.add(history);
            }
        });

        // if result list is empty, there is no need to continue this method
        if (result.size() < 1) {
            return result;
        }

        // first element should "new" fields the same as current
        History firstElement = result.get(0);
        firstElement.setNewConfirmed(firstElement.getConfirmed());
        firstElement.setNewDeaths(firstElement.getDeaths());
        firstElement.setNewRecovered(firstElement.getRecovered());

        // counting "newConfirmed", "newDeaths" and "newRecovered" for every element
        ListIterator<History> iterator = result.listIterator();
        while (iterator.hasNext()) {
            History previous = iterator.next();
            if (iterator.hasNext()) {
                History current = iterator.next();
                iterator.previous();

                current.setNewConfirmed(current.getConfirmed() - previous.getConfirmed());
                current.setNewDeaths(current.getDeaths() - previous.getDeaths());
                current.setNewRecovered(current.getRecovered() - previous.getRecovered());
            }
        }

        return result;
    }
}
