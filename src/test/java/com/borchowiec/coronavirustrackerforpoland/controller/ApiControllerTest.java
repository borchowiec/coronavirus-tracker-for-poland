package com.borchowiec.coronavirustrackerforpoland.controller;

import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.model.CurrentData;
import com.borchowiec.coronavirustrackerforpoland.model.News;
import com.borchowiec.coronavirustrackerforpoland.model.RegionalData;
import com.borchowiec.coronavirustrackerforpoland.payload.GraphDataResponse;
import com.borchowiec.coronavirustrackerforpoland.service.CurrentDataService;
import com.borchowiec.coronavirustrackerforpoland.service.HistoryService;
import com.borchowiec.coronavirustrackerforpoland.service.NewsService;
import com.borchowiec.coronavirustrackerforpoland.service.RegionalDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(ApiController.class)
class ApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HistoryService historyService;

    @MockBean
    private CurrentDataService currentDataService;

    @MockBean
    private RegionalDataService regionalDataService;

    @MockBean
    private NewsService newsService;

    @BeforeEach
    void buildMvc() {
        mvc = standaloneSetup(new ApiController(historyService, currentDataService, regionalDataService, newsService)).build();
    }

    @Test
    void getData_badRequest_shouldReturn400() throws Exception {
        mvc.perform(get("/api/wrong_value")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void getData_dataDoesntExist_shouldReturn204() throws Exception {
        when(historyService.getGraphData(any())).thenThrow(new DataNotAvailableException());
        mvc.perform(get("/api/deaths")).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void getData_dataExists_shouldReturnProperPayloadAnd200() throws Exception {
        // given
        List<GraphDataResponse> expected = Stream.of(new GraphDataResponse(100, LocalDate.parse("2020-02-02")),
                new GraphDataResponse(110, LocalDate.parse("2020-02-03"))).collect(Collectors.toList());

        // when
        when(historyService.getGraphData(any())).thenReturn(expected);
        ResultActions resultActions = mvc.perform(get("/api/recoveries"))
                .andDo(print())
                .andExpect(status().isOk());
        String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<GraphDataResponse> actual = Stream
                .of(objectMapper.readValue(responseAsString, GraphDataResponse[].class))
                .collect(Collectors.toList());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getCurrentData_obtainedData_shouldReturnCurrentData() throws Exception {
        CurrentData expected = new CurrentData(1000, 123, 321);

        when(currentDataService.getCurrentData()).thenReturn(expected);
        ResultActions resultActions = mvc.perform(get("/api/current"))
                .andDo(print())
                .andExpect(status().isOk());
        String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        CurrentData actual = objectMapper.readValue(responseAsString, CurrentData.class);

        assertEquals(expected, actual);
    }

    @Test
    void getRegionalData_dataDoesntExist_shouldReturn204() throws Exception {
        when(regionalDataService.getRegionalData()).thenThrow(new DataNotAvailableException());
        mvc.perform(get("/api/regional")).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void getRegionalData_dataExists_shouldReturnProperPayloadAnd200() throws Exception {
        // given
        List<RegionalData> expected = Stream.of(
                new RegionalData("podkarpackie", 100, 1,2),
                new RegionalData("mazowieckie", 200, 2,3)).collect(Collectors.toList());

        // when
        when(regionalDataService.getRegionalData()).thenReturn(expected);
        ResultActions resultActions = mvc.perform(get("/api/regional")
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
        String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<RegionalData> actual = Stream
                .of(objectMapper.readValue(responseAsString, RegionalData[].class))
                .collect(Collectors.toList());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getNews_dataDoesntExist_shouldReturn204() throws Exception {
        when(newsService.getNews()).thenThrow(new DataNotAvailableException());
        mvc.perform(get("/api/news")).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void getNews_dataExists_shouldReturnProperPayloadAnd200() throws Exception {
        // given
        List<News> expected = Stream.of(
                new News("title", LocalDate.of(2020, 1, 1), "url"),
                new News("title2", LocalDate.of(2019, 2, 12), "url")
        ).collect(Collectors.toList());

        // when
        when(newsService.getNews()).thenReturn(expected);
        ResultActions resultActions = mvc.perform(get("/api/news"))
                .andDo(print())
                .andExpect(status().isOk());
        String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<News> actual = Stream
                .of(objectMapper.readValue(responseAsString, News[].class))
                .collect(Collectors.toList());

        // then
        assertEquals(expected, actual);
    }

}