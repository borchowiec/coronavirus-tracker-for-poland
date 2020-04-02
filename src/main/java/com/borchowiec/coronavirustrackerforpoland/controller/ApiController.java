package com.borchowiec.coronavirustrackerforpoland.controller;

import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.model.History;
import com.borchowiec.coronavirustrackerforpoland.payload.GraphDataResponse;
import com.borchowiec.coronavirustrackerforpoland.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private final HistoryService historyService;

    public ApiController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * @return Numbers of all confirmed cases each day with date.
     */
    @GetMapping("/api/confirmed")
    public List<GraphDataResponse> getAllConfirmed() {
        // gets current history with all data
        List<History> historyList = historyService.getHistoryList().orElseThrow(() -> {
            try {
                historyService.updateHistoryList();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new DataNotAvailableException();
        });

        // returns only wanted data
        return historyList.stream()
                .map(history -> new GraphDataResponse(history.getConfirmed(), history.getDate()))
                .collect(Collectors.toList());
    }

}
