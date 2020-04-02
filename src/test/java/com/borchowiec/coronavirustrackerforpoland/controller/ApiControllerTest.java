package com.borchowiec.coronavirustrackerforpoland.controller;

import com.borchowiec.coronavirustrackerforpoland.model.History;
import com.borchowiec.coronavirustrackerforpoland.payload.GraphDataResponse;
import com.borchowiec.coronavirustrackerforpoland.service.HistoryService;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ApiController.class)
class ApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HistoryService historyService;

    @BeforeEach
    void buildMvc() {
        mvc = standaloneSetup(new ApiController(historyService)).build();
    }

    @Test
    void getAllConfirmed_dataDoesntExist_shouldReturn204() throws Exception {
        when(historyService.getHistoryList()).thenReturn(Optional.empty());
        mvc.perform(get("/api/confirmed")).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void getAllConfirmed_dataExists_shouldReturnProperPayloadAnd200() throws Exception {
        // given
        List<History> historyList = Stream.of(new History(LocalDate.parse("2020-03-28"), 200, 200,
                10, 10, 100, 100),
                new History(LocalDate.parse("2020-03-29"), 210, 10, 11, 1,
                        110, 10)).collect(Collectors.toList());

        // when
        when(historyService.getHistoryList()).thenReturn(Optional.of(historyList));
        ResultActions resultActions = mvc.perform(get("/api/confirmed"))
                .andDo(print())
                .andExpect(status().isOk());
        String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<GraphDataResponse> actual = Stream
                .of(objectMapper.readValue(responseAsString, GraphDataResponse[].class))
                .collect(Collectors.toList());

        // then
        List<GraphDataResponse> expected = historyList
                .stream().map(history -> new GraphDataResponse(history.getConfirmed(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }
}