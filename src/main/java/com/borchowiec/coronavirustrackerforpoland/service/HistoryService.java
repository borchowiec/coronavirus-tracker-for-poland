package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.History;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private List<History> historyList;

    @Scheduled(fixedDelayString = "${update.history.delay}")
    public void updateHistoryList() {

    }
}
