package com.borchowiec.coronavirustrackerforpoland.controller;

import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.model.History;
import com.borchowiec.coronavirustrackerforpoland.payload.AllConfirmedResponse;
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

    @GetMapping("/api/confirmed")
    public List<AllConfirmedResponse> getAllConfirmed() {
        List<History> historyList = historyService.getHistoryList().orElseThrow(() -> {
            try {
                historyService.updateHistoryList();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new DataNotAvailableException();
        });

        return historyList.stream()
                .map(history -> new AllConfirmedResponse(history.getConfirmed(), history.getDate()))
                .collect(Collectors.toList());
    }

}
