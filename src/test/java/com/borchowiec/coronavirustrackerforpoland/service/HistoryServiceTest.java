package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.History;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class HistoryServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void getHistoryList_dataExists_shouldReturnOptionalWithData() {
        // given
        List<History> historyList = new LinkedList<>();
        historyList.add(new History(LocalDate.now(), 100, 2, 10, 1,
                20, 3));
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
    void updateHistoryList_cannotDownloadData_shouldNotUpdateList() {
        // given
        List<History> oldHistoryList = new LinkedList<>();
        oldHistoryList.add(new History(LocalDate.now(), 100, 2, 10, 1,
                20, 3));

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
    void updateHistoryList_canDownloadData_shouldUpdateList() {
        // given
        List<History> historyList = new LinkedList<>();
        historyList.add(new History(LocalDate.now(), 100, 2, 10, 1,
                20, 3));

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
}