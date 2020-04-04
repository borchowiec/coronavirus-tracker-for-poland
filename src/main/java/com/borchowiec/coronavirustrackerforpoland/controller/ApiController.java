package com.borchowiec.coronavirustrackerforpoland.controller;

import com.borchowiec.coronavirustrackerforpoland.exception.BadRequestException;
import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.model.CurrentData;
import com.borchowiec.coronavirustrackerforpoland.payload.GraphDataResponse;
import com.borchowiec.coronavirustrackerforpoland.service.CurrentDataService;
import com.borchowiec.coronavirustrackerforpoland.service.GraphDataType;
import com.borchowiec.coronavirustrackerforpoland.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ApiController {

    private final HistoryService historyService;
    private final CurrentDataService currentDataService;

    public ApiController(HistoryService historyService, CurrentDataService currentDataService) {
        this.historyService = historyService;
        this.currentDataService = currentDataService;
    }

    /**
     * Gets list of {@link GraphDataResponse GraphDataResponses} that contains specific data about coronavirus.
     * You have to specify what type of data you want to receive by giving proper argument:
     * <ol>
     *     <li>confirmed</li>
     *     <li>deaths</li>
     *     <li>recoveries</li>
     *     <li>new_confirmed</li>
     *     <li>new_deaths</li>
     *     <li>new_recoveries</li>
     * </ol>
     * @param dataType Argument that specifies what type of data will be returned.
     * @return List of {@link GraphDataResponse GraphDataResponses} that contains specific data about coronavirus.
     * @throws JsonProcessingException
     */
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

    @GetMapping("/api/current")
    public CurrentData getCurrentData() throws IOException {
        return currentDataService.getCurrentData();
    }
}
