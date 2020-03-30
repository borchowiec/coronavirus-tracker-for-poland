package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.History;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HistoryServiceTest {

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
}