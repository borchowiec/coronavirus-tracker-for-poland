package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.exception.DataNotAvailableException;
import com.borchowiec.coronavirustrackerforpoland.model.History;
import com.borchowiec.coronavirustrackerforpoland.payload.GraphDataResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class HistoryServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    public static List<History> getHistoryListExample() {
        return Stream.of(new History(LocalDate.parse("2020-03-28"), 200, 200, 10,
                        10, 100, 100, 90),
                new History(LocalDate.parse("2020-03-29"), 210, 10, 11, 1,
                        110, 10, 130)).collect(Collectors.toList());
    }

    @Test
    void getHistoryList_dataExists_shouldReturnOptionalWithData() {
        // given
        List<History> historyList = new LinkedList<>();
        historyList.add(new History(LocalDate.now(), 100, 2, 10, 1,
                20, 3, 23));
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        Optional<List<History>> expected = Optional.of(historyList);

        // when
        Optional<List<History>> actual = historyService.getHistoryList();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getHistoryList_dataDoesntExist_shouldReturnEmptyOptional() {
        // given
        HistoryService historyService = new HistoryService();
        Optional<List<History>> expected = Optional.empty();

        // when
        Optional<List<History>> actual = historyService.getHistoryList();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void updateHistoryList_cannotDownloadData_shouldNotUpdateList() throws JsonProcessingException {
        // given
        List<History> oldHistoryList = new LinkedList<>();
        oldHistoryList.add(new History(LocalDate.now(), 100, 2, 10, 1,
                20, 3, 123));

        HistoryService historyService = new HistoryService(restTemplate);
        historyService.setHistoryList(oldHistoryList);

        Optional<List<History>> expected = Optional.of(oldHistoryList);

        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);
        historyService.updateHistoryList();

        // then
        Optional<List<History>> actual = historyService.getHistoryList();
        assertEquals(expected, actual);
    }

    @Test
    void updateHistoryList_canDownloadData_shouldUpdateList() throws JsonProcessingException {
        // given
        List<History> historyList = new LinkedList<>();
        historyList.add(new History(LocalDate.now(), 100, 2, 10, 1,
                20, 3, 123));

        HistoryService historyService = spy(new HistoryService(restTemplate));
        Optional<List<History>> expected = Optional.of(historyList);

        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);
        doReturn(historyList).when(historyService).parseHistoryList(responseEntity);
        historyService.updateHistoryList();

        // then
        Optional<List<History>> actual = historyService.getHistoryList();
        assertEquals(expected, actual);
    }

    @Test
    void parseHistoryList_containsElementsThatHas0Confirmed_shouldReturnTrimmedList() throws JsonProcessingException {
        // given
        String body = "{\n" +
                "  \"count\": 5,\n" +
                "  \"result\": {\n" +
                "    \"2020-03-14\": {\n" +
                "      \"confirmed\": 0,\n" +
                "      \"deaths\": 0,\n" +
                "      \"recovered\": 0\n" +
                "    },\n" +
                "    \"2020-03-15\": {\n" +
                "      \"confirmed\": 0,\n" +
                "      \"deaths\": 0,\n" +
                "      \"recovered\": 0\n" +
                "    },\n" +
                "    \"2020-03-16\": {\n" +
                "      \"confirmed\": 100,\n" +
                "      \"deaths\": 3,\n" +
                "      \"recovered\": 1\n" +
                "    },\n" +
                "    \"2020-03-17\": {\n" +
                "      \"confirmed\": 120,\n" +
                "      \"deaths\": 9,\n" +
                "      \"recovered\": 2\n" +
                "    },\n" +
                "    \"2020-03-18\": {\n" +
                "      \"confirmed\": 140,\n" +
                "      \"deaths\": 12,\n" +
                "      \"recovered\": 3\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);
        HistoryService historyService = new HistoryService();

        // when
        List<History> actual = historyService.parseHistoryList(responseEntity);

        // then
        List<History> expected = Stream.of(
                new History(LocalDate.of(2020, 3, 16),
                        100, 100, 3, 3, 1, 1, 96),
                new History(LocalDate.of(2020, 3, 17),
                        120, 20, 9, 6, 2, 1, 109),
                new History(LocalDate.of(2020, 3, 18),
                        140, 20, 12, 3, 3, 1, 125)
        ).collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @Test
    void parseHistoryList_responseContains0Elements_shouldReturnEmptyList() throws JsonProcessingException {
        // given
        String body = "{\n" +
                "  \"count\": 0,\n" +
                "  \"result\": {\n" +
                "  }\n" +
                "}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);
        HistoryService historyService = new HistoryService();

        // when
        List<History> actual = historyService.parseHistoryList(responseEntity);

        // then
        assertEquals(0, actual.size());
    }

    @Test
    void parseHistoryList_containsWrongElements_shouldReturnListWithoutTheseElements() throws JsonProcessingException {
        // todo split it into methods
        // todo figure out how to do it with stream. The problem is there is no connection with parent in json nodes.
        // given
        String body = "{\n" +
                "  \"count\": 4,\n" +
                "  \"result\": {\n" +
                "    \"2020-03-16\": {\n" +
                "      \"confirmed\": 100,\n" +
                "      \"recovered\": 1\n" +
                "    },\n" +
                "    \"2020-03-17\": {\n" +
                "      \"deaths\": 9,\n" +
                "      \"recovered\": 2\n" +
                "    },\n" +
                "    \"2020-03-18\": {\n" +
                "      \"confirmed\": 140,\n" +
                "      \"deaths\": 12\n" +
                "    },\n" +
                "    \"2020-03-19\": {\n" +
                "      \"confirmed\": 140,\n" +
                "      \"deaths\": 12,\n" +
                "      \"recovered\": 1\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);
        HistoryService historyService = new HistoryService();

        // when
        List<History> actual = historyService.parseHistoryList(responseEntity);

        // then
        List<History> expected = Stream.of(
                new History(LocalDate.of(2020, 3, 19),
                        140, 140, 12, 12, 1, 1, 127)
        ).collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    // getGraphData ====================================================================================================
    @Test
    void getGraphData_dataDoesntExist_shouldThrowDataNotAvailableException() {
        HistoryService historyService = new HistoryService();
        assertThrows(DataNotAvailableException.class, () -> historyService.getGraphData(GraphDataType.CONFIRMED));
    }

    @Test
    void getGraphData_givenConfirmed_shouldReturnConfirmedData() {
        // given
        GraphDataType argument = GraphDataType.CONFIRMED;
        List<History> historyList = getHistoryListExample();

        // when
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        List<GraphDataResponse> actual = historyService.getGraphData(argument);

        // then
        List<GraphDataResponse> expected = historyList.stream()
                .map(history -> new GraphDataResponse(history.getConfirmed(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(actual, expected);
    }

    @Test
    void getGraphData_givenDeaths_shouldReturnDeathsData() {
        // given
        GraphDataType argument = GraphDataType.DEATHS;
        List<History> historyList = getHistoryListExample();

        // when
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        List<GraphDataResponse> actual = historyService.getGraphData(argument);

        // then
        List<GraphDataResponse> expected = historyList.stream()
                .map(history -> new GraphDataResponse(history.getDeaths(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(actual, expected);
    }

    @Test
    void getGraphData_givenRecoveries_shouldReturnRecoveriesData() {
        // given
        GraphDataType argument = GraphDataType.RECOVERIES;
        List<History> historyList = getHistoryListExample();

        // when
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        List<GraphDataResponse> actual = historyService.getGraphData(argument);

        // then
        List<GraphDataResponse> expected = historyList.stream()
                .map(history -> new GraphDataResponse(history.getRecovered(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(actual, expected);
    }

    @Test
    void getGraphData_givenNewConfirmed_shouldReturnNewConfirmedData() {
        // given
        GraphDataType argument = GraphDataType.NEW_CONFIRMED;
        List<History> historyList = getHistoryListExample();

        // when
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        List<GraphDataResponse> actual = historyService.getGraphData(argument);

        // then
        List<GraphDataResponse> expected = historyList.stream()
                .map(history -> new GraphDataResponse(history.getNewConfirmed(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(actual, expected);
    }

    @Test
    void getGraphData_givenNewDeaths_shouldReturnNewDeathsData() {
        // given
        GraphDataType argument = GraphDataType.NEW_DEATHS;
        List<History> historyList = getHistoryListExample();

        // when
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        List<GraphDataResponse> actual = historyService.getGraphData(argument);

        // then
        List<GraphDataResponse> expected = historyList.stream()
                .map(history -> new GraphDataResponse(history.getNewDeaths(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(actual, expected);
    }

    @Test
    void getGraphData_givenNewRecoveries_shouldReturnNewRecoveriesData() {
        // given
        GraphDataType argument = GraphDataType.NEW_RECOVERIES;
        List<History> historyList = getHistoryListExample();

        // when
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        List<GraphDataResponse> actual = historyService.getGraphData(argument);

        // then
        List<GraphDataResponse> expected = historyList.stream()
                .map(history -> new GraphDataResponse(history.getNewRecovered(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(actual, expected);
    }

    @Test
    void getGraphData_givenActiveCases_shouldReturnActiveCasesData() {
        // given
        GraphDataType argument = GraphDataType.ACTIVE_CASES;
        List<History> historyList = getHistoryListExample();

        // when
        HistoryService historyService = new HistoryService();
        historyService.setHistoryList(historyList);
        List<GraphDataResponse> actual = historyService.getGraphData(argument);

        // then
        List<GraphDataResponse> expected = historyList.stream()
                .map(history -> new GraphDataResponse(history.getActiveCases(), history.getDate()))
                .collect(Collectors.toList());
        assertEquals(actual, expected);
    }
}