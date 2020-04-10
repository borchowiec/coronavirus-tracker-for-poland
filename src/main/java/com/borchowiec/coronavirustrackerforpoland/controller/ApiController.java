package com.borchowiec.coronavirustrackerforpoland.controller;

import com.borchowiec.coronavirustrackerforpoland.exception.BadRequestException;
import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.model.CurrentData;
import com.borchowiec.coronavirustrackerforpoland.model.News;
import com.borchowiec.coronavirustrackerforpoland.model.RegionalData;
import com.borchowiec.coronavirustrackerforpoland.payload.GraphDataResponse;
import com.borchowiec.coronavirustrackerforpoland.service.*;
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
    private final RegionalDataService regionalDataService;
    private final NewsService newsService;

    public ApiController(HistoryService historyService, CurrentDataService currentDataService, RegionalDataService regionalDataService, NewsService newsService) {
        this.historyService = historyService;
        this.currentDataService = currentDataService;
        this.regionalDataService = regionalDataService;
        this.newsService = newsService;
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

    /**
     * @return Current status of Poland.
     * @throws IOException
     */
    @GetMapping("/api/current")
    public CurrentData getCurrentData() throws IOException {
        return currentDataService.getCurrentData();
    }

    /**
     * @return Data containing status of every region in Poland.
     * @throws IOException
     */
    @GetMapping("/api/regional")
    public List<RegionalData> getRegionalData() throws IOException {
        try {
            return regionalDataService.getRegionalData();
        } catch (DataNotAvailableException e) {
            // if there is no data, try to update it
            regionalDataService.updateRegionalData();
            throw e;
        }
    }

    /**
     * @return Newest news about coronavirus.
     * @throws IOException
     */
    @GetMapping("/api/news")
    public List<News> getNews() throws IOException {
        try {
            return newsService.getNews();
        } catch (DataNotAvailableException e) {
            // if there is no data, try to update it
            newsService.updateNews();
            throw e;
        }
    }
}
