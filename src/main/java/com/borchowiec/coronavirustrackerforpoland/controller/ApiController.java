package com.borchowiec.coronavirustrackerforpoland.controller;

import com.borchowiec.coronavirustrackerforpoland.exception.BadRequestException;
import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.payload.GraphDataResponse;
import com.borchowiec.coronavirustrackerforpoland.service.GraphDataType;
import com.borchowiec.coronavirustrackerforpoland.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    private final HistoryService historyService;

    public ApiController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/api/{dataType}")
    public List<GraphDataResponse> getData(@PathVariable String dataType) throws JsonProcessingException {
        try {
            GraphDataType graphDataType = GraphDataType.valueOf(dataType.toUpperCase());
            return historyService.getGraphData(graphDataType);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Given wrong argument: " + dataType);
        } catch (DataNotAvailableException e) {
            historyService.updateHistoryList();
            throw e;
        }
    }
}
